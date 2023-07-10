package Controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import Interfaces.Rater;
import Models.EfficientRater;
import Models.Movie;
import org.apache.commons.csv.*;


public class FirstRatings {
    public ArrayList<Movie> loadMovies (String filename) throws IOException {
        ArrayList <Movie> myMovies= new ArrayList <Movie>();
        Reader fr = new FileReader(filename);
        CSVParser parser = new CSVParser(fr,CSVFormat.DEFAULT);
        for(CSVRecord record : parser){
            String id = record.get("id");
            String title = record.get("title");
            String year = record.get("year");
            String genres = record.get("genre");
            String director = record.get("director");
            String country = record.get("country");
            int minutes = Integer.parseInt(record.get("minutes"));
            String poster = record.get("poster");
            Movie m = new Movie(id,title,year,genres,director,country, poster,minutes);
            myMovies.add(m);
        }
        return myMovies;
    }
    public void testLoadMovies() throws IOException {
        ArrayList <Movie> myMovies = loadMovies("ratedmovies_short.csv");
        System.out.println("The Number of Movies "+myMovies.size());
        for (int i = 0; i < myMovies.size(); i++){
            Movie m = myMovies.get(i);
            System.out.println(m.toString());
        }

    }
    public void testComedyMovies() throws IOException {
        ArrayList <Movie> myMovies = loadMovies("ratedmovies_short.csv");
        System.out.println("The Number of Movies "+myMovies.size());
        int count = 0;
        for (int i = 0; i < myMovies.size(); i++){
            Movie m = myMovies.get(i);
            if (m.getGenres().contains("Comedy")){
                System.out.println(m.toString());
                count ++;
            }
        }
        System.out.println(count);
    }
    public void testLongMovies() throws IOException {
        ArrayList <Movie> myMovies = loadMovies("ratedmovies_short.csv");
        System.out.println("The Number of Movies "+myMovies.size());
        int count = 0;
        for (int i = 0; i < myMovies.size(); i++){
            Movie m = myMovies.get(i);
            if (m.getMinutes() > 150){
                System.out.println(m.toString());
                count ++;
            }
        }
        System.out.println(count);
    }
    private int getmaxDirectors(ArrayList <Movie> myMovies, String Director){
        int dir = 0;
        for (int i = 0;i < myMovies.size();i++){
            Movie m = myMovies.get(i);
            String direct = m.getDirector();
            if (direct.contains(Director)){
                dir++;
            }
        }
        return dir;
    }
    public void testMoviesDirectors() throws IOException {
        ArrayList <Movie> myMovies = loadMovies("ratedmovies_short.csv");
        //System.out.println("The Number of Movies "+myMovies.size());
        int mostDirected = 0;
        String maxDirector = null;
        int count = 0;
        int counted = 0;
        for (int i = 0; i < myMovies.size(); i++){
            Movie m = myMovies.get(i);
            String direct = m.getDirector();
            int directed = getmaxDirectors(myMovies, direct);
            if (directed > mostDirected){
                mostDirected = directed;
                maxDirector = direct;
            }
        }
        for (int a = 0;a < myMovies.size();a++){
            Movie m = myMovies.get(a);
            String direct = m.getDirector();
            int numDirected = getmaxDirectors(myMovies,direct);
            if (numDirected == mostDirected){
                System.out.println(direct);
                counted ++;
            }
        }
        System.out.println(counted);
    }
    public ArrayList<Rater> loadRaters (String filename) throws IOException {
        ArrayList <Rater> myRates = new ArrayList <Rater>();
        Reader fr = new FileReader(filename);
        CSVParser parser = new CSVParser(fr,CSVFormat.DEFAULT);
        for(CSVRecord record : parser){
            String rater_id = record.get("rater_id");
            double rating = Double.parseDouble(record.get("rating"));
            String movie_id = record.get("movie_id");
            String time = record.get("time");
            Rater r = new EfficientRater(rater_id);
            r.addRating(movie_id,rating);
            myRates.add(r);
        }
        return myRates;
    }
    public void testLoadRaters() throws IOException {
        ArrayList <Rater> myRates = loadRaters("data/ratings_short.csv");
        for (int i = 0; i < myRates.size(); i++){
            Rater r = myRates.get(i);
            String item = r.getID();
            int numOfRating  = r.numRatings();
            System.out.print(" Rated ID is "+item);
            System.out.println(" The number of ratings done is "+numOfRating);
            ArrayList<String> items = r.getItemsRated();
            for (int a = 0; a < items.size();a++){
                String movie = items.get(a);
                System.out.println("Movie ID is: "+movie+" And the Rating is: "+r.getRating(movie));
            }
        }
    }
    public void maxtestLoadRaters() throws IOException {
        ArrayList <Rater> myRates = loadRaters("data/ratings_short.csv");
        HashMap<String, Integer> raters = new HashMap <String, Integer>();
        int count = 0;
        for (int i = 0; i < myRates.size(); i++){
            Rater r = myRates.get(i);
            String item = r.getID();
            if (!raters.containsKey(item)){
                raters.put(item,1);
            }
            else{
                raters.put(item, raters.get(item)+1);
            }
        }
        for(String h : raters.keySet()){
            int curr = raters. get(h);
            if (curr > count){
                count = curr;
            }
        }
        System.out.println("The maximum rating is "+count);
        for(String h : raters.keySet()){
            int curr = raters. get(h);
            if (curr == count){
                System.out.println("Rater "+h+" rates the most movies and they have "+curr+" ratings" );
            }
        }
    }
    private int howManyheRated(String ID) throws IOException {
        ArrayList <Rater> myRates = loadRaters("ratings_short.csv");
        HashMap<String, Integer> raters = new HashMap <String, Integer>();
        int count = 0;
        for (int i = 0; i < myRates.size(); i++){
            Rater r = myRates.get(i);
            String item = r.getID();
            if (!raters.containsKey(item)){
                raters.put(item,1);
            }
            else{
                raters.put(item, raters.get(item)+1);
            }
        }
        for(String h : raters.keySet()){
            int curr = raters. get(h);
            if (h.equals(ID)){
                count = curr;

            }
        }
        return count;
    }
    public void testhowManyheRated() throws IOException {
        String ID = "193";
        int rate = howManyheRated(ID);
        System.out.println("The number of ratings by Rated ID "+ID+" is "+rate);
    }
    private int howManyRatedMovie(String movie) throws IOException {
        ArrayList <Rater> myRates = loadRaters("data/ratings_short.csv");
        HashMap<String, Integer> raters = new HashMap <String, Integer>();
        int count = 0;
        for (int i = 0; i < myRates.size(); i++){
            Rater r = myRates.get(i);
            ArrayList<String> items = r.getItemsRated();

            for (int a = 0; a < items.size();a++){
                String item = items.get(a);
                if (!raters.containsKey(item)){
                    raters.put(item,1);
                }
                else{
                    raters.put(item, raters.get(item)+1);
                }
            }
        }
        //System.out.println(item);


        for(String h : raters.keySet()){
            int curr = raters. get(h);
            if (h.equals(movie)){
                count = curr;
            }
        }
        return count;
    }
    public void testhowManyRatedMovie() throws IOException {
        String movie = "1798709";
        int rate = howManyRatedMovie(movie);
        System.out.println("The number of ratings by Rated ID "+movie+" is "+rate);
    }
    public void movieRated() throws IOException {
        ArrayList <Rater> myRates = loadRaters("ratings_short.csv");
        HashMap<String, Integer> raters = new HashMap <String, Integer>();
        int count = 0;
        for (int i = 0; i < myRates.size(); i++){
            Rater r = myRates.get(i);
            ArrayList<String> items = r.getItemsRated();

            for (int a = 0; a < items.size();a++){
                String item = items.get(a);
                if (!raters.containsKey(item)){
                    raters.put(item,1);
                }
                else{
                    raters.put(item, raters.get(item)+1);
                }
            }
        }
        for(String h : raters.keySet()){
            System.out.println(h);
            count++;
        }
        System.out.println("The size is "+raters.size()+" AND COUNT IS "+count);
    }
}