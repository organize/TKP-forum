package axel.tkp.forum.model;

/**
 * Represents a forum subject.
 * 
 * @author Axel Wallin
 */

public class ForumSubject {

    private Integer uid;
    private String name;
    
    public ForumSubject(Integer uid, String name) {
        this.uid = uid;
        this.name = name;
    }
    
    public Integer getUid() {
        return uid;
    }
    
    public String getName() {
        return name;
    }
}
