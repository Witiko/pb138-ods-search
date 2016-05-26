package cz.muni.fi.pb138.odsSearch.common;

import java.util.Set;

/**
 * This interface represents an object that can be queried for fixed strings
 * (and perhaps for other things, such as regular expressions, if time permits).
 * @author Vít Novotný <witiko@gmail.com>
 * @param <Result> The type of the query results.
 */
public interface Queriable<Result> {
    
    /**
     * Returns a set of results that correspond to a fixed string query.
     * @param query a fixed string.
     * @param caseSensitive whether the query should be case-sensitive.
     * @param exactMatch whether the query should match only those strings
     * that match exactly the query. or whether substrings should be matched
     * as well.
     * @return a set of results that correspond to a fixed string query.
     */
    public Set<Result> queryFixedString(String query,
            boolean caseSensitive, boolean exactMatch);

}
