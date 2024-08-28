package DAO;

import Toolbox.Config;
import Toolbox.Connections;
import Toolbox.MongoDB;
import Toolbox.MySqlDb;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {

    /***** for MongoDB and MySql ***/
    public static Connections connectionType = null;
    public static Object connection = null;
    private static final Config config = Config.getInstance();;

    public static Object getConnection() {
        connectionType = Connections.valueOf(config.getDatabaseType());
        if (connectionType == connectionType.MONGO){
            connection = MongoDB.connect();
        }
        connection = MySqlDb.connect();
        return connection;
    }

    /***** for actors and movies ***/

    public static IDAO create(DAOType type) {
        switch (type) {
            case MOVIE -> {
                return new MovieDAO(getConnection(), connectionType);
            }
            case ACTOR -> {
                return new ActorDAO(getConnection(), connectionType);
            }
            default -> {
                return null;
            }
        }
    }
}
