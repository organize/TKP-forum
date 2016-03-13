package axel.tkp.forum.util;

import axel.tkp.forum.util.collectors.*;
import axel.tkp.forum.dao.*;
import axel.tkp.forum.database.Database;
import axel.tkp.forum.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
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
            System.out.println("error -> cannot bind GETs");
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
        List<ForumSubject> subjects = new SubjectCollector()
            .collect(new SubjectDAO(database).getAll());
        
        /* The main view */
        get("/", (req, res) -> {
            Map map = new HashMap<>();
            map.put("subtitle", "TKP-Forum");
            map.put("baseUrl", "http://localhost:4567");
            map.put("subjects", subjects);
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        /* When a subject has been selected */
        get("/subject", (req, res) -> {
            int subjectid = Integer.parseInt(req.queryParams("subjectId"));
            ThreadDAO threadDao = new ThreadDAO(database);
            Collector<ForumThread> collector = new ThreadCollector(threadDao);
            List<ForumThread> threads = collector.collect(threadDao.getForSubject(subjectid));
            Map map = new HashMap<>();
            map.put("subtitle", "TKP-Forum");
            map.put("baseUrl", "http://localhost:4567");
            map.put("threads", threads);
            return new ModelAndView(map, "threads");
        }, new ThymeleafTemplateEngine());
        
        /* When a thread has been selected */
        get("/thread", (req, res) -> {
            int threadId = Integer.parseInt(req.queryParams("threadId"));
            return new ModelAndView(getThread(database, threadId), "forumPost");
        }, new ThymeleafTemplateEngine());
    }
    
    /**
     * Bind all POST requests.
     * 
     * @param database the database instance.
     */
    private static void bindPOST(Database database) {
        post("/thread", (req, res) -> {
            int threadId = Integer.parseInt(req.queryParams("threadId"));
            MessageDAO messageDao = new MessageDAO(database);
            
            String sender = req.queryParams("name");
            String content = req.queryParams("content");
            ForumMessage message 
                    = new ForumMessage(0, content, sender, "", threadId);
            messageDao.create(message);
            return new ModelAndView(getThread(database, threadId), "forumPost");
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
    private static Map getThread(Database database, int threadId) throws Exception {
        List<ForumMessage> posts = new PostCollector()
            .collect(new MessageDAO(database).getForThread(threadId));
        Map map = new HashMap<>();
        map.put("subtitle", "TKP-Forum");
        map.put("baseUrl", "http://localhost:4567");
        map.put("threadId", threadId);
        map.put("posts", posts);
        return map;
    }

}
