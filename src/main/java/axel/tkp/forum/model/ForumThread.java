package axel.tkp.forum.model;

/**
 * Represents a forum thread.
 * 
 * @author Axel Wallin
 */
public class ForumThread {

    private Integer threadId, subjectId, postCount;
    private String title, lastPostDate;
    
    public ForumThread(Integer threadId, String title, 
            Integer subjectId, String lastDate, Integer postCount) {
        this.threadId = threadId;
        this.subjectId = subjectId;
        this.title = title;
        this.lastPostDate = lastDate;
        this.postCount = postCount;
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
    
    public Integer getPostCount() {
        return postCount;
    }

    public boolean invalid() {
        return title == null || title.equals("") 
                || title.length() > 30
                || title.replaceAll(" ", "").equals("");
    }
}
