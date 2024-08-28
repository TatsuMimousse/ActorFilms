package Toolbox;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {
    private static Config instance;

    private String databaseType;
    private MongoDBConfig mongodb;
    private MySQLConfig mysql;

    public static class MongoDBConfig {
        private String host;
        private int port;
        private String database;

        // Getters and Setters
        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }
    }

    public static class MySQLConfig {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;

        // Getters and Setters
        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Singleton pattern to load the configuration
    public static Config getInstance() {
        if (instance == null) {
            try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.json");
                 JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {

                Gson gson = new Gson();
                instance = gson.fromJson(reader, Config.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    // Getters and Setters for the Config class fields
    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public MongoDBConfig getMongodb() {
        return mongodb;
    }

    public void setMongodb(MongoDBConfig mongodb) {
        this.mongodb = mongodb;
    }

    public MySQLConfig getMysql() {
        return mysql;
    }

    public void setMysql(MySQLConfig mysql) {
        this.mysql = mysql;
    }
}
