import dao.Dao;
import dao.SudokuBoardDaoFactory;
import database.Board;
import database.DbManager;
import exceptions.SudokuReadException;
import exceptions.SudokuWriteException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.MySecondSudokuSolver;
import model.SudokuBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StackPaneController {
    private final Logger log = LoggerFactory.getLogger(StackPaneController.class);

    @FXML
    private GridPaneController gridPaneController;

    private SudokuBoard sudokuBoard = new SudokuBoard();
    private MySecondSudokuSolver solver = new MySecondSudokuSolver();
    private Dao<SudokuBoard> dao;
    private int lastClickedButton = -1;
    private int lastValClickedButton = -1;
    private int lastDiffClickedButton = -1;
    private List<Button> buttons;
    private List<Button> valButtons;
    private ArrayList<Button> difficultyButtons;

    @FXML
    private TextField textField;
    @FXML
    private Button saveButton;
    @FXML
    private ChoiceBox<String> loadChoiceBox;
    @FXML
    private Button loadButton;
    @FXML
    private GridPane gridPane1;
    @FXML
    private VBox valPanel;
    @FXML
    private Button difficulty1;
    @FXML
    private Button difficulty2;
    @FXML
    private Button difficulty3;

    @FXML
    public void initialize() {
        gridPane1 = gridPaneController.getGPane();
        ObservableList<Node> children = gridPane1.getChildren();
        List<Button> p = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            p.add(new Button());
        }
        int x, y;
        for (int i = 0; i < 81; i++) {
            if (gridPane1.getRowIndex(children.get(i)) == null) {
                x = 0;
            } else {
                x = gridPane1.getRowIndex(children.get(i));
            }
            if (gridPane1.getColumnIndex(children.get(i)) == null) {
                y = 0;
            } else {
                y = gridPane1.getColumnIndex(children.get(i));
            }
            p.set(x * 9 + y, (Button) children.get(i));
        }

        buttons = p;

        ObservableList<Node> valPanelChildren= valPanel.getChildren();
        ArrayList<Button> valButtonsTmp = new ArrayList<Button>();
        for (int i = 0; i < 10; i++) {
            valButtonsTmp.add((Button) valPanelChildren.get(i));
        }
        valButtons = valButtonsTmp;
        valButtonsEvents();
        difficultyButtons = new ArrayList<>();
        difficultyButtons.add(difficulty1);
        difficultyButtons.add(difficulty2);
        difficultyButtons.add(difficulty3);

        valPanel.setVisible(false);
        gridPane1.setVisible(false);
        saveButton.setVisible(false);
        loadButton.setVisible(false);
        textField.setPromptText("Nazwa zapisu");
        textField.setVisible(false);
        getAllSaves();

        saveButton.setOnMouseClicked(e -> saveSudoku());

        loadChoiceBox.setOnMouseClicked(e -> {
            getAllSaves();
            loadChoiceBox.show();
            loadButton.setVisible(true);
            textField.setVisible(true);
        });

        loadButton.setOnMouseClicked(e -> {
            difficulty1.setVisible(false);
            difficulty2.setVisible(false);
            difficulty3.setVisible(false);
            loadButton.setVisible(false);
            gridPane1.setVisible(true);
            saveButton.setVisible(true);

            for (Button b : buttons) {
                b.setOnAction(c -> { });
                b.setOnMouseClicked(c -> { });
            }

            SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
            dao = factory.getDbDao(DbManager.getDatabaseUrl(), loadChoiceBox.getValue());

            try {
                sudokuBoard = dao.read();
            } catch (SudokuReadException r) {
                log.error("Błąd wczytywania planszy: ", r.getCause());
            }
            log.info("Wczytano Sudoku");
            log.info("Tablica pustych pól: {}", sudokuBoard.getEmptyFields().toString());

            buttonsEvents();
            //wyswietlenie sudoku na planszy
            for (int j = 0; j < buttons.size(); j++) {
                if (sudokuBoard.getFieldsArray().get(j).getFieldValue() == 0) {
                    buttons.get(j).setText("");
                } else {
                    buttons.get(j).setText(Integer.toString(sudokuBoard.getFieldsArray().get(j).getFieldValue()));
                }
            }

            for (int j = 0; j < sudokuBoard.getEmptyFields().size(); j++) {
                for (int i = 0; i < sudokuBoard.getFieldsArray().size(); i++) {
                    if (sudokuBoard.getEmptyFields().get(j) == (sudokuBoard.getFieldsArray().get(i))
                        && sudokuBoard.getFieldsArray().get(i).getFieldValue() != 0) {
                        buttons.get(i).setFont(new Font("System Bold", 14));
                        buttons.get(i).setTextFill(new Color(0, 0, 1, 1));
                    }
                }
            }
        });
    }

    @FXML
    public void NewGameEvent() {
        log.info("Rozpoczęto nową grę.");
        gridPane1.setVisible(false);
        valPanel.setVisible(false);
        saveButton.setVisible(false);
        for (Button b : buttons) {
            b.setDisable(false);
            b.setTextFill(new Color(0, 0, 0, 1));
            b.setFont(new Font("System", 15));
        }

        for (Button b : buttons) {
            b.setOnAction(e -> { });
            b.setOnMouseClicked(e -> { });
        }
        sudokuBoard = new SudokuBoard();
        solver.solve(sudokuBoard);
        differenceButtonsEvents();

        difficulty1.setVisible(true);
        difficulty2.setVisible(true);
        difficulty3.setVisible(true);
}

    @FXML
    public void valChosenEvent() {
        //sudokuBoard.set(lastClickedButton, Integer.parseInt(valButtons.get(lastValClickedButton).getText()));
        if (sudokuBoard.set(lastClickedButton, Integer.parseInt(valButtons.get(lastValClickedButton).getText()))) {
            if (lastValClickedButton == 0) {
                buttons.get(lastClickedButton).setText("");
                sudokuBoard.set(lastClickedButton, 0);
            } else {
                buttons.get(lastClickedButton).setFont(new Font("System Bold", 14));
                buttons.get(lastClickedButton).setTextFill(new Color(0, 0, 1, 1));
                buttons.get(lastClickedButton).setText(valButtons.get(lastValClickedButton).getText());
            }
            log.info("Wstawiono liczbe: {}", valButtons.get(lastValClickedButton).getText());
            log.info("Liczba pozostalych pol do wypelnienia: {}", sudokuBoard.amIWinner());
        } else {
            log.info("Nie wstawiono liczby {}", valButtons.get(lastValClickedButton).getText());
        }
        valPanel.setVisible(false);
        lastClickedButton = -1;
        lastValClickedButton = -1;
    }


    private void valButtonsEvents() {
        for (int i = 0; i < valButtons.size(); i++) {
            final int valButtonNumber = i;

            valButtons.get(i).setOnMouseEntered(e -> {
                for (Button b: buttons) {
                    if (b.getText().equals(valButtons.get(valButtonNumber).getText())) {
                        b.setStyle("-fx-background-color: #afcbf7;");
                    }
                }
            });

            valButtons.get(i).setOnMouseExited(e -> {
                for (Button b: buttons) {
                    if (b.getText().equals(valButtons.get(valButtonNumber).getText())) {
                        b.setStyle("0");
                    }
                }
            });

            valButtons.get(i).setOnAction((e) -> {
                lastValClickedButton = valButtonNumber;
                log.info("Zmieniasz wartosc dla przycisku: {}", lastClickedButton);
            });
        }
    }

    private void differenceButtonsEvents() {
        for (int i = 0; i < difficultyButtons.size(); i++) {
            final int diffButtonNumber = i + 1;

            difficultyButtons.get(i).setOnAction((e) -> {
                lastDiffClickedButton = diffButtonNumber;
                sudokuBoard.prepareBoard(lastDiffClickedButton);
                difficulty1.setVisible(false);
                difficulty2.setVisible(false);
                difficulty3.setVisible(false);
                saveButton.setVisible(true);
                textField.setVisible(true);
                gridPane1.setVisible(true);
                buttonsEvents();
                for (int j = 0; j < buttons.size(); j++) {
                    if (sudokuBoard.getFieldsArray().get(j).getFieldValue() == 0) {
                        buttons.get(j).setText("");
                    } else {
                        buttons.get(j).setText(Integer.toString(sudokuBoard.getFieldsArray().get(j).getFieldValue()));
//                        buttons.get(j).setDisable(true);
                    }
                }
                log.info("Zmieniasz trudność dla przycisku: {}", lastDiffClickedButton);
            });


        }
    }

    private void buttonsEvents() {
        for (int i = 0; i < buttons.size(); i++) {
            for (int j = 0; j < sudokuBoard.getEmptyFields().size(); j++) {
                final int buttonNumber = i;
                buttons.get(i).setText("");
                if (sudokuBoard.getEmptyFields().get(j) == (sudokuBoard.getFieldsArray().get(i))) {
                    buttons.get(i).setOnAction((e) -> {
                        lastClickedButton = buttonNumber;
                        log.info("Wcisales przycisk: {}", Integer.toString(lastClickedButton));
                    });
                    buttons.get(i).setOnMouseClicked((e) -> {
                        log.info("Wyswietliles panel wyboru przyciskow.");
                        valPanel.setVisible(true);
                    });
                }
            }
        }
    }

    private void getAllSaves() {
        try {
            List<Board> boards = DbManager.selectAllBoards();
            List<String> boardsNames = new ArrayList<>();
            for (Board b : boards) {
                boardsNames.add(b.getName());
            }
            ObservableList<String> boardsObservable = FXCollections.observableArrayList(boardsNames);
            loadChoiceBox.setItems(boardsObservable);
        } catch (Exception e) {

        }
    }

    private void saveSudoku() {
        if (!textField.getText().equals("")) {
            SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
            dao = factory.getDbDao(DbManager.getDatabaseUrl(), textField.getText());
            try {
                dao.write(sudokuBoard);
            } catch (SudokuWriteException w) {
                log.error("Błąd zapisu planszy: ", w.getCause());
            }
            log.info("Zapisano Sudoku");
        }
    }

}
