package axel.tkp.forum.util.collectors;

import axel.tkp.forum.model.ForumMessage;
import axel.tkp.forum.util.Collector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostCollector implements Collector<ForumMessage> {

    @Override
    public ArrayList<ForumMessage> collect(ResultSet rs) throws SQLException {
        List<ForumMessage> collected = new ArrayList<>();
        while(rs.next()) {
            Integer uid = rs.getInt("uid");
            String content = rs.getString("content");
            String sender = rs.getString("sender");
            String time = rs.getString("time");
            Integer threadId = rs.getInt("threadId");
            collected.add(new ForumMessage(uid, content, sender, time, threadId));
        }
        return (ArrayList<ForumMessage>) collected;
    }

}
