package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a DAO for the forum threads.
 * 
 * @author Axel Wallin
 */
public class ThreadDAO implements AbstractDataAccessObject {
    
    private Database database;
    
    public ThreadDAO(Database database) {
        this.database = database;
    }
    
    @Override
    public ResultSet getAll() throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Thread;");
    }
    
    @Override
    public ResultSet getForUID(int uid) throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Thread WHERE id = '" + uid + "';");
    }
    
    /**
     * Gets all the threads related to a subject.
     * 
     * @param subjectId the subject of which's related threads we want.
     * @return a ResultSet instance containing all the threads related to the subject.
     * @throws Exception in case of emergency.
     */
    public ResultSet getForSubject(int subjectId) throws Exception {
        return database.getConnection()
                .createStatement()
                .executeQuery("SELECT * FROM Thread WHERE subjectId = '" + subjectId + "';");
    }
    
    /**
     * Gets the date for the latest post in a specified thread.
     * 
     * @param threadId the thread of which's last post date we want.
     * @return the date, as a String.
     * @throws SQLException in case of emergency.
     */
    public String getLatestPostDate(int threadId) throws SQLException {
        ResultSet rs = database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Message "
                + "WHERE threadId = '" + threadId + "' ORDER BY uid DESC LIMIT 1;");
        if(rs.next()) {
            return rs.getString("time");
        }
        return "this thread has no posts yet!";
    }

}
