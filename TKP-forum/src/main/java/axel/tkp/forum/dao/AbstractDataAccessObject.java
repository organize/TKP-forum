package axel.tkp.forum.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents an abstract DAO.
 * 
 * @author Axel Wallin
 */
public interface AbstractDataAccessObject {

    /**
     * Get all rows related to this DAO from the Database.
     * 
     * @return a ResultSet instance containing all the data.
     * @throws SQLException in case of emergency.
     */
    public ResultSet getAll() throws Exception;
    
    /**
     * Get a specified row, defined by the uid parameter.
     * 
     * @param uid the unique id of the row.
     * @return a ResultSet instance containing the row.
     * @throws SQLException in case of emergency.
     */
    public ResultSet getForUID(int uid) throws Exception;
    
}
