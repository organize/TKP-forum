package axel.tkp.forum;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.util.Constants;
import axel.tkp.forum.util.RequestBinder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import spark.Spark;
import static spark.Spark.*;

/**
 * The main TKP-Forum class.
 * 
 * @author Axel Wallin
 */
public class Forum {
    
    /**
     * Program entry point.
     * 
     * @param argv no arguments supported currently.
     * @throws Exception in case of emergency.
     */
    public static void main(String[] argv) throws Exception {
        /* Main database instance */
        port(5000);
        
        Database database = new Database(System.getenv("DATABASE_URL"));
        if(System.getenv("DATABASE_URL") != null) {
            database = new Database(System.getenv("DATABASE_URL"));
        } else {
            database = new Database(Constants.DATABASE_NAME);
        }
        
        if(Constants.createTables) {
            createPostgreTables(database.getConnection());
        }
        
        /* Relative location to our misc. files */
        staticFileLocation("public");
        
        /* Bind the GETs and POSTs */
        RequestBinder.init(database);
        
        /* Initialize Spark */
        init();
    }
    
    private static void createPostgreTables(Connection connection) {
        try(Statement s = connection.createStatement()) {
            s.executeUpdate("CREATE TABLE Subject(id SERIAL PRIMARY KEY, name VARCHAR(30) NOT NULL);");
            s.executeUpdate("CREATE TABLE Thread(id SERIAL PRIMARY KEY, "
                    + "title VARCHAR(30) NOT NULL, subjectId INTEGER, latestPost DATE, FOREIGN KEY(subjectId) REFERENCES Subject(id), "
                    + "FOREIGN KEY latestPost REFERENCES Message(time));");
            s.executeUpdate("CREATE TABLE Message(uid SERIAL PRIMARY KEY, "
                    + "content VARCHAR(100) NOT NULL, sender VARCHAR(30) NOT NULL,"
                    + " time DATE, threadId INTEGER, FOREIGN KEY(threadId) REFERENCES Thread(id));");
            s.executeUpdate("INSERT INTO Subject(name) VALUES ('Programming');");
            s.executeUpdate("INSERT INTO Subject(name) VALUES ('Music');");
            s.executeUpdate("INSERT INTO Subject(name) VALUES ('Politics');");
            s.executeUpdate("INSERT INTO Subject(name) VALUES ('Sports');");
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("executed postgres");
    }

}
