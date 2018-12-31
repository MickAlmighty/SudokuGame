package dao;

import exceptions.SudokuReadException;
import exceptions.SudokuWriteException;

public abstract interface Dao<T> {
    public T read() throws SudokuReadException;
    public void write(T obj) throws SudokuWriteException;
}
