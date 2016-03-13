package axel.tkp.forum.util.collectors;

import axel.tkp.forum.dao.ThreadDAO;
import axel.tkp.forum.model.ForumThread;
import axel.tkp.forum.util.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A collector implementation to collect forum threads.
 * 
 * @author Axel Wallin
 */
public class ThreadCollector implements Collector<ForumThread> {
    
    private ThreadDAO dao;
    
    public ThreadCollector(ThreadDAO dao) {
        this.dao = dao;
    }

    @Override
    public ArrayList<ForumThread> collect(ResultSet rs) throws Exception {
        List<ForumThread> collected = new ArrayList<>();
        while(rs.next()) {
            Integer uid = rs.getInt("id");
            Integer subjectId = rs.getInt("subjectId");
            String title = rs.getString("title");
            String lastPost = dao.getLatestPostDate(uid);
            Integer postCount = dao.getPostCount(uid);
            collected.add(new ForumThread(uid, title, 
                    subjectId, lastPost, postCount));
        }
        rs.close();
        return (ArrayList<ForumThread>) collected;
    }

}
