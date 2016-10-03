package es.upv.sdm.labs.bikeroutes.util.pojo;

import com.google.gson.GsonBuilder;

import es.upv.sdm.labs.bikeroutes.interfaces.Enviable;

/**
 * Created by anderson on 12/04/2016.
 */
public abstract class AbstractPOJO implements Enviable {

    @Override
    public String toJson(){
        return new GsonBuilder().create().toJson(this);
    }
}
