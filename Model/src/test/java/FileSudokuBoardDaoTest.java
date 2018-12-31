import dao.Dao;
import dao.SudokuBoardDaoFactory;
import model.MySecondSudokuSolver;
import model.SudokuBoard;
import org.junit.Assert;
import org.junit.Test;

public class FileSudokuBoardDaoTest {

    @Test
    public void readAndWrite() {
        SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
        Dao<SudokuBoard> serializer = factory.getFileDao("sudoku");

        SudokuBoard board = new SudokuBoard();
        MySecondSudokuSolver solver = new MySecondSudokuSolver();
        solver.solve(board);
//        System.out.print("Sudoku serializowane:\n");
//        System.out.print(board.toString());
//        System.out.print("\n");
        try {
            serializer.write(board);
        } catch (Exception w){

        }

        SudokuBoard boardDeserialized = null;
        try {
            boardDeserialized = serializer.read();
        } catch (Exception r){

        }

//        System.out.print("\nSudoku deserializowane:\n");
//        System.out.print(boardDeserialized.toString());

        Assert.assertTrue(board.equals(boardDeserialized));

        //******************************//
        Dao<SudokuBoard> serializer2 = factory.getFileDao("sudoku2");
        SudokuBoard board2 = new SudokuBoard();
        try {
            serializer2.write(board2);
        } catch (Exception w){

        }
        SudokuBoard boardDeserialized2 = null;

        try {
            boardDeserialized2 = serializer2.read();
        } catch (Exception r){

        }
        Assert.assertTrue(board2.equals(boardDeserialized2));
        Assert.assertFalse(board.equals(boardDeserialized2));
    }
}