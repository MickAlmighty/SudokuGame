import model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import static org.junit.Assert.*;
public class SudokuContenerTest {
    @org.junit.Test
    public void SudokuContenersTest(){

        SudokuContener pojemnik = new SudokuRow();
        Vector<SudokuField> fieldVector = new Vector<SudokuField>();

        for (int i = 0; i < 81; i++) {
            fieldVector.add(new SudokuField());
        }

        boolean flag = true;
        int i = 0;
        while (flag) {
            flag = pojemnik.addSudokuField(fieldVector.elementAt(i));
            i++;
        }

        assertEquals(9, pojemnik.getSudokuFieldsVector().size());
        assertTrue(pojemnik.verify());  //same zera, sprawdzenie czy pola nie mają tej samej wartośći != 0

        pojemnik.getSudokuFieldsVector().get(0).setFieldValue(1);
        assertTrue(pojemnik.verify()); //sprawdzenie czy 1 się powtarza

        pojemnik.getSudokuFieldsVector().get(1).setFieldValue(1);
        assertFalse(pojemnik.verify(1)); //w wektorze są pola o takiej samej wartosci
        if (pojemnik.verify() == false ) {
            pojemnik.getSudokuFieldsVector().get(1).setFieldValue(0);
        }
        assertTrue(pojemnik.verify());  //po ustawieniu pola 1 na 0 f. zwraca true

        System.out.print(pojemnik.toString());
    }

    @Test
    public void ContenersTest() {
        List<SudokuField> fields = new ArrayList<SudokuField>();
        List<SudokuRow> rows = new ArrayList<SudokuRow>();
        List<SudokuColumn> columns = new ArrayList<SudokuColumn>();
        List<SudokuBox> boxes = new ArrayList<SudokuBox>();

        //fields.add(pole);
        for (int i = 0; i < 81; i++) {
            fields.add(new SudokuField());
        }

        for(int i = 0; i < 9; i++) {
            rows.add(new SudokuRow());
            columns.add(new SudokuColumn());
            boxes.add(new SudokuBox());
        }
        assertEquals(9, rows.size());
        assertEquals(9, columns.size());
        assertEquals(9, boxes.size());

        for(int i = 0; i < 81; i++) {
            rows.get(i / 9).addSudokuField(fields.get(i));
            columns.get(i % 9).addSudokuField(fields.get(i));
            boxes.get(i / 27 * 3 + i % 9 / 3).addSudokuField(fields.get(i));
        }

        for(int i = 0; i< 9; i++){
            assertEquals(9,rows.get(i).getSudokuFieldsVector().size());
            assertEquals(9,columns.get(i).getSudokuFieldsVector().size());
            assertEquals(9,boxes.get(i).getSudokuFieldsVector().size());
        }

        rows.get(3).getSudokuFieldsVector().get(1).setFieldValue(5);
        //sprawdzenie czy wstawiona wartość w pola rzędu 4 została zmieniona dla pola w obiekcie kolumny i boxu.
        assertEquals(columns.get(1).getSudokuFieldsVector().get(3).getFieldValue(),
                boxes.get(3).getSudokuFieldsVector().get(1).getFieldValue());
    }
}