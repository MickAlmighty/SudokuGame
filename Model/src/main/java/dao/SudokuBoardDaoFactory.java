package dao;

import model.SudokuBoard;

public class SudokuBoardDaoFactory {
    public Dao<SudokuBoard> getFileDao(final String fileName) {
        return new FileSudokuBoardDao(fileName);
    }

    public DbSudokuBoardDao getDbDao(final String dbUrl, final String sudokuName) {
        return new DbSudokuBoardDao(dbUrl, sudokuName);
    }
}
