package axel.tkp.forum.dao;

import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.ForumThread;
import axel.tkp.forum.util.collectors.ThreadCollector;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a DAO for the forum subjects.
 * 
 * @author Axel Wallin
 */
public class SubjectDAO implements AbstractDataAccessObject {
    
    private Database database;
    
    public SubjectDAO(Database database) {
        this.database = database;
    }
    
    @Override
    public ResultSet getAll() throws Exception {
        return database.getConnection()
                .createStatement().executeQuery("SELECT * FROM subject ORDER BY name ASC;");
    }

    @Override
    public ResultSet getForUID(int uid) throws Exception {
        return database.getConnection().
                createStatement().executeQuery("SELECT * FROM subject WHERE id = '" + uid + "';");
    }
    
    public int getPostCount(int uid) throws Exception {
        int result = 0;
        ThreadDAO threadDao = new ThreadDAO(database);
        ResultSet rs = threadDao.getForSubject(uid);
        List<ForumThread> threads = new ThreadCollector(threadDao).collect(rs);
        return threads.stream().map((thread) -> thread.getPostCount()).reduce(result, Integer::sum);
    }
    
    public String getLastPostTime(int uid) throws Exception {
        ThreadDAO threadDao = new ThreadDAO(database);
        ResultSet rs = threadDao.getForSubject(uid);
        List<ForumThread> threads = new ThreadCollector(threadDao).collect(rs);
        List<Timestamp> timestamps = new ArrayList<>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(ForumThread thread : threads) {
                timestamps.add(new Timestamp(dateFormat.parse(thread.getLastPostDate()).getTime()));
            }
        } catch(ParseException parseException) {}
        Collections.sort(timestamps);
        return timestamps.isEmpty() ? "none!" : timestamps.get(timestamps.size() - 1).toString().replaceAll(".0", "");
    }
}
