package database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import exceptions.SudokuReadException;
import model.SudokuBoard;
import model.SudokuField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private static String databaseUrl = "jdbc:sqlite:bazadanych";

    public static void setDbUrl(final String url) {
        databaseUrl = url;
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static void createTables() throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

        TableUtils.createTableIfNotExists(connectionSource, Board.class);
        TableUtils.createTableIfNotExists(connectionSource, Field.class);

        try {
            connectionSource.close();
        } catch (IOException e) {
            e.getCause();
        }
    }

    public static void insertBoardAndFields(final String sudokuName, final SudokuBoard sudokuBoard) throws SQLException, IOException {
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
        Dao<Board, String> boardDao = DaoManager.createDao(connectionSource, Board.class);

        Board board = new Board();
        board.setName(sudokuName);
        boardDao.createOrUpdate(board);

        Dao<Field, Integer> fieldDao = DaoManager.createDao(connectionSource, Field.class);
        if (fieldDao.query(fieldDao.queryBuilder().where().eq("BOARD_ID", sudokuName).prepare()) != null) {
            DeleteBuilder<Field, Integer> deleteBuilder = fieldDao.deleteBuilder();
            deleteBuilder.where().eq("BOARD_ID", sudokuName);
            deleteBuilder.delete();
        }

        for (int i = 0; i < sudokuBoard.getFieldsArray().size(); i++) {
            boolean flag = false;
            for (SudokuField changeable: sudokuBoard.getEmptyFields()) {
                if (changeable == sudokuBoard.getFieldsArray().get(i)) {
                    flag = true;
                }
            }
            Field field = new Field();
            field.setBoard(board);
            field.setIndex(i);
            field.setValue(sudokuBoard.getFieldsArray().get(i).getFieldValue());
            field.setChangeable(flag);
            fieldDao.createOrUpdate(field);
        }
        connectionSource.close();
    }

    public static SudokuBoard readSudokuBoard(final String sudokuName) throws SudokuReadException {
        ConnectionSource connectionSource = null;
        Dao<Field, Integer> fieldDao = null;
        List<Field> result = null;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            fieldDao = DaoManager.createDao(connectionSource, Field.class);
        } catch (SQLException e) {
            throw new SudokuReadException("Bledny url do bazy danych.", e);
        }

        try {
            result = fieldDao.query(fieldDao.queryBuilder().
                    where().
                    eq("BOARD_ID", sudokuName).
                    prepare());
        } catch (SQLException e) {
            throw new SudokuReadException("Blad podczas wykonywania kwerendy", e);
        }

        ArrayList<SudokuField> sudokuFields = new ArrayList<>();
        ArrayList<SudokuField> changeableFields = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            sudokuFields.add(new SudokuField());
        }

        for (int i = 0; i < 81; i++) {
            sudokuFields.get(i).setFieldValue(result.get(i).getValue());
            if (result.get(i).isChangeable()) {
                changeableFields.add(sudokuFields.get(i));
            }
        }
        SudokuBoard boardFromDb = new SudokuBoard(sudokuFields, changeableFields);

        try {
            connectionSource.close();
        } catch (IOException e) {
            throw new SudokuReadException("Blad podczas zamykania polaczenia z Db", e);
        }

        return boardFromDb;
    }

    public static List<Board> selectAllBoards() throws SQLException, IOException {
        List<Board> tmp = null;
        ConnectionSource connectionSource = null;
        try {
            createTables();
            connectionSource = new JdbcConnectionSource(databaseUrl);
            Dao<Board, String> boardDao = DaoManager.createDao(connectionSource, Board.class);

            tmp = boardDao.query(boardDao.queryBuilder().prepare());
        } catch (SQLException e) {
            throw new SQLException();
        }
        try {
            connectionSource.close();
        } catch (IOException e) {
            throw new IOException();
        }
        return tmp;
    }
}
