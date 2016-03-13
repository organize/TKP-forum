package axel.tkp.forum.model;

/**
 * Represents a forum thread.
 * 
 * @author Axel Wallin
 */
public class ForumThread {

    private Integer threadId, subjectId;
    private String title, lastPostDate;
    
    public ForumThread(Integer threadId, String title, Integer subjectId, String lastDate) {
        this.threadId = threadId;
        this.subjectId = subjectId;
        this.title = title;
        this.lastPostDate = lastDate;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getLastPostDate() {
        return lastPostDate;
    }
    
    public Integer getThreadId() {
        return threadId;
    }
    
    public Integer getSubjectId() {
        return subjectId;
    }
}
