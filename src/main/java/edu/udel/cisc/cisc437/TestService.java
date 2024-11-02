package edu.udel.cisc.cisc437;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    MongoClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("hello world, I have just started up");

        MongoCollection<Document> collection = client.getDatabase("cisc").getCollection("users");
        FindIterable<Document> results = collection.find();
        for(var res : results) {
            System.out.println(res.get("name")); // "Test"
            System.out.println(res.get("age")); // 20
        }
    }
}
