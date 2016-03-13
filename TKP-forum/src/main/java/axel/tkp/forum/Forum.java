package axel.tkp.forum;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.util.RequestBinder;
import static spark.Spark.*;

/**
 * Program entry class.
 * 
 * @author Axel Wallin
 */
public class Forum {
    
    public static void main(String[] argv) throws Exception {
        Database database = new Database("forum.db");
        /* Relative location to our misc. files */
        staticFileLocation("public");
        
        /* Bind the GETs and POSTs */
        RequestBinder.init(database);
        
        /* Initialize Spark */
        init();
    }

}
