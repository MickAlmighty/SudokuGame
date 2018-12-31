package dao;

import exceptions.SudokuReadException;
import exceptions.SudokuWriteException;
import model.SudokuBoard;

import java.io.*;
public class FileSudokuBoardDao implements Dao<SudokuBoard> {
    private String fileName;
    public FileSudokuBoardDao(final String fileName) {
        this.fileName = fileName;
    }
    public void write(final SudokuBoard board) throws SudokuWriteException {

        try {
            File file = new File(fileName + ".bin");
            if (!file.exists()) {
                file.createNewFile();
            }
        }   catch (IOException e) {
            throw new SudokuWriteException("Blad podczas tworzenia pliku.", e);
//            StackTraceElement[] elements = e.getStackTrace();
//            for (int i = 0, n = elements.length; i < n; i++) {
//                loger.warn(elements[i].getMethodName());
//            }
        }
        //try-with-resources
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName + ".bin"))) {
            outputStream.writeObject(board);
        } catch (FileNotFoundException notFound) {
            throw new SudokuWriteException("Nie znaleziono pliku.", notFound);
        } catch (IOException f) {
            throw new SudokuWriteException("Blad podczas serializacji planszy sudoku.", f);
        }
    }

    public SudokuBoard read() throws SudokuReadException {

        SudokuBoard board = null;
        //try-with-resources
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName + ".bin"))) {
            board = (SudokuBoard) inputStream.readObject();
        } catch (ClassNotFoundException klasa) {
            throw new SudokuReadException("Nie znaleziono klasy.", klasa);
        } catch (FileNotFoundException notFound) {
            throw new SudokuReadException("Nie znaleziono pliku.", notFound);
        } catch (IOException d) {
            throw new SudokuReadException("Blad podczas deserializacji", d);
        }
        //deleteFileIfPossible();
        return board;
    }

    public boolean deleteFileIfPossible() {
        File czyIstnieje = new File(fileName + ".bin");
        if (czyIstnieje.exists()) {
            czyIstnieje.delete();
            return true;
        } else {
            return false;
        }
    }
}
