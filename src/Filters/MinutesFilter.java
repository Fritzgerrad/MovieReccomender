package Filters;

import DataSet.MovieDatabase;
import Interfaces.Filter;

import java.io.IOException;

public class MinutesFilter implements Filter {
    private int myMinMin;
    private int myMaxMin;

    public MinutesFilter(int minMin,int maxMin) {
        myMinMin = minMin;
        myMaxMin = maxMin;
    }

    @Override
    public boolean satisfies(String id) throws IOException {
        int minutes = MovieDatabase.getMinutes(id);
        if(minutes >= myMinMin && minutes <= myMaxMin){
            return true;
        }
        return false;
    }

}
