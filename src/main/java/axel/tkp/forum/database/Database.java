package axel.tkp.forum.database;

import java.net.URI;
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
    private String address;
    
    public Database(String address) throws Exception {
        Class.forName("org.postgresql.Driver");
        this.address = address;
    }
    
    public Connection getConnection() throws Exception {
        if(connection != null) {
            return connection;
        }
        URI dbUri = new URI(address);
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        return connection = DriverManager.getConnection(dbUrl, username, password);
    }

}
