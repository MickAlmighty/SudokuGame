package model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SudokuContener implements Serializable{//,Cloneable{

    protected ArrayList<SudokuField> fields;

    SudokuContener() {
       fields = new ArrayList<SudokuField>();
    }

    public boolean addSudokuField(final SudokuField field) {

        if (fields.size() < 9) {
            fields.add(field);
        }
        if (fields.size() < 9) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verify() {
        int tmp;
        for (int i = 0; i < fields.size(); i++) {
            tmp = fields.get(i).getFieldValue();
            if (tmp != 0) {
                for (int j = 0; j < fields.size(); j++) {
                    if (i != j && tmp == fields.get(j).getFieldValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean verify(int val) {

        for (int i = 0; i < fields.size(); i++) {
            if (val == fields.get(i).getFieldValue()) {
                return false;
            }
        }
        return true;
    }

    public List<SudokuField> getSudokuFieldsVector() {
        return fields;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fields", fields)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuContener that = (SudokuContener) o;
        return Objects.equal(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }

//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
}
