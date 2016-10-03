package es.upv.sdm.labs.bikeroutes.util;

import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.pojo.EventPOJO;
import es.upv.sdm.labs.bikeroutes.util.pojo.IntPojo;
import es.upv.sdm.labs.bikeroutes.util.pojo.UserPOJO;

/**
 * Created by anderson on 12/04/2016.
 */
public class JsonParser {

    public static User toUser(InputStream in){
        UserPOJO u = new GsonBuilder().create().fromJson(new InputStreamReader(in), UserPOJO.class);
        return u.getUser();
    }

    public static Event toEvent(InputStream in){
        EventPOJO e =  new GsonBuilder().create().fromJson(new InputStreamReader(in), EventPOJO.class);
        return e.getEvent();
    }

    public static int toInt(InputStream in){
        InputStreamReader is = new InputStreamReader(in);
        IntPojo i = new GsonBuilder().create().fromJson(is, IntPojo.class);
        return i.getInt();
    }

    public static Integer[] toInts(InputStream in){
        InputStreamReader is = new InputStreamReader(in);
        IntPojo i = new GsonBuilder().create().fromJson(is, IntPojo.class);
        return i.getInts();
    }

    public static User toUser(String json){
        UserPOJO u = new GsonBuilder().create().fromJson(json, UserPOJO.class);
        return u.getUser();
    }

    public static ArrayList<User> toUsers(InputStream in){
        InputStreamReader isr = new InputStreamReader(in);
            UserPOJO u =  new GsonBuilder().create().fromJson(isr, UserPOJO.class);
        return u.toUsers();
    }

    public static ArrayList<User> toUsers(String json){
        UserPOJO u =  new GsonBuilder().create().fromJson(json, UserPOJO.class);
        return u.toUsers();
    }

    public static Event toEvent(String json){
        EventPOJO e =  new GsonBuilder().create().fromJson(json, EventPOJO.class);
        return e.getEvent();
    }

    public static ArrayList<Event> toEvents(InputStream in){
        InputStreamReader is = new InputStreamReader(in);
        EventPOJO e =  new GsonBuilder().create().fromJson(is, EventPOJO.class);
        return e.toEvents();
    }

    public static ArrayList<Event> toEvents(String json){
        EventPOJO e =  new GsonBuilder().create().fromJson(json, EventPOJO.class);
        return e.toEvents();
    }

}
