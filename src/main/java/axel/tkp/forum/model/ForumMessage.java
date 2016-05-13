package axel.tkp.forum.model;

/**
 * Represents a forum message.
 * 
 * @author Axel Wallin
 */
public class ForumMessage {
    
    private String sender, content;
    private String time;
    private Integer uid, threadId;
    
    public ForumMessage(Integer uid, String content, 
            String sender, String time, Integer threadId) {
        this.uid = uid;
        this.sender = sender;
        this.content = content;
        this.threadId = threadId;
        this.time = time;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getTime() {
        return time.substring(0, 16);
    }
    
    public Integer getThreadId() {
        return threadId;
    }
    
    public boolean invalid() {
        return content == null || content.equals("") 
                || sender == null || sender.equals("")
                || sender.replaceAll(" ", "").equals("")
                || content.replaceAll(" ", "").equals("");
    }
}
