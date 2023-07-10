package Filters;

import Interfaces.Filter;

public class TrueFilter implements Filter {
    @Override
    public boolean satisfies(String id) {
        return true;
    }

}
