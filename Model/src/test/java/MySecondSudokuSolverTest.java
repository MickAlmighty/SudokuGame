import model.MySecondSudokuSolver;
import model.SudokuBoard;


public class MySecondSudokuSolverTest {


    @org.junit.Test
    public void SolveTest() {

        SudokuBoard board1 = new SudokuBoard();
        MySecondSudokuSolver solver2 = new MySecondSudokuSolver();
        solver2.solve(board1);
        System.out.print(board1.toString());
    }
}