package cz.muni.fi.pb138.odsSearch.gui;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import cz.muni.fi.pb138.odsSearch.common.Spreadsheet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 * This class represents a table of results.
 * @author Vit Novotny <witiko@mail.muni.cz>
 */
public class ResultTableModel extends AbstractTableModel {
 
    public List<Cell> results = new ArrayList<>();
    private final ResourceBundle bundle = ResourceBundle.getBundle("cz/muni/fi/"
            + "pb138/odsSearch/gui/ResultTableModel");
    
    /**
     * This method swaps the results.
     * @param results the new set of results.
     */
    public void swapList(Set<Cell> results) {
        this.results = new ArrayList<>(results);
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return results.size();
    }
 
    @Override
    public int getColumnCount() {
        return 5;
    }
    
    /**
     * Returns the table cell represented by a row.
     * @param rowIndex the number for the row.
     * @return the table cell represented by a row.
     */
    public Cell getRow(int rowIndex) {
        return results.get(rowIndex);
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cell cell = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return cell.getSpreadsheet();
            case 1:
                return cell.getTable();
            case 2:
                return cell.getRowNumber();
            case 3:
                return cell.getColumnNumber();
            case 4:
                return cell.getContent();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return bundle.getString("spreadsheet");
            case 1:
                return bundle.getString("table");
            case 2:
                return bundle.getString("row");
            case 3:
                return bundle.getString("column");
            case 4:
                return bundle.getString("content");
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Spreadsheet.class;
            case 2:
            case 3:
                return Integer.class;
            case 1:
            case 4:
                return String.class;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
