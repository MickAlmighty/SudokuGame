package dao;

import database.Board;
import database.DbManager;
import model.MySecondSudokuSolver;
import model.SudokuBoard;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DbSudokuBoardDaoTest {

    @Test
    public void write() {
        String databaseUrl = "jdbc:sqlite:bazadanych";

        SudokuBoard sudokuBoard = new SudokuBoard();
        MySecondSudokuSolver solver = new MySecondSudokuSolver();
        solver.solve(sudokuBoard);
        sudokuBoard.prepareBoard(3);
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        DbSudokuBoardDao dao = factory.getDbDao(databaseUrl, "NoweSudoku");

        try {
            dao.write(sudokuBoard);
        } catch (Exception e) {
            System.out.println("Cos sie stało");
        }
        System.out.println(sudokuBoard.toString());
        SudokuBoard board = new SudokuBoard();
        try {
            board = dao.read();
        } catch (Exception e) {

        }
        System.out.println(board.toString());
        Assert.assertEquals(sudokuBoard, board);

//        DbSudokuBoardDao dao2 = factory.getDbDao(databaseUrl, "NoweSudoku1");
//        SudokuBoard board2 = new SudokuBoard();
//        try {
//            board2 = dao2.read();
//        } catch (Exception e) {
//            System.out.println("Nie ma sudoku z taką nazwą");
//        }
    }

    @Test
    public void read() {
        try {
            List<Board> tmp = DbManager.selectAllBoards();
            System.out.println(tmp.get(0).getName());
        } catch (Exception e) {
            System.out.println("Wystapil blad podczas wykonywania kwerendy");
        }

    }
}