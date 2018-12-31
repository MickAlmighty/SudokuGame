package model;

import com.google.common.base.Objects;

public class SudokuRow extends SudokuContener {

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fields", fields)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
