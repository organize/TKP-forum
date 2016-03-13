package axel.tkp.forum;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.util.Constants;
import axel.tkp.forum.util.RequestBinder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
        port(Integer.valueOf(System.getenv("PORT")));
        /* Main database instance */
        Database database = new Database(Constants.POSTGRES_URL);
        //if(System.getenv("DATABASE_URL") != null) {
        //    database = new Database(System.getenv("DATABASE_URL"));
        //} else {
        //    database = new Database(Constants.DATABASE_NAME);
        //}
        
        if(Constants.createTables) {
            createPostgreTables(database);
        }
        
        /* Relative location to our misc. files */
        staticFileLocation("public");
        
        /* Bind the GETs and POSTs */
        RequestBinder.init(database);
        
        /* Initialize Spark */
        init();
    }
    
    private static void createPostgreTables(Database database) {
        try(Connection c = database.getConnection()) {
            Statement s = c.createStatement();
            for(String input : commands()) {
                s.executeUpdate(input);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("executed postgres");
    }
    
    private static List<String> commands() {
        List<String> result = new ArrayList<>();
        result.add("drop table subject");
        result.add("CREATE TABLE subject (id SERIAL PRIMARY KEY, name VARCHAR(30) NOT NULL);");
        result.add("CREATE TABLE thread(id SERIAL PRIMARY KEY, "
            + "title VARCHAR(30) NOT NULL, subjectId INTEGER, latestPost TIMESTAMP, FOREIGN KEY(subjectId) REFERENCES subject(id), "
            + "FOREIGN KEY(latestPost) REFERENCES message(time));");
        result.add("CREATE TABLE message(uid SERIAL PRIMARY KEY, "
            + "content VARCHAR(100) NOT NULL, sender VARCHAR(30) NOT NULL,"
            + " time TIMESTAMP, threadId INTEGER, FOREIGN KEY(threadId) REFERENCES Thread(id));");
        result.add("INSERT INTO subject(name) VALUES ('Programming');");
        result.add("INSERT INTO subject(name) VALUES ('Music');");
        result.add("INSERT INTO subject(name) VALUES ('Politics');");
        result.add("INSERT INTO subject(name) VALUES ('Sports');");
        return result;
    }

}
