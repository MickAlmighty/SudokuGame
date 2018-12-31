package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fields")
public class Field {

    public Field() {

    }

    @DatabaseField(columnName = "ID", generatedId = true)
    private int id;

    @DatabaseField(columnName = "BOARD_ID", foreign = true, canBeNull = false)
    private Board board;

    @DatabaseField(columnName = "INDEX", canBeNull = false)
    private int index;

    @DatabaseField(columnName = "VALUE", canBeNull = false)
    private int value;

    @DatabaseField(columnName = "CHANGEABLE", canBeNull = false)
    private boolean changeable;

    public boolean isChangeable() {
        return changeable;
    }

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(final Board board_id) {
        this.board = board_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
