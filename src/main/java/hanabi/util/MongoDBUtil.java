package hanabi.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hanabi.model.*;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBUtil {
    private static final MongoClient mongoClient;
    private static final MongoDatabase database;

    static {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder().automatic(true).build()
        );
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(global.MONGO_URI))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry)
                .build();
        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(global.DB_NAME);
        DatabaseInitializer.initialize();
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static <T> MongoCollection<T> getCollection(String name, Class<T> clazz) {
        return database.getCollection(name, clazz);
    }

    public static MongoCollection<org.bson.Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public static void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}