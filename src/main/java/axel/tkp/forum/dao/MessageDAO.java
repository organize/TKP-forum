package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.ForumMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a DAO for the forum messages.
 * 
 * @author Axel Wallin
 */
public class MessageDAO implements AbstractDataAccessObject {
    
    private Database database;
    
    public MessageDAO(Database database) {
        this.database = database;
    }
    
    /**
     * Creates a new row in the database from the provided message.
     * 
     * @param message the message to save into the database.
     * @throws Exception in case of emergency.
     */
    public void create(ForumMessage message) throws Exception {
        /* Affirm that the message instance is valid */
        if(message.invalid()) {
            return;
        }
        
        /* Create a prepared statement to prevent injection */
        PreparedStatement statement = database
            .getConnection().prepareStatement(
                "INSERT INTO message (content, sender, time, threadId) VALUES (?, ?, now()::timestamptz(0), ?);");
        statement.setString(1, message.getContent());
        statement.setString(2, message.getSender());
        statement.setInt(3, message.getThreadId());
        
        /* Update the latest post on this post's thread */
        new ThreadDAO(database).updateLatestPost(message.getThreadId());
        
        /* Execute the update query */
        statement.executeUpdate();
    }

    @Override
    public ResultSet getAll() throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message");
    }

    @Override
    public ResultSet getForUID(int uid) throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message WHERE uid = '" + uid + "';");
    }
    
    /**
     * Gets all messages related to the specified thread.
     * 
     * @param threadId the thread of which's messages we want.
     * @return a ResultSet instance containing 
     *      all the messages in a thread.
     * @throws SQLException in case of emergency.
     */
    public ResultSet getForThread(int threadId) throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message WHERE threadId = '" + threadId + "';");
    }
    
    public ResultSet getForThreadAndLimit(int threadId, int pageId) throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM message "
                        + "WHERE threadId = '" + threadId + "' LIMIT 10 OFFSET " + ((pageId * 10) - 1) + ";");
    }

}
