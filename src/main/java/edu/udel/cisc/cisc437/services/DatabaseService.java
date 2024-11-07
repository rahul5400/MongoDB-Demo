package edu.udel.cisc.cisc437.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

@Service
public class DatabaseService {
    private final MongoCollection<Document> users;
    private final UploadService uploadService;

    public DatabaseService(MongoClient client, UploadService uploadService) {
        users = client.getDatabase("cisc").getCollection("users");
        this.uploadService = uploadService;
    }

    public void persistCSVFileIntoDatabase(MultipartFile file) throws IOException {
        List<Document> documents = uploadService.convertFileToDocuments(file);
        insertMany(documents);
    }

    public void insertMany(List<Document> documents) {
        users.insertMany(documents);
    }

    public void deleteAll() {
        users.deleteMany(new Document()); // delete all
    }

    private List<Map<String, Object>> findAllWithFilter(Bson filter) {
        List<Map<String, Object>> usersMaps = new ArrayList<>();
        for(var user : filter == null ? users.find() : users.find(filter)) {
            Map<String, Object> map = new HashMap<>(user);
            usersMaps.add(map);
        }
        return usersMaps;
    }

    public List<Map<String, Object>> findAll() {
        return findAllWithFilter(null);
    }

    public List<Map<String, Object>> findAllByClub(String clubName) {
        return findAllWithFilter(eq("club_name", clubName));
    }

    public List<Map<String, Object>> findAllOver21() {
        return findAllWithFilter(gt("age", 21));
    }

    public String create(Map<String, Object> user) {
        Document persistedUser = new Document();
        for(var entry : user.entrySet()) {
            persistedUser.append(entry.getKey(), entry.getValue());
        }
        InsertOneResult res = users.insertOne(persistedUser);

        if(res.getInsertedId() != null) persistedUser.append("_id", res.getInsertedId());
        return persistedUser.toJson();
    }

    public String read(String id) {
        Document user = users.find(eq("_id", new ObjectId(id))).first();
        if(user == null) return "{}";
        return user.toJson();
    }

    public boolean update(String id, Map<String, Object> user) {
        Document updatedUser = new Document();
        for(var entry : user.entrySet()) {
            updatedUser.append(entry.getKey(), entry.getValue());
        }
        ObjectId objectId = new ObjectId(id);
        updatedUser.append("_id", objectId);
        long count = users.replaceOne(eq("_id", objectId), updatedUser).getMatchedCount();
        return count > 0;
    }

    public boolean delete(String id) {
        long count = users.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
        return count > 0;
    }
}
