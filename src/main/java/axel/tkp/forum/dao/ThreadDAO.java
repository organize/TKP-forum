package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.ForumThread;
import java.sql.PreparedStatement;
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
    
    /**
     * Creates a new thread representation in the database table 'Thread'.
     * 
     * @param thread the thread to create.
     * @throws Exception in case of emergency.
     */
    public void create(ForumThread thread) throws Exception {
        /* Affirm that the thread instance is valid */
        if(thread.invalid()) {
            throw new Exception();
        }
        /* Create a prepared statement to prevent injection */
        PreparedStatement statement = database
            .getConnection().prepareStatement(
                    "INSERT INTO thread (title, subjectId, latestPost) "
                  + "VALUES (?, ?, now());");
        statement.setString(1, thread.getTitle());
        statement.setInt(2, thread.getSubjectId());
        
        /* Execute the prepared update query */
        statement.executeUpdate();
    }
    
    @Override
    public ResultSet getAll() throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM thread;");
    }
    
    @Override
    public ResultSet getForUID(int uid) throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM thread WHERE id = '" + uid + "';");
    }
    
    public ResultSet getRecentTen() throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM thread ORDER BY latestPost LIMIT 10;");
    }
    
    /**
     * Gets all the threads related to a subject.
     * 
     * @param subjectId the subject of which's related threads we want.
     * @return a ResultSet instance containing all the threads related to the subject.
     * @throws SQLException in case of emergency.
     */
    public ResultSet getForSubject(int subjectId) throws Exception {
        return database.getConnection()
                .createStatement()
                .executeQuery("SELECT * "
                        + "FROM thread "
                        + "WHERE subjectId = '" + subjectId + "' "
                        + "ORDER BY latestPost DESC LIMIT 10;");
    }
    
    /**
     * Gets the date for the latest post in a specified thread.
     * 
     * @param threadId the thread of which's last post date we want.
     * @return the date, as a String.
     * @throws SQLException in case of emergency.
     */
    public String getLatestPostDate(int threadId) throws Exception {
        ResultSet rs = database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message "
                + "WHERE threadId = '" + threadId + "' ORDER BY uid DESC LIMIT 1;");
        if(rs.next()) {
            return rs.getTimestamp("time").toString();
        }
        return "this thread has no posts yet!";
    }
    
    public int getPostCount(int threadId) throws Exception {
        int result = 0;
        ResultSet rs = database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message "
                + "WHERE threadId = '" + threadId + "';");
        while(rs.next()) {
            result++;
        }
        return result;
    }
    
    public void updateLatestPost(int id) throws Exception {
        database.getConnection()
                .createStatement()
                .executeUpdate("UPDATE thread "
                        + "SET latestPost = now() "
                        + "WHERE id = '" + id + "';");
    
    }

    public int count() throws Exception {
        ResultSet rs = database.getConnection()
                .createStatement()
                .executeQuery("SELECT count(id) AS post_count FROM thread;");
        int result = 0;
        if(rs.first()) {
            result = rs.getInt("post_count");
        }
        return result;
    }

}
