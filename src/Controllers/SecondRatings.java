package Controllers;


import Interfaces.Rater;
import Models.Movie;
import Models.Rating;

import java.io.IOException;
import java.util.*;

public class SecondRatings {
    private ArrayList<Movie> myMovies;
    private ArrayList<Rater> myRaters;
    public SecondRatings() throws IOException {
        // default constructor
        this("data\\ratedmoviesfull.csv", "data\\ratings.csv");
        //this("ratedmovies_short.csv","ratings_short.csv");
    }
    public SecondRatings(String moviefile, String ratingsfile) throws IOException {
        FirstRatings fr = new FirstRatings();
        myMovies = fr.loadMovies(moviefile);
        myRaters = fr.loadRaters(ratingsfile);
    }
    public int getMovieSize(){
        return myMovies.size();
    }
    public int getRaterSize(){
        return myRaters.size();
    }
    private double getAverageByID(String id,int minimalRaters){
        ArrayList<Double> avg = new ArrayList<Double>();
        for (int i = 0; i < myRaters.size(); i++){
            Rater r = myRaters.get(i);
            ArrayList<String> it = r.getItemsRated();
            for(int a = 0;a<it.size();a++){
                String ida = it.get(a);
                if (ida.contains(id)){
                    double rate = r.getRating(ida);
                    if (rate != 0.0){
                        avg.add(rate);
                    }
                }
            }
        }
        return getAverage(avg, minimalRaters);
    }
    public void testGAR(){
        ArrayList<Rating> rt = getAverageRatings(1);
        for (int i = 0;i< rt.size();i++){
            Rating rate = rt.get(i);
            String id = rate.getItem();
            double r = rate.getValue();
            String title = this.getTitle(id);
            if (r != -1.0){
                System.out.println("The title is "+title+" and Rating is "+r);
            }
        }
    }
    private double getAverage(ArrayList<Double> list,int min){
        int len = list.size();
        double ans;
        double total = 0;
        if (len < min){
            return 0.0;
        }
        else{
            for(int i =0;i < len ;i++){
                double curr = list.get(i);
                total += curr;
            }
            ans = total/len;
        }
        return ans;
    }
    public ArrayList<Rating> getAverageRatings(int minimalRaters){
        ArrayList<Rating> rated = new ArrayList<Rating>();
        for(int i = 0; i < myMovies.size();i++){
            Movie m = myMovies.get(i);
            String ID = m.getID();
            double rating = getAverageByID(ID,minimalRaters);
            Rating rt = new Rating(ID,rating);
            rated.add(rt);
        }
        return rated;
    }
    private int minRate(ArrayList<Rating> rt){
        int min = 0;
        for (int i=0;i<rt.size();i++){
            Rating rate = rt.get(i);
            double r = rate.getValue();
            Rating minRate = rt.get(min);
            double minVal = minRate.getValue();
            if(r > minVal){
                min = i;
            }
        }
        return min;
    }
    public ArrayList<Rating> sortByRating (ArrayList<Rating> rt){
        ArrayList<Rating> newRate = new ArrayList<Rating> ();
        while(rt.size() > 0){
            //Rating rate = rt.get(i);
            //double r = rate.getValue();
            int min = minRate(rt);
            Rating minRate = rt.get(min);
            newRate.add(minRate);
            rt.remove(minRate);
        }
        return newRate;
    }
    public void testGARbyID(){
        for(int i = 0; i < myMovies.size();i++){
            Movie m = myMovies.get(i);
            String ID = m.getID();
            System.out.println("id is "+ID);
            double rating = getAverageByID(ID,1);
            System.out.println(rating);
        }
        //return rated;
        //double ans = getAverageByID();
    }
    public String getTitle(String id){
        for(int i = 0; i < myMovies.size();i++){
            Movie m = myMovies.get(i);
            String ID = m.getID();
            if (id.contains(ID)){
                return m.getTitle();
            }
        }
        return "NO SUCH TITLE.";
    }
    public String getID (String title){
        for(int i = 0; i < myMovies.size();i++){
            Movie m = myMovies.get(i);
            String mtitle = m.getTitle();
            if (title.contains(mtitle)){
                return m.getID();
            }
        }
        return "ID NOT FOUND.";
    }
}