import model.MySecondSudokuSolver;
import model.SudokuBoard;
import model.SudokuSolver;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class SudokuBoardTest {

    @org.junit.Test
    public void fillBoard() {
        SudokuBoard testBoard = new SudokuBoard();

        boolean checkValue = false;
        if ( testBoard.get(0,0) == 0) {
            checkValue = true;
        }
        assertTrue(checkValue);

        assertTrue( testBoard.set(0,0, 3));
        assertTrue( testBoard.getBox(0, 0).getSudokuFieldsVector().get(0).getFieldValue() == 3);
        assertTrue( testBoard.set(0,0, 0));
        assertTrue( testBoard.getRow(0).getSudokuFieldsVector().get(0).getFieldValue() == 0);
        assertTrue( testBoard.getColumn(0).getSudokuFieldsVector().get(0).getFieldValue() == 0);
    }

    @org.junit.Test
    public void cloneBoard() {
        SudokuBoard testBoard = new SudokuBoard();
        SudokuSolver solver = new MySecondSudokuSolver();
        solver.solve(testBoard);

        try {
            SudokuBoard clonedBoard = (SudokuBoard) testBoard.clone();
            testBoard.set(0,0,0);

            assertFalse(clonedBoard.equals(testBoard));
        }  catch (CloneNotSupportedException e) {
            System.out.println("CloneNotSupportedException!");
        }
    }
}