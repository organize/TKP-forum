package axel.tkp.forum.util;

import axel.tkp.forum.util.collectors.*;
import axel.tkp.forum.dao.*;
import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import static spark.Spark.*;

/**
 * Helper class to bind requests.
 * 
 * @author Axel Wallin
 */
public class RequestBinder {
    
    /**
     * Initializes all GETs and POSTs.
     * 
     * @param database the main database instance.
     */
    public static void init(Database database) {
        try {
            bindGET(database);
        } catch(Exception e) {
            halt(404, Constants.MESSAGE_404);
        }
        bindPOST(database);
    }
    
    /**
     * Bind all GET requests.
     * 
     * @param database the database instance.
     * @throws Exception in case of emergency.
     */
    private static void bindGET(Database database) throws Exception {
        /* Initialize the DAOs just once */
        SubjectDAO subjectDao = new SubjectDAO(database);
        ThreadDAO threadDao = new ThreadDAO(database);
        
        /* The main view */
        get("/", (req, res) -> {
            List<ForumSubject> subjects = new SubjectCollector(subjectDao)
                    .collect(subjectDao.getAll());

            Map map = new HashMap<>();
            map.put("subtitle", "TKP-Forum");
            map.put("baseUrl", Constants.BASE_PATH);
            map.put("subjects", subjects);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        /* When a subject has been selected */
        get("/subject", (req, res) -> {
            int subjectid = parseFromParam(req, "subjectId");
            Collector<ForumThread> collector = new ThreadCollector(threadDao);
            List<ForumThread> threads = collector.collect(threadDao.getForSubject(subjectid));
            Map map = new HashMap<>();
            map.put("subtitle", "TKP-Forum");
            map.put("baseUrl", Constants.BASE_PATH);
            map.put("returnUrl", map.get("baseUrl") + "/");
            map.put("subjectName",
                    new SubjectCollector(subjectDao)
                    .collect(subjectDao.getForUID(subjectid))
                    .get(0).getName());
            map.put("threads", threads);
            map.put("subjectId", subjectid);
            return new ModelAndView(map, "threads");
        }, new ThymeleafTemplateEngine());

        /* When a thread has been selected */
        get("/thread", (req, res) -> {
            int threadId = Integer.parseInt(req.queryParams("threadId"));
            return new ModelAndView(getThread(req, database, threadDao, threadId), "forumPost");
        }, new ThymeleafTemplateEngine());

        /* When posting a new thread */
        get("/newThread", (req, res) -> {
            Map map = new HashMap<>();
            map.put("baseUrl", Constants.BASE_PATH);
            map.put("subjectId", req.queryParams("subject"));
            return new ModelAndView(map, "postThread");
        }, new ThymeleafTemplateEngine());
    }
    
    /**
     * Bind all POST requests.
     * 
     * @param database the database instance.
     */
    private static void bindPOST(Database database) {
        ThreadDAO threadDao = new ThreadDAO(database);
        MessageDAO messageDao = new MessageDAO(database);
        
        /* A new post has been added to a thread */
        post("/thread", (req, res) -> {
            int threadId = parseFromParam(req, "threadId");
            String sender = req.queryParams("name");
            String content = req.queryParams("content");
            ForumMessage message 
                    = new ForumMessage(0, content, sender, "", threadId);
            messageDao.create(message);
            return new ModelAndView(getThread(req, database, threadDao, threadId), "forumPost");
        }, new ThymeleafTemplateEngine());
        
        /* A new thread has been posted */
        post("/newThread", (req, res) -> {
            int subjectId = parseFromParam(req, "subject");
            String threadTitle = req.queryParams("title");
            ForumThread thread = new ForumThread(0, threadTitle, 
                subjectId, "", 0);
            threadDao.create(thread);
            
            int threadId = threadDao.count();
            
            String sender = req.queryParams("name");
            String content = req.queryParams("content");
            ForumMessage message 
                    = new ForumMessage(0, content, sender, "", threadId);
            messageDao.create(message);
            
            res.redirect(Constants.BASE_PATH + "/thread?threadId=" + threadId + "&page=1");
            return new ModelAndView(getThread(req, database, threadDao, threadId), "forumPost");
        }, new ThymeleafTemplateEngine());
    }
    
    /**
     * Creates a new Map for the Thymeleaf engine,
     *  that represents the view for a Thread instance.
     * 
     * @param database the database instance.
     * @param threadId the thread unique id.
     * @return a map that contains all the necessary data for the engine.
     * @throws Exception in case of emergency.
     */
    private static Map getThread(Request req, Database database, ThreadDAO threadDao, int threadId) throws Exception {
        Integer pageId = parseFromParam(req, "page");
        List<ForumMessage> posts = new PostCollector()
            .collect(new MessageDAO(database).getForThreadAndLimit(threadId, pageId));
        ForumThread ownInstance = new ThreadCollector(threadDao)
                .collect(threadDao.getForUID(threadId)).get(0);
        Map map = new HashMap<>();
        map.put("subtitle", ownInstance.getTitle());
        map.put("baseUrl", Constants.BASE_PATH);
        map.put("returnUrl", 
                map.get("baseUrl") + "/subject?subjectId=" + ownInstance.getSubjectId());
        map.put("threadId", threadId);
        map.put("posts", posts);
        map.put("numPages", getNumPages(ownInstance.getPostCount()));
        return map;
    }
    
    /**
     * Helper method to catch exceptions without 200 lines of copypaste.
     * Eliminates error-poking and possible injections.
     * 
     * @param req the request instance.
     * @param paramName the parameter name of which's value is required.
     * @return the value specified by paramName, or -1 if nonexisting.
     */
    private static Integer parseFromParam(Request req, String paramName) {
        int result = 1;
        try {
            result = Integer.parseInt(req.queryParams(paramName));
        } catch(Exception e) {
            halt(404, Constants.MESSAGE_404);
        }
        return result;
    }
    
    private static int getNumPages(int posts) {
        if(posts > 10 && posts < 20) {
            return 2;
        }
        if(posts > 20 && posts < 30) {
            return 3;
        }
        if(posts > 30 && posts < 40) {
            return 4;
        }
        if(posts > 40 && posts < 50) {
            return 5;
        }
        return 1;
    }
}
