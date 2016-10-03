package es.upv.sdm.labs.bikeroutes.util.async;

import es.upv.sdm.labs.bikeroutes.interfaces.AsyncExecutable;

/**
 * Created by anderson on 19/04/2016.
 */
public abstract class PostExecute implements AsyncExecutable{

    @Override
    public void preExecute(int option) {}
}
