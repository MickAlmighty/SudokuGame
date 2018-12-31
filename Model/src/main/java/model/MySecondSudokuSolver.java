package model;

import java.util.*;

public class MySecondSudokuSolver implements SudokuSolver {

    private List<SudokuField> fieldArray; // = new ArrayList<SudokuField>();
    private List<SudokuRow> rowArray; // = new ArrayList<SudokuRow>();
    private List<SudokuColumn> columnArray; // = new ArrayList<SudokuColumn>();
    private List<SudokuBox> boxArray; // = new ArrayList<SudokuBox>();

    public void solve(final SudokuBoard board) {

        fieldArray = board.getFieldsArray();
        rowArray = board.getRowsArray();
        columnArray = board.getColumnsArray();
        boxArray = board.getBoxesArray();

        fieldArray = Collections.unmodifiableList(fieldArray);
        rowArray = Collections.unmodifiableList(rowArray);
        columnArray = Collections.unmodifiableList(columnArray);
        boxArray = Collections.unmodifiableList(boxArray);
        fillBoard();
    }

    private void generateBoard() {
        Random rand = new Random();
        int randomNum;
        for (int i = 0; i < 81; i++) {
            randomNum = rand.nextInt((9 - 1) + 1) + 1;  //wartosc <1,9>
            if (checkInsertionPossibility(randomNum, i)) {
                fieldArray.get(i).setFieldValue(randomNum);
            }
        }
    }

    private boolean checkInsertionPossibility(int val, int i) {
        if (!rowArray.get(i / 9).verify(val)) {
            return false;
        } else if (!columnArray.get(i % 9).verify(val)) {
            return false;
        } else if (!boxArray.get(i / 27 * 3 + i % 9 / 3).verify(val)) {
            return false;
        }   else {
            return true;
        }// jesli w wierszu, kolumnie i bloku 3x3 nie ma jeszcze takiej wartości metoda zwroci true;
    }

    private boolean next(int i) {
        if (i == 80) {
            return true;
        } else {
            return checkPlace(i + 1);
        }
    }

    private boolean checkPlace(int i) {
        if (fieldArray.get(i).getFieldValue() == 0) {
            List<Integer> validValues = new Vector<Integer>();
            for (int val = 1; val <= 9; val++) {  //jesli dla podanej komórki petla nie wstawi wartosci wtedy metoda zwraca false
                if (checkInsertionPossibility(val, i)) {
                    validValues.add(val);
                }
            }
            for (int place = 0; place < validValues.size(); place++) {
                fieldArray.get(i).setFieldValue(validValues.get(place));
                if (next(i)) {   // jesli metoda next zwroci true wtedy poprawnie wypelniono tablice
                    return true;
                } else {
                    fieldArray.get(i).setFieldValue(0);
                }
            }
            return false;
        }
        return next(i);
    }

    private void fillBoard() {
        boolean flag = false;
        while (!flag) {
            generateBoard();
            if (checkPlace(0)) {
                flag = true;
            } else {
                flag = false;
                for (int i = 0; i < 81; i++) {   //zerowanie tablicy po nieudanej probie wypelnienia
                    fieldArray.get(i).setFieldValue(0);
                }
            }
        }
    }
}
