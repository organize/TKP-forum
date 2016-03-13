package axel.tkp.forum.util.collectors;

import axel.tkp.forum.model.ForumSubject;
import axel.tkp.forum.util.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectCollector implements Collector<ForumSubject> {

    @Override
    public ArrayList<ForumSubject> collect(ResultSet rs) throws SQLException {
        List<ForumSubject> collected = new ArrayList<>();
        while(rs.next()) {
            Integer uid = rs.getInt("id");
            String name = rs.getString("name");
            collected.add(new ForumSubject(uid, name));
        }
        return (ArrayList<ForumSubject>) collected;
    }

}
