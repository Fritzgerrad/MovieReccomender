package Filters;

import DataSet.MovieDatabase;
import Interfaces.Filter;

import java.io.IOException;

public class DirectorsFilter implements Filter {
    private String [] myDirector;

    public  DirectorsFilter(String director) {
        myDirector = director.split("[,]",0);
    }

    @Override
    public boolean satisfies(String id) throws IOException {
        String Moviedir = MovieDatabase.getDirector(id);
        for (int i = 0; i < myDirector.length;i++){
            String director = myDirector[i];
            if(Moviedir.contains(director)){
                return true;
            }
        }
        return false;
    }

}
