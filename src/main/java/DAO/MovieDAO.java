package DAO;

import Classes.Actor;
import Classes.Movie;
import Toolbox.Connections;
import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO extends DAO<Movie , Integer> implements  IDAO<Movie , Integer> {

    public final String tableName = "movie";
    public final String keyName = "idMovie";

    public MovieDAO(Object connection, Connections connectionType) {
        super(connection, connectionType);
    }


    @Override
    public int create(Movie movie) throws SQLException {
        int result = 0;
        switch (this.connectionType) {
            case MONGO -> {
                // Convert Movie object to JSON
                String json = new Gson().toJson(movie, Movie.class);

                // Insert into MongoDB
                MongoClient mongoClient = (MongoClient) this.conn;
                MongoDatabase database = mongoClient.getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);

                collection.insertOne(Document.parse(json));
            }
            case MYSQL -> {
                // Insert into MySQL
                String query = "INSERT INTO movies (id, title, duration, genre) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = ((Connection)conn).prepareStatement(query)) {

                    ps.setInt(1, movie.getId());
                    ps.setString(2, movie.getTitle());
                    ps.setString(3, movie.getDuration());
                    ps.setString(4, movie.getGenre());

                    // Execute the insert
                    result = ps.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                insertActors(movie.getId(), movie.getActors());
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return result;
    }

    @Override
    public Movie find(Integer id) throws SQLException {
        Movie movie = null;
        switch (this.connectionType) {
            case MONGO -> {
                MongoClient mongoClient = (MongoClient) this.conn;
                MongoDatabase database = mongoClient.getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);

                Bson filter = Filters.eq(this.keyName, id); // pour mongodb
                Document document = collection.find(filter).first();

                if (document != null) {
                    movie = new Gson().fromJson(document.toJson(), Movie.class);
                }
            }
            case MYSQL -> {
                String query = "SELECT * FROM movies WHERE id = ?";
                try (PreparedStatement ps = ((Connection)conn).prepareStatement(query)) {
                    ps.setInt(1, id);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        movie = new Movie(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("duration"),
                                rs.getString("genre"),
                                findActorsByMovieId(id)
                        );
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return movie;
    }

    @Override
    public List<Movie> getAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        switch (this.connectionType) {
            case MONGO -> {
                MongoClient mongoClient = (MongoClient) this.conn;
                MongoDatabase database = mongoClient.getDatabase(this.config.getMongodb().getDatabase());
                MongoCollection<Document> collection = database.getCollection(this.tableName);

                for (Document document : collection.find()) {
                    Movie movie = new Gson().fromJson(document.toJson(), Movie.class);
                    movies.add(movie);
                }
            }
            case MYSQL -> {
                String query = "SELECT * FROM movies";
                try (PreparedStatement ps = ((Connection)conn).prepareStatement(query);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Movie movie = new Movie(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("duration"),
                                rs.getString("genre"),
                                findActorsByMovieId(rs.getInt("id"))
                        );
                        movies.add(movie);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            default -> throw new SQLException(String.format("Unsupported database type: %s", this.connectionType));
        }
        return movies;
    }

    /*** methodes privées ***/

    private void insertActors(int movieId, List<Actor> actors) throws SQLException {
        if (actors != null && !actors.isEmpty()) {
            String query = "INSERT INTO movie_actors (movie_id, actor_id, actor_name, actor_surname, actor_birthday, actor_gender) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = ((Connection)conn).prepareStatement(query)) {

                for (Actor actor : actors) {
                    ps.setInt(1, movieId);
                    ps.setInt(2, actor.getId());
                    ps.setString(3, actor.getName());
                    ps.setString(4, actor.getSurname());
                    ps.setString(5, actor.getBirthday());
                    ps.setString(6, actor.getGender());
                    ps.addBatch();
                }
                ps.executeBatch(); // Execute all inserts in a single batch
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private List<Actor> findActorsByMovieId(int movieId) throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String query = "SELECT * FROM movie_actors ma INNER JOIN actors a on a.id = ma.actor_id WHERE movie_id = ?";
        try (PreparedStatement ps = ((Connection)conn).prepareStatement(query)) {
            ps.setInt(1, movieId);

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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return actors;
    }
}
