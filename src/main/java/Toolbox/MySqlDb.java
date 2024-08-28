package Toolbox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDb {

    private static Connection conn = null;

    public static Connection connect(){
        if (conn == null) {
            try {
                Config config = Config.getInstance();
                Config.MySQLConfig mysqlConfig = config.getMysql();
                String url = "jdbc:mysql://" + mysqlConfig.getHost() + ":" + mysqlConfig.getPort() + "/" + mysqlConfig.getDatabase();
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, mysqlConfig.getUsername(), mysqlConfig.getPassword());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }



}


