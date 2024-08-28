package DAO;

import Toolbox.Config;
import Toolbox.Connections;
import com.mongodb.client.MongoClient;

import java.sql.Connection;

public class DAO <T, K> {
// genericite

    protected final Object conn;
    protected final Connections connectionType;
    protected final Config config;

    public DAO(Object connection, Connections connectionType) {
        this.conn = connection;
        this.connectionType = connectionType;
        this.config = Config.getInstance();
    }
}

