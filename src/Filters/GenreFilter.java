package Filters;

import DataSet.MovieDatabase;
import Interfaces.Filter;

import java.io.IOException;

public class GenreFilter implements Filter {
    private String myGenre;

    public  GenreFilter(String genre) {
        myGenre = genre;
    }

    @Override
    public boolean satisfies(String id) throws IOException {
        return MovieDatabase.getGenres(id).contains(myGenre);
    }

}