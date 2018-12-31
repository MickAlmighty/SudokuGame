package database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.MySecondSudokuSolver;
import model.SudokuBoard;
import model.SudokuField;

public class DbMain {
    public static void main(final String[] args) throws Exception {
    // this uses SQLite by default but change to match your database
        String databaseUrl = "jdbc:sqlite:bazadanych";
        // create a connection source to our database
            ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

            Dao<Board, Integer> boardDao = DaoManager.createDao(connectionSource, Board.class);
            Dao<Field, Integer> fieldDao = DaoManager.createDao(connectionSource, Field.class);
            //TableUtils.dropTable(boardDao, true);
            //TableUtils.dropTable(fieldDao, true);
            TableUtils.createTableIfNotExists(connectionSource, Board.class);
            TableUtils.createTableIfNotExists(connectionSource, Field.class);


            SudokuBoard sudokuBoard = new SudokuBoard();
            MySecondSudokuSolver solver = new MySecondSudokuSolver();
            solver.solve(sudokuBoard);
            sudokuBoard.prepareBoard(3);

            Board board = new Board();
            board.setName("Sudoku");
            boardDao.createOrUpdate(board);

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

        SudokuBoard sudokuBoard1 = new SudokuBoard();
        solver = new MySecondSudokuSolver();
        solver.solve(sudokuBoard1);
        sudokuBoard.prepareBoard(3);

        Board board1 = new Board();
        board1.setName("Sudoku1");
        boardDao.createOrUpdate(board1);

        for (int i = 0; i < sudokuBoard.getFieldsArray().size(); i++) {
            boolean flag = false;
            for (SudokuField changeable: sudokuBoard.getEmptyFields()) {
                if (changeable == sudokuBoard.getFieldsArray().get(i)) {
                    flag = true;
                }
            }
            Field field = new Field();
            field.setBoard(board1);
            field.setIndex(i);
            field.setValue(sudokuBoard.getFieldsArray().get(i).getFieldValue());
            field.setChangeable(flag);
            fieldDao.createOrUpdate(field);
        }
            QueryBuilder<Field, Integer> queryBuilder = fieldDao.queryBuilder();
            QueryBuilder<Board, Integer> queryBuilder2 = boardDao.queryBuilder();

            queryBuilder2.where().eq("NAME", board.getName());




            PreparedQuery<Board> prepared2 = queryBuilder2.prepare();
//            List<Board> result2 = boardDao.query(prepared2);
//            System.out.println(result2.toString());
//
//            queryBuilder.where().eq("BOARD_ID", result2.get(0));
//            PreparedQuery<Field> prepared = queryBuilder.prepare();
//            List<Field> result = fieldDao.query(prepared);
//            List<SudokuField> sudokuFields = new ArrayList<>();
//            for (int i = 0; i < 81; i++) {
//                sudokuFields.add(new SudokuField());
//            }
//
//            result.forEach(e -> {
//                System.out.println(e.getIndex() + " : " + e.getValue());
//            });

            connectionSource.close();
    }
}
