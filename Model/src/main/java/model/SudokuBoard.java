package model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuBoard implements Serializable, Cloneable {
    private List<SudokuField> fields;
    private List<SudokuRow> rows;
    private List<SudokuColumn> columns;
    private List<SudokuBox> boxes;
    private List<SudokuField> emptyFields;

    public SudokuBoard(final List<SudokuField> fields1, final List<SudokuRow> rows1, final List<SudokuColumn> columns1, final List<SudokuBox> boxes1, final List<SudokuField> emptyFields1) {
        fields = fields1;
        rows = rows1;
        columns = columns1;
        boxes = boxes1;
        emptyFields = emptyFields1;
    }

    public SudokuBoard() {
        fields = new ArrayList<SudokuField>();
        rows = new ArrayList<SudokuRow>();
        columns = new ArrayList<SudokuColumn>();
        boxes = new ArrayList<SudokuBox>();
        emptyFields = new ArrayList<SudokuField>();

        for (int i = 0; i < 81; i++) {
            fields.add(new SudokuField());
            emptyFields.add(fields.get(i)); //!!!!!!!!
        }

        for (int i = 0; i < 9; i++) {
            rows.add(new SudokuRow());
            columns.add(new SudokuColumn());
            boxes.add(new SudokuBox());
        }

        for (int i = 0; i < 81; i++) {
            rows.get(i / 9).addSudokuField(fields.get(i));
            columns.get(i % 9).addSudokuField(fields.get(i));
            boxes.get(i / 27 * 3 + i % 9 / 3).addSudokuField(fields.get(i));
        }
        //fields = Collections.unmodifiableList(fields);
        rows = Collections.unmodifiableList(rows);
        columns = Collections.unmodifiableList(columns);
        boxes = Collections.unmodifiableList(boxes);
    }

    public SudokuBoard(final ArrayList<SudokuField> newFields, final ArrayList<SudokuField> changeableFields) {
        fields = newFields;
        rows = new ArrayList<SudokuRow>();
        columns = new ArrayList<SudokuColumn>();
        boxes = new ArrayList<SudokuBox>();
        emptyFields = changeableFields;

        for (int i = 0; i < 9; i++) {
            rows.add(new SudokuRow());
            columns.add(new SudokuColumn());
            boxes.add(new SudokuBox());
        }

        for (int i = 0; i < 81; i++) {
            rows.get(i / 9).addSudokuField(fields.get(i));
            columns.get(i % 9).addSudokuField(fields.get(i));
            boxes.get(i / 27 * 3 + i % 9 / 3).addSudokuField(fields.get(i));
        }
        //fields = Collections.unmodifiableList(fields);
        rows = Collections.unmodifiableList(rows);
        columns = Collections.unmodifiableList(columns);
        boxes = Collections.unmodifiableList(boxes);
    }

    private boolean checkInsertionPossibility(int val, int x, int y) {
        if (!rows.get((x * 3 + y) / 9).verify(val)) {
            return false;
        } else if (!columns.get((x * 3 + y) % 9).verify(val)) {
            return false;
        } else if (!boxes.get((x * 3 + y) / 27 * 3 + (x * 3 + y) % 9 / 3).verify(val)) {
            return false;
        }   else {
            return true;
        }// jesli w wierszu, kolumnie i bloku 3x3 nie ma jeszcze takiej wartości metoda zwroci true;
    }

    public boolean set(int x, int y, int val) {
        boolean isChangeable = false;
        for (SudokuField f : emptyFields) {
            if (f == fields.get(x)) {
                isChangeable = true;
            }
        }
        if (isChangeable) {
            if (val == 0) {
                fields.get(x * 9 + y).setFieldValue(val);
                return true;
            } else if (checkInsertionPossibility(val, x, y)) {
                fields.get(x * 9 + y).setFieldValue(val);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean set(int x, int val) {
        boolean isChangeable = false;
        for (SudokuField f : emptyFields) {
            if (f == fields.get(x)) {
                isChangeable = true;
            }
        }
        if (isChangeable) {
            if (val == 0) {
                fields.get(x).setFieldValue(val);
                return true;
            } else if (checkInsertionPossibility2(val, x / 9, x % 9)) {
                fields.get(x).setFieldValue(val);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkInsertionPossibility2(int val, int x, int y) {
        if (!rows.get(x).verify(val)) {
            return false;
        } else if (!columns.get(y).verify(val)) {
            return false;
        } else if (!boxes.get((x * 9 + y) / 27 * 3 + (x * 9 + y) % 9 / 3).verify(val)) {
            return false;
        }   else {
            return true;
        }// jesli w wierszu, kolumnie i bloku 3x3 nie ma jeszcze takiej wartości metoda zwroci true;
    }

    public void prepareBoard(int difficulty) {
        int valuesToDelete = 0;
        if (difficulty == 1) { //easy
            valuesToDelete = 30;
        } else if (difficulty == 2) {
            valuesToDelete = 40;
        } else if (difficulty == 3) {
            valuesToDelete = 50;
        }
        Random rand = new Random();
        int tmp = 0;
        while (valuesToDelete != amIWinner()) {
            tmp = rand.nextInt((80 - 1) + 1);
            fields.get(tmp).setFieldValue(0);
        }
        setValidFields();
    }

    private void setValidFields() {
        emptyFields = new ArrayList<SudokuField>();
        for (int i = 0; i < 81; i++) {
            if (fields.get(i).getFieldValue() == 0) {
                emptyFields.add(fields.get(i));
            }
        }
    }

    public int amIWinner() {
        int counter = 0;
        for (SudokuField field : fields) {
            if (field.getFieldValue() == 0) {
                counter++;
            }
        }
        return counter;
    }

    public int get(int x, int y) {
       return fields.get(x * 3 + y).getFieldValue();
    }

    public SudokuRow getRow(int x) {
        return rows.get(x);
    }

    public SudokuColumn getColumn(int x) {
        return columns.get(x);
    }

    public SudokuBox getBox(int x, int y) {
        int tmp = (x * 3) + y;
        return boxes.get(tmp);
    }

    public List<SudokuField> getFieldsArray() {
        return fields;
    }

    public List<SudokuRow> getRowsArray() {
        return rows;
    }
    public List<SudokuColumn> getColumnsArray() {
        return columns;
    }

    public List<SudokuBox> getBoxesArray() {
        return boxes;
    }
    public List<SudokuField> getEmptyFields() {
        return emptyFields;
    }
    /*@Override
    public String toString() {
        String tmp = new String();
        for (int i = 0; i < rows.size(); i++) {
            if (i == 0) {
                tmp += " ***********************\n";
            } else if (i == 3 || i == 6) {
                tmp += " ***********************\n";
            }
            tmp += rows.get(i).toString();
            if (i == 8) {
                tmp += " ***********************\n";
            }
        }
        tmp += "\n";
        return tmp;
    }*/

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("\r\nfields", fields)
                .add("\r\nrows", rows)
                .add("\r\ncolumns", columns)
                .add("\r\nboxes", boxes)
                .add("\r\nemptyFields", emptyFields)
                .toString();
    }
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//                .append("\r\nfields", fields)
//                .append("\r\nrows", rows)
//                .append("\r\ncolumns", columns)
//                .append("\r\nboxes", boxes)
//                .append("\r\nemptyFields", emptyFields)
//                //.append("\r\n")
//                .toString();
//    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuBoard that = (SudokuBoard) o;
        return Objects.equal(fields, that.fields) &&
                Objects.equal(rows, that.rows) &&
                Objects.equal(columns, that.columns) &&
                Objects.equal(boxes, that.boxes) &&
                Objects.equal(emptyFields, that.emptyFields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields, rows, columns, boxes, emptyFields);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        List<SudokuField> fields1 = new ArrayList<SudokuField>();
        List<SudokuRow> rows1 = new ArrayList<SudokuRow>();
        List<SudokuColumn> columns1 = new ArrayList<SudokuColumn>();
        List<SudokuBox> boxes1 = new ArrayList<SudokuBox>();
        List<SudokuField> emptyFields1 = new ArrayList<SudokuField>();

        for (int i = 0; i < 81; i++) {

            fields1.add(this.getFieldsArray().get(i).clone());
            emptyFields1.add(fields1.get(i)); //!!!!!!!!
        }

        for (int i = 0; i < 9; i++) {
            rows1.add(new SudokuRow());
            columns1.add(new SudokuColumn());
            boxes1.add(new SudokuBox());
        }

        for (int i = 0; i < 81; i++) {
            rows1.get(i / 9).addSudokuField(fields1.get(i));
            columns1.get(i % 9).addSudokuField(fields1.get(i));
            boxes1.get(i / 27 * 3 + i % 9 / 3).addSudokuField(fields1.get(i));
        }
        SudokuBoard tmp = new SudokuBoard(fields1, rows1, columns1, boxes1, emptyFields1);
        return tmp;
    }
}
