package cz.muni.fi.pb138.odsSearch.gui;

import cz.muni.fi.pb138.odsSearch.common.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractListModel;

/**
 * This is the model of the list of results used in the MainFrame class.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public final class ResultListModel extends AbstractListModel<String> {
    
    public List<Cell> results = new ArrayList<>();
    
    /**
     * This method swaps the results.
     * @param results the new set of results.
     */
    public void swapList(Set<Cell> results) {
        this.results = new ArrayList(results);
        this.fireContentsChanged(this, 0, results.size());
    }

    @Override
    public int getSize() {
        return results.size();
    }

    @Override
    public String getElementAt(int index) {
        return results.get(index).toString();
    }
    
}
