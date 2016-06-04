package cz.muni.fi.pb138.odsSearch.gui;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import javax.swing.table.AbstractTableModel;

/**
 * This class represents a table that represents the 3x3 context of a cell.
 * @author Vit Novotny <witiko@mail.muni.cz>
 */
public class ContextTableModel extends AbstractTableModel {
 
    public Cell focus;
    
    /**
     * This method makes the model switch focus to a different cell.
     * @param focus the new cell to focus on.
     */
    public void switchFocus(Cell focus) {
        if (this.focus == null || !this.focus.equals(focus)) {
            this.focus = focus;
            this.fireTableDataChanged();
        }
    }
    
    @Override
    public int getRowCount() {
        return 3;
    }
 
    @Override
    public int getColumnCount() {
        return 3;
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (focus == null)
            return null;
        if (rowIndex == 1 && columnIndex == 1)
            return focus.getContent();
        Cell cell = focus.getSpreadsheet().getCell(focus.getTable(),
                focus.getRowNumber() + rowIndex - 1,
                focus.getColumnNumber() + columnIndex - 1);
        return cell != null ? cell.getContent() : null;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
