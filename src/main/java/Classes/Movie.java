package Classes;

import java.util.List;

public class Movie {
    public int id;
    public String title;
    public String duration;
    public String genre;
    public List<Actor> actors;


    /************* Constructors ***************/

    public Movie(int id, String title, String duration, String genre, List<Actor> actors) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.actors = actors;
    }

    public Movie() {
    }


    /************* Setters and Getters ***************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    /************* Methods ***************/

    public String toString() {
        StringBuilder actorsString = new StringBuilder();
        if (this.actors != null)
            for (Actor actors : this.actors) {
                actorsString.append(actors.toString());
            }
        return "Films{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", genre='" + genre + '\'' +
                ", actors= [" + actorsString + "]" +
                '}';
    }

}
