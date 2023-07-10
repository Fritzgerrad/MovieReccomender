package Controllers;

import DataSet.MovieDatabase;
import DataSet.RaterDatabase;
import Filters.TrueFilter;
import Interfaces.Filter;
import Interfaces.Rater;
import Models.Rating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FourthRatings  {
    private double getAverageByID(String id,int minimalRaters){
        ArrayList<Rater> myRaters = RaterDatabase.getRaters();
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
        ArrayList<Rating> rated = new ArrayList<>();
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
    private boolean isItIn(ArrayList<String> list, String ID){
        for(int i=0;i<list.size();i++){
            String id = list.get(i);
            if(id.equals(ID)){
                return true;
            }
        }
        return false;
    }
    private double dotProduct(Rater me, Rater r){
        String meID = me.getID();
        //System.out.println("DOT ME ID IS "+meID);
        //double meRate = me.getRating(meID);
        int dotProd = 0;
        ArrayList<String> meList = me.getItemsRated();
        ArrayList<String> rList = r.getItemsRated();
        for(String s: meList){
            if (isItIn(rList,s)){
                double meRate = me.getRating(s)-5;
                double rRate = r.getRating(s)-5;
                double thisProd = meRate*rRate;
                dotProd += thisProd;
            }
        }
        return dotProd;
    }
    private ArrayList<Rating> getSimilarities (String id){
        ArrayList<Rating> simi = new ArrayList<Rating>();
        ArrayList<Rater> myRaters = RaterDatabase.getRaters();
        Rater in = RaterDatabase.getRater(id);
        String meID = in.getID();
        //System.out.println( "GET SIMILAR ME ID IS "+meID);
        for(Rater r : myRaters){
            String rid = r.getID();
            if(!rid.equals(id)){
                double rate = dotProduct(in,r);
                if(rate > 0){
                    Rating ra = new Rating(rid, rate);
                    simi.add(ra);
                }
            }
        }
        //ArrayList<Rating> similar = sortByRating(simi);
        Collections.sort(simi,Collections.reverseOrder());
        return simi;
    }

    public Rater getRater(String id){
        ArrayList<Rater> myRaters = RaterDatabase.getRaters();
        Rater r = null;
        for(int i = 0; i < myRaters.size();i++){
            Rater rt = myRaters.get(i);
            String rater_id = rt.getID();
            if (rater_id.equals(id)){
                r  = rt;
            }
        }
        return r;
    }
    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) throws IOException {
        ArrayList<Rating> rated = new ArrayList<Rating>();
        //ArrayList<String> movies = MovieDatabase.filterBy(new TrueFilter());
        RecommendationRunner r = new RecommendationRunner();
        ArrayList<String> movies = r.getItemsToRate();
        ArrayList<Rating> simi = getSimilarities(id);
        Rater irt = getRater(id);
        String meID = irt.getID();
        //System.out.println("GET SIMILAR RATINGS ME ID IS "+meID);
        if(numSimilarRaters > simi.size()){numSimilarRaters = simi.size();}
        for(int i = 0; i < movies.size();i++){
            String ID = movies.get(i);
            int count = 0;
            double sumWeightedRating = 0.0;
            for (int j = 0;j < numSimilarRaters;j++){
                Rating  raterSimi = simi.get(j);
                String rater_id = raterSimi.getItem();
                double SimilarRating = raterSimi.getValue();
                Rater currRater = getRater(rater_id);
                if(currRater.hasRating(ID)){
                    double rawRating = currRater.getRating(ID);
                    double weightedRating = rawRating * SimilarRating;
                    sumWeightedRating += weightedRating;
                    count++;
                }
            }
            if(count >= minimalRaters){
                double weightedAvg = sumWeightedRating/count;
                Rating rt = new Rating(ID,weightedAvg);
                rated.add(rt);
            }
        }
        Collections.sort(rated,Collections.reverseOrder());
        return rated;
    }
    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minimalRaters,Filter f) throws IOException {
        ArrayList<Rating> rated = new ArrayList<Rating>();
        ArrayList<String> movies = MovieDatabase.filterBy(f);
        ArrayList<Rating> simi = getSimilarities(id);
        for(int i = 0; i < movies.size();i++){
            String ID = movies.get(i);
            int count = 0;
            double sumWeightedRating = 0.0;
            for (int j = 0;j < numSimilarRaters;j++){
                Rating  raterSimi = simi.get(j);
                String rater_id = raterSimi.getItem();
                double SimilarRating = raterSimi.getValue();
                Rater currRater = getRater(rater_id);
                if(currRater.hasRating(ID)){
                    double rawRating = currRater.getRating(ID);
                    double weightedRating = rawRating * SimilarRating;
                    sumWeightedRating += weightedRating;
                    count++;
                }
            }
            if(count >= minimalRaters){
                double weightedAvg = sumWeightedRating/count;
                Rating rt = new Rating(ID,weightedAvg);
                rated.add(rt);
            }
        }
        Collections.sort(rated,Collections.reverseOrder());
        return rated;
    }
}
