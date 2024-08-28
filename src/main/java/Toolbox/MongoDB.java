package Toolbox;
// imports

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDB {

    private static MongoClient mongoClient = null;

    public static MongoClient connect() {
        if (mongoClient == null) {
            Config config = Config.getInstance();
            Config.MongoDBConfig mongoConfig = config.getMongodb();
            String connectionString = "mongodb://" + mongoConfig.getHost() + ":" + mongoConfig.getPort();
            mongoClient = MongoClients.create(connectionString);
            // creation du server sur vm Linux
        }return mongoClient;
    }
}

