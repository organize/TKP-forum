package axel.tkp.forum.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * The main database representation.
 * 
 * @author <?>
 */
public class Database {
    
    private Connection connection;
    
    public Database(String name) throws Exception {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + name);
    }
    
    public void update(String sql) throws Exception {
        connection.setAutoCommit(false);
        try(Statement s = connection.createStatement()) {
            s.executeUpdate(sql);
        }
        connection.commit();
    }
    
    public Connection getConnection() {
        return connection;
    }

}
