import Classes.Actor;
import Classes.Movie;
import DAO.ActorDAO;
import DAO.DAOFactory;
import DAO.DAOType;
import DAO.MovieDAO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        ActorDAO actorDAO = (ActorDAO)DAOFactory.create(DAOType.ACTOR);
        actorDAO.create(new Actor(42, "Bloom", "Orlando", "1977-01-13", "Male"));
        List<Actor> actors = actorDAO.getAll();
        for (Actor actor : actors) {
            System.out.println(actor);
        }

        MovieDAO movieDAO = (MovieDAO)DAOFactory.create(DAOType.MOVIE);
        Actor orlando = actorDAO.find(42);
        System.out.println(orlando);
        movieDAO.create(new Movie(42, "Kingdom of heaven", "2h30", "Action", Arrays.asList(orlando)));
        List<Movie> movies = movieDAO.getAll();
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        Movie kingdomOfHeaven = movieDAO.find(42);
        System.out.println(kingdomOfHeaven);
    }
}