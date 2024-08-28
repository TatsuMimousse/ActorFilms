package DAO;

import Classes.Actor;
import Toolbox.Connections;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ActorDAO extends DAO<Actor, Integer> implements IDAO<Actor, Integer> {

    public final String tableName = "actor";
    public final String keyName = "idActor";

    public ActorDAO(Object connection, Connections connectionType) {
        super(connection, connectionType);
    }

    @Override
    public int create(Actor actor) throws SQLException {
        int result = 0; // l'id n'est pas auto généré
        switch (this.connectionType) {
            case MONGO -> {
                String json = new Gson().toJson(actor, Actor.class); // transformation du bean Actor en objet json
                MongoDatabase database = ((MongoClient)this.conn).getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);
                Document document = Document.parse(json);
                // Insertion du document
                collection.insertOne(document);
            }
            case MYSQL -> {
                String query = "INSERT INTO actors (id, name, surname, birthday, gender) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement ps = ((Connection)conn).prepareStatement(query);

                ps.setInt(1, actor.getId());
                ps.setString(2, actor.getName());
                ps.setString(3, actor.getSurname());
                ps.setString(4, actor.getBirthday());
                ps.setString(5, actor.getGender());

                // Execute the update
                ps.executeUpdate();
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return result;
    }


    @Override
    public Actor find(Integer id) throws SQLException {
        Actor actor = null;
        switch (this.connectionType) {
            case MONGO -> {
                MongoDatabase database = ((MongoClient)this.conn).getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);
                Document document = collection.find(eq("id", id)).first();

                if (document != null) {
                    actor = new Gson().fromJson(document.toJson(), Actor.class);
                }
            }
            case MYSQL -> {
                String query = "SELECT * FROM actors WHERE id = ?";
                PreparedStatement ps = ((Connection)conn).prepareStatement(query);
                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    actor = new Actor(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("gender")
                    );
                }
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return actor;
    }


    @Override
    public List<Actor> getAll() throws SQLException {
        List<Actor> actors = new ArrayList<>();
        switch (this.connectionType) {
            case MONGO -> {
                MongoDatabase database = ((MongoClient)this.conn).getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);
                FindIterable<Document> documents = collection.find();

                for (Document document : documents) {
                    Actor actor = new Gson().fromJson(document.toJson(), Actor.class);
                    actors.add(actor);
                }
            }
            case MYSQL -> {
                String query = "SELECT * FROM actors";
                PreparedStatement ps = ((Connection)conn).prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Actor actor = new Actor(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("birthday"),
                            rs.getString("gender")
                    );
                    actors.add(actor);
                }
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return actors;
    }

}
