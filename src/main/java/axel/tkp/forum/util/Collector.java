package axel.tkp.forum.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * A Collector will collect all the model instances from a ResultSet instance.
 * 
 * @param <T> the generic type of this collector.
 * @author Axel Wallin
 */
public interface Collector<T> {

    /**
     * Collects all data from a ResultSet.
     * 
     * @param rs the ResultSet supplied.
     * @return a list with the type that 
     *      is defined upon initialization of this class.
     * @throws SQLException in case of emergency.
     */
    List<T> collect(ResultSet rs) throws Exception;

}
