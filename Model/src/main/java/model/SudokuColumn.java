package model;

import com.google.common.base.Objects;

public class SudokuColumn extends SudokuContener {

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fields", fields)
                .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
