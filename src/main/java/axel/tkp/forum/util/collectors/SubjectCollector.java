package axel.tkp.forum.util.collectors;

import axel.tkp.forum.dao.SubjectDAO;
import axel.tkp.forum.model.ForumSubject;
import axel.tkp.forum.util.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A collector implementation to collect forum subjects.
 * 
 * @author Axel Wallin
 */
public class SubjectCollector implements Collector<ForumSubject> {
    
    private SubjectDAO dao;
    
    public SubjectCollector(SubjectDAO dao) {
        this.dao = dao;
    }

    @Override
    public ArrayList<ForumSubject> collect(ResultSet rs) throws Exception {
        List<ForumSubject> collected = new ArrayList<>();
        while(rs.next()) {
            Integer uid = rs.getInt("id");
            String name = rs.getString("name");
            String lastPostTime = dao.getLastPostTime(uid);
            Integer collectivePostCount = dao.getPostCount(uid);
            collected.add(new ForumSubject(uid, name,
                    lastPostTime, collectivePostCount));
        }
        return (ArrayList<ForumSubject>) collected;
    }

}
