package axel.tkp.forum.model;

/**
 * Represents a forum subject.
 * 
 * @author Axel Wallin
 */

public class ForumSubject {

    private Integer uid, collectivePostCount;
    private String name, lastPostTime;
    
    public ForumSubject(Integer uid, String name, 
            String lastPostTime, Integer collectivePostCount) {
        this.uid = uid;
        this.name = name;
        this.lastPostTime = lastPostTime;
        this.collectivePostCount = collectivePostCount;
    }
    
    public Integer getCollectivePostCount() {
        return collectivePostCount;
    }
    
    public Integer getUid() {
        return uid;
    }
    
    public String getName() {
        return name;
    }
    
    public String getLastPostDate() {
        return lastPostTime;
    }
}
