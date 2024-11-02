package edu.udel.cisc.cisc437;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class DBService {
    private MongoDatabase db;

    @Value("${connection-url}")
    private String connectionUrl;

    @PostConstruct
    private void init() {
        if(connectionUrl == null) throw new IllegalArgumentException("No database URL found");

        /*
        ServerApi serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build();
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionUrl))
            .serverApi(serverApi)
            .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }

         */
    }

    //@Bean
    public MongoDatabase getDb() {
        return db;
    }
}
