package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.ForumMessage;
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
        StringBuilder bldr = new StringBuilder();
        bldr.append("INSERT INTO Message ");
        bldr.append("(content, sender, time, threadId) VALUES (");
        bldr.append(process(message.getContent())).append(", ");
        bldr.append(process(message.getSender())).append(", ");
        bldr.append("date('now'), ");
        bldr.append(process("" + message.getThreadId()));
        bldr.append(" );");
        database.update(bldr.toString());
    }
    
    /**
     * Helps process parameters when inserting data into a table.
     * 
     * @param s the string to process.
     * @return a processed version of the input.
     */
    private String process(String s) {
        return "'" + s + "'";
    }

    @Override
    public ResultSet getAll() throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Message");
    }

    @Override
    public ResultSet getForUID(int uid) throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Message WHERE uid = '" + uid + "';");
    }
    
    /**
     * Gets all messages related to the specified thread.
     * 
     * @param threadId the thread of which's messages we want.
     * @return a ResultSet instance containing 
     *      all the messages in a thread.
     * @throws SQLException in case of emergency.
     */
    public ResultSet getForThread(int threadId) throws SQLException {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM Message WHERE threadId = '" + threadId + "';");
    }

}
