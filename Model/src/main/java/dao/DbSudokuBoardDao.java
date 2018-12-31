package dao;

import database.DbManager;
import exceptions.SudokuReadException;
import exceptions.SudokuWriteException;
import model.SudokuBoard;

import java.io.IOException;
import java.sql.SQLException;

public class DbSudokuBoardDao implements Dao<SudokuBoard> {

    private String databaseUrl;
    private String sudokuName;

    public DbSudokuBoardDao(final String databaseUrl, final String sudokuName) {
        this.databaseUrl = databaseUrl;
        this.sudokuName = sudokuName;
    }

    @Override
    public void write(final SudokuBoard sudokuBoard) throws SudokuWriteException {
        try {
            DbManager.createTables();
            DbManager.insertBoardAndFields(sudokuName, sudokuBoard);
        } catch (SQLException e) {
            throw new SudokuWriteException("Blad podczas zapisu sudoku", e);
        } catch (IOException b) {
            throw new SudokuWriteException("Blad podczas zamykania polaczenia z Db", b);
        }

    }

    @Override
    public SudokuBoard read() throws SudokuReadException {
        try {
            return DbManager.readSudokuBoard(sudokuName);
        } catch (SudokuReadException e) {
            throw new SudokuReadException("Wyjątek z metody read() DbDao", e);
        }
    }
}
