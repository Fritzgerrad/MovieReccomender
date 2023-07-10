package Controllers;

import DataSet.MovieDatabase;
import DataSet.RaterDatabase;
import Filters.AllFilters;
import Filters.DirectorsFilter;
import Filters.YearAfterFilter;
import Interfaces.Filter;
import Interfaces.Recommender;
import Models.Rating;

import java.io.IOException;
import java.util.*;
public class RecommendationRunner implements Recommender {
    public ArrayList<String> getItemsToRate () throws IOException {
        ArrayList<String> moviesToRate = new ArrayList<String>();
        String directors = "Quentin Tarantino,Martin Scorsese,James Wan, Clint Eastwood,Steven Spielberg,Russo,Wes Anderson,Shane Black,Charlie Chaplin,Orson Welles,Christopher Nolan,Roman Polanski,John Cassavetes,Ridley Scott,Spike Lee,Woody Allen, James Cameron, Jon Favreau,George Lucas ";
        Filter f1 =new DirectorsFilter(directors);
        int year = 1995;
        Filter f2 = new YearAfterFilter(year);
        Filter Alf = new AllFilters();
        AllFilters All = (AllFilters) Alf;
        All.addFilter(f1);
        All.addFilter(f2);
        MovieDatabase.initialize("ratedmovies_short.csv");
        //MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings_short.csv");
        //RaterDatabase.initialize("ratings.csv");
        ArrayList<String> movies = MovieDatabase.filterBy(All);
        int size = 19;
        if (movies.size() < 19){
            size = movies.size();
        }
        for(int i = 0; i < 19;i++){
            Random myRandom  = new Random();
            int m = myRandom.nextInt(movies.size());
            String s = movies.get(m);
            moviesToRate.add(s);
        }
        return moviesToRate;
    }

    public void printRecommendationsFor (String webRaterID) throws IOException {
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RaterDatabase.initialize("ratings.csv");
        FourthRatings   fr = new FourthRatings();
        int numRaters = RaterDatabase.size();
        System.out.println("Read data for "+numRaters+" raters");
        String rater_id = webRaterID;
        int minRaters = 0;
        int topSimiRaters = 3;
        ArrayList<Rating> myRate = fr.getSimilarRatings(rater_id,topSimiRaters,minRaters);
        System.out.println("<table class=\"myTable\">");
        System.out.print("<style>");
        System.out.print(".myTable {font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%;}");
        System.out.print(".myTable td, .myTable th {border: 2px solid #blue; padding: 10px;}");
        System.out.print("</style>");
        System.out.println("<tr>");
        System.out.println("<th>" +"MOVIE" +"</th>");
        System.out.println("<th>" +"GENRE" +"</th>");
        System.out.println("<th>" +"COUNTRY" +"</th>");
        System.out.println("<th>" +"RUNTIME" +"</th>");
        System.out.println("<th>" +"YEAR" +"</th>");
        System.out.println("</tr>");
        for (int i = 0;i< myRate.size();i++){
            Rating rate = myRate.get(i);
            String id = rate.getItem();
            double r = rate.getValue();
            String mTitle = MovieDatabase.getTitle(id);
            String mGenre = MovieDatabase.getGenres(id);
            String mCountry = MovieDatabase.getCountry(id);
            String mTime = makeHour(MovieDatabase.getMinutes(id));
            int mYear = MovieDatabase.getYear(id);
            System.out.println("<tr>");
            System.out.println("<td>" +mTitle +"</td>");
            System.out.println("<td>" +mGenre +"</td>");
            System.out.println("<td>" +mCountry+"</td>");
            System.out.println("<td>" +mTime+"</td>");
            System.out.println("<td>" +mYear+"</td>");
            System.out.println("</tr>");
        }
        System.out.println("</table>");
        if(myRate.size() == 0){
            System.out.println("Sorry no Movies Available for you");
        }
    }
    public String makeHour(int min){
        int Hour = min/60;
        double time = min/60;
        int mins = min - (Hour *60);
        String ans = "";
        if(Hour > 0 && mins > 0){
            if (Hour >1){
                if (mins >1 ){
                    if(mins <10){
                        ans = Hour + "hrs "+"0"+mins+ " mins";
                    }
                    else{
                        ans = Hour + "hrs "+mins+ " mins";
                    }
                }
                else{
                    ans = Hour + "hrs "+"0"+mins +" min";
                }
            }
            else{
                if (min > 1){
                    if(mins <10){
                        ans = Hour + "hr "+"0"+mins+ " mins";
                    }
                    else{
                        ans = Hour + "hr "+mins+ " mins";
                    }
                }
                else{
                    ans = Hour + "hr "+"0"+mins+ " min";
                }
            }
        }
        if(Hour > 0 && mins == 0){
            if (Hour >1){
                ans = Hour + "hrs ";
            }
            else{
                ans = Hour + "hr ";
            }
        }
        if(Hour <1 ) {
            if (mins > 1){
                ans = mins+ " mins";
            }
            else{
                ans = mins + " min";
            }
        }
        return ans;
    }
}
