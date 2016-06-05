package cz.muni.fi.pb138.odsSearch.gui;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * This class represents a renderer of a column within a table of results.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class ColumnNumberCellRenderer extends DefaultTableCellRenderer {
    @Override
    public void setValue(Object value) {
        if (value != null)
            value = value.toString(); 
        super.setValue(value);
    }
}
