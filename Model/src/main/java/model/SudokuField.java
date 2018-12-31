package model;

import com.google.common.base.Objects;

import java.io.Serializable;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    private int value;
    public SudokuField() {
        value = 0;
    }
    public int getFieldValue() {
        return value;
    }
    public void setFieldValue(int val) {
        value = val;
    }

    @Override
    public String toString() {
        return Integer.toString(this.getFieldValue());
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//                .append("value", value)
//                .toString();
//    }

    /*@Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        if (this.getFieldValue() == ((SudokuField) obj).getFieldValue()) {
            return true;
        } else {
            return false;
        }
    }*/


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuField that = (SudokuField) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public SudokuField clone() throws CloneNotSupportedException {
        SudokuField tmp = new SudokuField();
        tmp.setFieldValue(this.getFieldValue());
        return tmp;
    }

    @Override
    public int compareTo(final SudokuField o) {
        return this.getFieldValue() < o.getFieldValue() ? 1 : this.getFieldValue() == o.getFieldValue() ? 0 : -1;
        //return this.getFieldValue() == o.getFieldValue() ? 1: this.getFieldValue() != o.getFieldValue() ? 0: -1;
    }
}
