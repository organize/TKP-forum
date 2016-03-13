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
        
        /* Bind to the local port */
        port(Integer.valueOf(System.getenv("PORT")));
        
        /* Main database instance */
        Database database = new Database(Constants.POSTGRES_URL);
        
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
        result.add("INSERT INTO subject(name) VALUES ('Xbox');");
        result.add("INSERT INTO subject(name) VALUES ('Animals');");
        result.add("INSERT INTO subject(name) VALUES ('Amnesty');");
        result.add("INSERT INTO subject(name) VALUES ('Cooking');");
        return result;
    }

}
