package Controllers;


import DataSet.MovieDatabase;
import Filters.TrueFilter;
import Interfaces.Filter;
import Interfaces.Rater;
import Models.Rating;

import java.io.IOException;
import java.util.*;

public class ThirdRatings {
    //private ArrayList<Movie> myMovies;
    private ArrayList<Rater> myRaters;
    public ThirdRatings() throws IOException {
        // default constructor
        this("data/ratings.csv");
        //this("ratings_short.csv");
    }
    public ThirdRatings( String ratingsfile) throws IOException {
        FirstRatings fr = new FirstRatings();
        myRaters = fr.loadRaters(ratingsfile);
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
    public ArrayList<Rating> getAverageRatings(int minimalRaters) throws IOException {
        ArrayList<Rating> rated = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        for(int i = 0; i < movies.size();i++){
            String ID = movies.get(i);
            double rating = getAverageByID(ID,minimalRaters);
            Rating rt = new Rating(ID,rating);

            if(rating > 0.0){rated.add(rt);}
        }
        return rated;
    }
    public int minRate(ArrayList<Rating> rt){
        int min = 0;
        for (int i=0;i<rt.size();i++){
            Rating rate = rt.get(i);
            double r = rate.getValue();
            Rating minRate = rt.get(min);
            double minVal = minRate.getValue();
            if(r < minVal){
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
    public ArrayList <Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) throws IOException {
        ArrayList<Rating> rated = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(filterCriteria);
        for(int i = 0; i < movies.size();i++){
            String ID = movies.get(i);
            double rating = getAverageByID(ID,minimalRaters);
            Rating rt = new Rating(ID,rating);

            if(rating > 0.0){rated.add(rt);}
        }
        return rated;
    }

}
