package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a DAO for the forum subjects.
 * 
 * @author Axel Wallin
 */
public class SubjectDAO implements AbstractDataAccessObject {
    
    private Database database;
    
    public SubjectDAO(Database database) {
        this.database = database;
    }
    
    @Override
    public ResultSet getAll() throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Subject");
    }

    @Override
    public ResultSet getForUID(int uid) throws SQLException {
        return database.getConnection().
                createStatement().executeQuery("SELECT * FROM Subject WHERE uid = '" + uid + "';");
    }

}
