package es.upv.sdm.labs.bikeroutes.util.pojo;

import java.util.ArrayList;
import java.util.Date;

import es.upv.sdm.labs.bikeroutes.model.EventType;
import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.Location;
import es.upv.sdm.labs.bikeroutes.util.DateHelper;

/**
 * Created by anderson on 12/04/2016.
 */
public class EventPOJO extends AbstractPOJO{

    private Events[] events;

    public EventPOJO(){}

    public EventPOJO(Event event){
        this.events = new Events[1];
        this.events[0] = new Events(event);
    }

    public Event getEvent(){
        if(this.events==null || this.events.length==0) return null;
        return this.events[0].getEvent();
    }

    public ArrayList<Event> toEvents(){
        ArrayList<Event> res = new ArrayList<Event>();
        for (Events e:this.events) {
            res.add(e.getEvent());
        }
        return res;
    }

    public Events[] getEvents() {
        return events;
    }

    public void setEvents(Events[] events) {
        this.events = events;
    }

    private class Events{

        public Events(){}

        public Events(Event event){
            this.setId(event.getId());
            this.setType(event.getType());
            this.setDate(DateHelper.toFormatString(event.getDate()));
            this.setDeparture(event.getDeparture());
            this.setArrival(event.getArrival());
            this.setDescription(event.getDescription());
            this.setSecret(event.isSecret());
            this.setOrganizer(new UserPOJO().new Users(event.getOrganizer()));
            this.setOver(event.isOver());
        }

        private int id;
        private EventType type;
        private String date;
        private Location departure;
        private Location arrival;
        private String description;
        private boolean secret;
        private UserPOJO.Users organizer;
        private boolean over;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public EventType getType() {
            return type;
        }

        public void setType(EventType type) {
            this.type = type;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Location getDeparture() {
            return departure;
        }

        public void setDeparture(Location departure) {
            this.departure = departure;
        }

        public Location getArrival() {
            return arrival;
        }

        public void setArrival(Location arrival) {
            this.arrival = arrival;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isSecret() {
            return secret;
        }

        public void setSecret(boolean secret) {
            this.secret = secret;
        }

        public UserPOJO.Users getOrganizer() {
            return organizer;
        }

        public void setOrganizer(UserPOJO.Users organizer) {
            this.organizer = organizer;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public Event getEvent(){
            Event res = new Event();
            res.setId(this.getId());
            res.setType(this.getType());
            res.setDate(DateHelper.toDate(this.getDate()));
            res.setDeparture(this.getDeparture());
            res.setArrival(this.getArrival());
            res.setDescription(this.getDescription());
            res.setSecret(this.isSecret());
            res.setOrganizer((this.getOrganizer().getUser()));
            res.setOver(this.isOver());
            return res;
        }
    }
}
