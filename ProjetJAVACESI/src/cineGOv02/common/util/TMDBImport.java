package cineGOv02.common.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cineGOv02.common.entity.Film;
import cineGOv02.common.hibernate.MySQLDataSource;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;

/**
 * Classe permettant d'importer des film depuis l'API TMDB et de les enregistrer en BD
 * @author Hugo
 *
 */
public class TMDBImport {
    private SessionFactory factory;
    private Session session;
    // Verrou pour gérer l'interblocage des thread
    /** URL de base pour la récupératiuon des posters de film */
    private String baseUrlTmdb = "http://image.tmdb.org/t/p/"; 
    /** Paramètre de résolution pour les poster */
    private String sizeImage = "w500";
    /** API KEY */
    private String apiKey = "4412ceac8d03393da24ea311a5e5cc69";
    /** Création de l'objet de l'objet d'appel de l'API tmdb */
    private TmdbApi tmdbApi;
    /** Initialisation de la récupération de film */
    private TmdbMovies tmdbMovies;
    /** Objet Film à insérer en bd */
    private Film film;
    /** Id du film */
    private int idMovie;
    /** Film */
    private MovieDb movie;
    /**Crédits */
    private Credits credits;
    /** Equipe technique */
    private ArrayList<PersonCrew> crewList;
    /** Casting */
    private ArrayList<PersonCast> castList;
    /** Genres du film */
    private ArrayList<Genre> genresList;
    /** Titre du film */
    private String titre;
    /** Synospis */
    private String overview;
    /***/
    private String genre;
    /** Date de sortie */
    private String releaseDate;
    /** Durée */
    private int runTime;
    /** Note */
    private double rate;
    /** Réalisateur */
    private String realisateur;
    /** Casting*/
    private String casting = null;
    /** Date de sortie*/
    private java.sql.Date dateSortie = null;
    /** Poster au format Blob*/
    private Blob blob = null;
    /** Pages de résultat */
    private MovieResultsPage nowMoviesPage;
    /** Liste de movie */
    private ArrayList<MovieDb>nowMoviesList;
    /** Liste de film */
    private ArrayList<Film> filmList = new ArrayList<Film>();
    /** Date d'ajout du film */
    private java.sql.Date dateAjout = new java.sql.Date(Calendar.getInstance().getTime().getTime());
    /** Liste des film effectivement ajoutés */
    private ArrayList<Film> listeAjoutes = new ArrayList<>();

    /**
     * Méthode d'import des film en BD
     * @param args
     */
    public ArrayList<Film> ImportFromTMDB() {
        tmdbApi = new TmdbApi(apiKey);
        tmdbMovies = tmdbApi.getMovies();
        nowMoviesPage = tmdbMovies.getNowPlayingMovies("en", null);
        nowMoviesList = new ArrayList<MovieDb>(nowMoviesPage.getResults());
        factory = MySQLDataSource.getInstance().getFactory();
        session = factory.openSession();
        session.getTransaction().begin();
        ArrayList<Film> filmBD = new ArrayList<Film>(session.createQuery("FROM Film").list());

        //Récupère les 20 premiers films à l'affiche
        for(int i=0; i < nowMoviesList.size(); i++){
            idMovie = nowMoviesList.get(i).getId();
            boolean estPresent = false;
            if(filmBD.size()> 0){
                for (Film cdFilmBD : filmBD) {
                    if(cdFilmBD.getIdTMDB() == idMovie){
                        estPresent = true;
                    }
                }
            }
            if(!estPresent){
                movie = tmdbMovies.getMovie(idMovie, "en");
                credits = tmdbMovies.getCredits(idMovie);
                crewList = new ArrayList<PersonCrew>(credits.getCrew());
                castList = new ArrayList<PersonCast>(credits.getCast());
                genresList = new ArrayList<Genre>(movie.getGenres());
                String posterPath = movie.getPosterPath();
                titre = movie.getTitle();
                overview = movie.getOverview();
                if(genresList.size() > 0){
                    genre = genresList.get(0).getName();
                }
                genre = getGenre(genre);
                releaseDate = movie.getReleaseDate();
                runTime = movie.getRuntime() == 0 ? 120 : movie.getRuntime();
                rate = movie.getVoteAverage();
                realisateur = crewList.size() > 0 ? crewList.get(0).getName() : null;

                //Obtient les 4 premiers acteurs du casting. 
                for(int j= 0 ; j < castList.size() && j < 3; j++){
                    casting += castList.get(j).getName() + ", ";
                }
                if (casting.substring(0, 4).equals("null")){
                    casting = casting.substring(4, casting.length());
                }
                // Obtient la date
                String[] datetoken = releaseDate.split("-");
                String releaseDate2 = datetoken[2] + "/" + datetoken[1] + "/" + datetoken[0];
                DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    dateSortie = new java.sql.Date(sourceFormat.parse(releaseDate2).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Obtention de l'image à partir de l'URL et convertion en type java.SQL.blob
                URL imageURL;
                BufferedImage originalImage;
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                byte[] imageInByte;

                try {
                    imageURL = new URL(baseUrlTmdb + sizeImage + posterPath);
                    originalImage = ImageIO.read(imageURL);
                    ImageIO.write(originalImage, "jpg", baos );
                    baos.flush();
                    imageInByte = baos.toByteArray();
                    blob = new javax.sql.rowset.serial.SerialBlob(imageInByte);
                    //Persist - in this case to a file
                    FileOutputStream fos = new FileOutputStream("images/outputmageName.jpg");
                    baos.writeTo(fos);
                    fos.close();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                String color = Integer.toString(generatePastelColor().getRGB());
                film = new Film(idMovie,titre, runTime, genre, realisateur, dateSortie, dateAjout, casting, overview, rate, blob, color);
                filmList.add(film);
            }
        }
        for (Film ceFilm : filmList) {
            session.save(ceFilm);
        }
        session.getTransaction().commit();
        session.close();
        return filmList;
    }

    private String getGenre(String genre) {
        switch(genre){
        case "Action":
            return "Action";
        case "Animation":
            return "Animation";
        case "Adventure":
            return "Aventure";
        case "Comedy":
            return "Comédie";
        case "Crime":
            return "Crime";
        case "Thriller":
            return "Thriller";
        case "Doocumentary":
            return "Documentaire";
        case "Drama":
            return "Drame";
        case "Fantasy":
            return "Fantastique";
        case "Horror":
            return "Action";
        case "Science Fiction":
            return "Science Fiction";
        case "Romance":
            return "Romance";
        default:
            return "Autre";
        }
    }
    /**
     * Génère des couleurs pastels
     * @return color
     */
    public Color generatePastelColor() {
        Random random = new Random();
        // Will produce only bright / light colours:
        float red = (float) (random.nextFloat() / 2 + 0.5);
        float green = (float) (random.nextFloat() / 2 + 0.5);
        float blue = (float) (random.nextFloat() / 2 + 0.5);
        Color color = new Color(red, green, blue);
        return color;
    }
}