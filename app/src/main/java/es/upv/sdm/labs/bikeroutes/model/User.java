package es.upv.sdm.labs.bikeroutes.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.enumerations.Gender;
import es.upv.sdm.labs.bikeroutes.interfaces.Enviable;
import es.upv.sdm.labs.bikeroutes.util.pojo.UserPOJO;

/**
 * Created by anderson on 11/04/2016.
 */
public class User implements Enviable{

    private int id;
    private String name;
    private String mail;
    private String password;
    private String description;
    private Gender gender;
    private Bitmap image;
    private ArrayList<User> friends;
    private ArrayList<Event> confirmedEvents;
    private ArrayList<Event> invited;
    private ArrayList<Event> historic;

    public User(){
        this("", "", "", null);
    }

    public User(String name, String mail, String password, Bitmap image) {
        this(0, name, mail, password, "", Gender.UNINFORMED, image);
    }

    public User(int id, String name, String mail, String password, String description, Gender gender, Bitmap image){
        this(id, name, mail, password, description, gender, image,  new ArrayList<User>(), new ArrayList<Event>(), new ArrayList<Event>(), new ArrayList<Event>());
    }

    public User(int id, String name, String mail, String password, String description, Gender gender,
                Bitmap image, ArrayList<User> friends, ArrayList<Event> confirmedEvents, ArrayList<Event> invited, ArrayList<Event> historic) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.description = description;
        this.gender = gender;
        this.image = image;
        this.friends = friends;
        this.confirmedEvents = confirmedEvents;
        this.invited = invited;
        this.historic = historic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<Event> getConfirmedEvents() {
        return confirmedEvents;
    }

    public void setConfirmedEvents(ArrayList<Event> confirmedEvents) {
        this.confirmedEvents = confirmedEvents;
    }

    public ArrayList<Event> getInvited() {
        return invited;
    }

    public void setInvited(ArrayList<Event> invited) {
        this.invited = invited;
    }

    public ArrayList<Event> getHistoric() {
        return historic;
    }

    public void setHistoric(ArrayList<Event> historic) {
        this.historic = historic;
    }

    public boolean hasImage(){
        return this.image!=null;
    }

    public boolean isFriend(User user){
        return this.friends.contains(user);
    }

    @Override
    public boolean equals(Object o) {
        return this.id == ((User)o).getId();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", gender=" + gender +
                ", image=" + image +
                ", friends=" + friends +
                ", confirmedEvents=" + confirmedEvents +
                ", invited=" + invited +
                ", historic=" + historic +
                '}';
    }

    public static ArrayList<User> getUsers() {
        ArrayList<User> persons = new ArrayList<User>();


        persons.add(new User("Juan", "juan@email.com", "123", null ));
        persons.add(new User("Hans", "hans@email.com", "123", null ));
        persons.add(new User("Max", "max@email.com", "123", null ));
        persons.add(new User("Klaus", "klaus@email.com", "123", null ));
        persons.add(new User("Lucas", "lucas@email.com", "123", null ));

        return persons;
    }

    public static String toJsonArray(ArrayList<User> users){
        String json = "[";
        boolean ok = false;
        for(User u : users) {json += u.toJson()+","; ok=true;}
        if(ok) json = json.substring(0,json.length()-1);
        json += "]";
        return json;
    }

    @Override
    public String toJson() {
        String json = new UserPOJO(this).toJson();
        json = json.substring(json.indexOf('[')+1, json.length()-2);
        return json;
    }

    public void copy(User other){
        if(other==null) return;
        this.id = other.getId();
        this.name = other.getName();
        this.mail = other.getMail();
        this.password = other.getPassword();
        this.description = other.getDescription();
        this.gender = other.getGender();
        this.image = other.getImage();
        this.friends = other.getFriends();
        this.confirmedEvents = other.getConfirmedEvents();
        this.invited = other.getInvited();
        this.historic = other.getHistoric();
    }
}
