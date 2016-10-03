package es.upv.sdm.labs.bikeroutes.services;


import java.io.InputStream;
import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.interfaces.AsyncExecutable;
import es.upv.sdm.labs.bikeroutes.model.Event;
import es.upv.sdm.labs.bikeroutes.model.EventType;
import es.upv.sdm.labs.bikeroutes.model.Location;
import es.upv.sdm.labs.bikeroutes.util.JsonParser;
import es.upv.sdm.labs.bikeroutes.util.pojo.IntPojo;

/**
 * Created by anderson on 12/04/2016.
 */
public class EventService extends AbstractService<Event> {

    public static final int ADD_EVENT                           = 0;
    public static final int REMOVE_EVENT                        = 1;
    public static final int UPDATE_EVENT                        = 2;
    public static final int FIND_ALL_EVENTS                     = 3;
    public static final int FIND_BLOCK_EVENTS                   = 4;
    public static final int FIND_EVENT_BY_ID                    = 5;
    public static final int FIND_USERS_CONFIRMED                = 6;
    public static final int FIND_USERS_INVITED                  = 7;
    public static final int FIND_BLOCK_USERS_CONFIRMED          = 8;
    public static final int FIND_BLOCK_INVITED                  = 9;
    public static final int CONFIRM_ATTENDANCE                  = 10;
    public static final int INVITE                              = 11;
    public static final int CANCEL_ATTENDANCE                   = 12;
    public static final int SEARCH_AVAILABLE_TO_USER            = 13;
    public static final int SEARCH_BLOCK_AVAILABLE_TO_USER      = 14;
    public static final int INVITE_MANY_PEOPLE                  = 15;

    public static final int ERROR_REMOVE_EVENT          = 500;
    public static final int ERROR_EVENT_NOT_FOUND       = 501;

    @Override
    public void findBlock(int position, int length, ArrayList<Event> responseReference, AsyncExecutable exec) {
        this.listData = responseReference;
        this.request(FIND_BLOCK_EVENTS, "find_block_events", new String[]{"position", "length"},
                new String[]{String.valueOf(position), String.valueOf(length)}, exec);
    }

    @Override
    public void insert(Event event, AsyncExecutable exec) {
        this.objData = event;
        this.send(ADD_EVENT, "add_event", new String[]{"event"}, exec);
    }

    @Override
    public void update(Event event, AsyncExecutable exec) {
        this.objData = event;
        this.send(UPDATE_EVENT, "update_event", new String[]{"event"}, exec);
    }

    @Override
    public void remove(int id, AsyncExecutable exec) {
        this.send(REMOVE_EVENT, "remove_event", new String[]{"id"}, new String[]{String.valueOf(id)}, exec);
    }

    public void findNearbyEvents(int idUser, Location location, ArrayList<Event> responseReference, AsyncExecutable exec){
        this.searchAvailableEventsToUser(responseReference, idUser, null, null, null, Location.DEFAULT_DISTANCE, location, exec);
    }

    public void findBlockNearbyEvents(int position, int length, int idUser, Location location,
                                      ArrayList<Event> responseReference, AsyncExecutable exec){
        this.searchBlockAvailableEventsToUser(position, length, responseReference, idUser,
                null, null, null, Location.DEFAULT_DISTANCE, location, exec);
    }

    public void inviteManyPeople(Integer ids[], int idE, AsyncExecutable exec){
        this.intData = ids;
        this.send(INVITE_MANY_PEOPLE, "invite_many_people", new String[]{"event_id", "users_ids"}, new String[]{String.valueOf(idE)}, exec);
    }

    public void searchAvailableEventsToUser(ArrayList<Event> responseReference, int idUser, EventType type, String date,
                                            String hour, int distance, Location location,  AsyncExecutable exec){
        this.listData = responseReference;
        String ty = (type==null) ? "" : type.getType().toString();
        String spec = (type==null) ? "" : type.getSpecification();
        if(date==null) date = "";
        if(hour==null) hour = "";
        String lat = (location==null) ? "" : String.valueOf(location.getLatitude());
        String lon = (location==null) ? "" : String.valueOf(location.getLongitude());
        this.request(SEARCH_AVAILABLE_TO_USER, "search_available_to_user",
                new String[]{"user_id", "distance", "type", "specification_type", "date", "hour", "latitude", "longitude"},
                new String[]{String.valueOf(idUser), String.valueOf(distance), ty, spec, date, hour, lat, lon}, exec);
    }

    public void searchBlockAvailableEventsToUser(int position, int length, ArrayList<Event> responseReference, int idUser, EventType type, String date,
                                                 String hour, int distance, Location location,  AsyncExecutable exec){
        this.listData = responseReference;
        String ty = (type==null) ? "" : type.getType().toString();
        String spec = (type==null) ? "" : type.getSpecification();
        if(date==null) date = "";
        if(hour==null) hour = "";
        String lat = (location==null) ? "" : String.valueOf(location.getLatitude());
        String lon = (location==null) ? "" : String.valueOf(location.getLongitude());
        this.request(SEARCH_BLOCK_AVAILABLE_TO_USER, "search_block_available_to_user",
                new String[]{"user_id", "position","length","distance","type","specification_type","date","hour","latitude","longitude"},
                new String[]{String.valueOf(idUser),String.valueOf(position),String.valueOf(length),
                        String.valueOf(distance),ty,spec,date,hour,lat,lon},exec);
    }



    @Override
    public void findById(int id, Event responseReference, AsyncExecutable exec) {
        this.objData = responseReference;
        this.request(FIND_EVENT_BY_ID, "find_event_by_id", new String[]{"id"}, new String[]{String.valueOf(id)}, exec);
    }

    @Override
    public void findAll(ArrayList<Event> responseReference, AsyncExecutable exec) {
        this.listData = responseReference;
        this.request(FIND_ALL_EVENTS, "find_all_events", null, exec);
    }

    public void invite(int idEvent, int idUser, AsyncExecutable exec){
        this.send(INVITE, "invite", new String[]{"event_id", "user_id", "confirmed"},
                new String[]{String.valueOf(idEvent), String.valueOf(idUser), String.valueOf(0)}, exec);
    }

    public void confirmAttendance(int idEvent, int idUser, AsyncExecutable exec){
        this.send(CONFIRM_ATTENDANCE, "confirm_attendance", new String[]{"event_id", "user_id", "confirmed"},
                new String[]{String.valueOf(idEvent), String.valueOf(idUser), String.valueOf(1)}, exec);
    }

    public void cancelAttendance(int idEvent, int idUser, AsyncExecutable exec){
        this.send(CANCEL_ATTENDANCE, "cancel_attendance", new String[]{"event_id", "user_id", "confirmed"},
                new String[]{String.valueOf(idEvent), String.valueOf(idUser),String.valueOf(0)},exec);
    }

    public void findConfirmedUsers(Event event, AsyncExecutable exec){
        this.objData = event;
        this.request(FIND_USERS_CONFIRMED, "find_users_confirmed", new String[]{"event_id", "confirmed"},
                new String[]{String.valueOf(event.getId()), String.valueOf(1)}, exec);
    }

    public void findInviteds(Event event, AsyncExecutable exec){
        this.objData = event;
        this.request(FIND_USERS_INVITED, "find_users_invited", new String[]{"event_id", "confirmed"},
                new String[]{String.valueOf(event.getId()), String.valueOf(0)}, exec);
    }

    public void findBlockUsers(int position, int length, Event event, AsyncExecutable exec){
        this.objData = event;
        this.request(FIND_BLOCK_USERS_CONFIRMED, "find_block_users_confirmed", new String[]{"event_id", "confirmed","position","length"},
                new String[]{String.valueOf(event.getId()), String.valueOf(1), String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findBlockGuests(int position, int length, Event event, AsyncExecutable exec){
        this.objData = event;
        this.request(FIND_BLOCK_INVITED, "find_block_invited", new String[]{"event_id", "confirmed","position","length"},
                new String[]{String.valueOf(event.getId()), String.valueOf(0), String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findAvailable(int userId, ArrayList<Event> events, AsyncExecutable exec){
        searchAvailableEventsToUser(events, userId, null, null, null, 0, null, exec);
    }

    public void findAvailable(int userId, ArrayList<Event> events){
        findAvailable(userId, events, null);
    }

    public void findConfirmedUsers(Event event){
        findConfirmedUsers(event, null);
    }

    public void findInviteds(Event event){
        findInviteds(event, null);
    }

    public void findBlockUsers(int position, int length, Event event){
        findBlockUsers(position, length, event, null);
    }

    public void findBlockGuests(int position, int length, Event event){
        findBlockGuests(position, length, event, null);
    }
    public void invite(int idEvent, int idUser){
        invite(idEvent, idUser, null);
    }

    public void confirmAttendance(int idEvent, int idUser){
        confirmAttendance(idEvent, idUser, null);
    }

    public void cancelAttendance(int idEvent, int idUser){
        cancelAttendance(idEvent, idUser, null);
    }

    public void searchAvailableEventsToUser(ArrayList<Event> responseReference, int idUser, EventType type, String date,
                                            String hour, int distance, Location location){
        searchAvailableEventsToUser(responseReference, idUser, type, date, hour, distance, location, null);
    }

    public void searchBlockAvailableEventsToUser(int position, int length, ArrayList<Event> responseReference, int idUser, EventType type, String date,
                                                 String hour, int distance, Location location){
        searchBlockAvailableEventsToUser(position, length, responseReference, idUser, type, date, hour, distance, location, null);
    }

    public void inviteManyPeople(Integer ids[], int idE){
        inviteManyPeople(ids, idE, null);
    }

    public void findNearbyEvents(int idUser, Location location, ArrayList<Event> responseReference){
        findNearbyEvents(idUser, location, responseReference, null);
    }

    public void findBlockNearbyEvents(int position, int length, int idUser, Location location, ArrayList<Event> responseReference){
        findBlockNearbyEvents(position, length, idUser, location, responseReference, null);
    }


    @Override
    protected void putParams(int option, String[] params) {
        switch (option){
            case ADD_EVENT:
            case UPDATE_EVENT:
                params[0] = objData.toJson();
                break;
            case INVITE_MANY_PEOPLE:
                params[1] = new IntPojo(intData).toJson();
                break;
        }
    }

    @Override
    protected void onResponse(int option, InputStream in) {
        switch (option) {
            case ADD_EVENT:
            case UPDATE_EVENT:
                objData.copy(JsonParser.toEvent(in));
                ServerInfo.RESPONSE_CODE = (objData.getId() != 0) ? ServerInfo.RESPONSE_OK : ServerInfo.ERROR_UNKNOWN;
                break;
            case REMOVE_EVENT:
                ServerInfo.RESPONSE_CODE = (JsonParser.toInt(in) == 0) ? ERROR_REMOVE_EVENT : ServerInfo.RESPONSE_OK;
                break;
            case FIND_EVENT_BY_ID:
                Event e = JsonParser.toEvent(in);
                if (e != null) {
                    objData.copy(e);
                    ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                } else ServerInfo.RESPONSE_CODE = ERROR_EVENT_NOT_FOUND;
                break;
            case FIND_BLOCK_EVENTS:
            case FIND_ALL_EVENTS:
            case SEARCH_AVAILABLE_TO_USER:
            case SEARCH_BLOCK_AVAILABLE_TO_USER:
                listData.addAll(JsonParser.toEvents(in));
                ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                break;
            case INVITE:
            case INVITE_MANY_PEOPLE:
            case CONFIRM_ATTENDANCE:
            case CANCEL_ATTENDANCE:
                ServerInfo.RESPONSE_CODE = (JsonParser.toInt(in) == 0) ? ServerInfo.ERROR_UNKNOWN: ServerInfo.RESPONSE_OK;
                break;
            case FIND_BLOCK_INVITED:
            case FIND_USERS_INVITED:
                this.objData.getInviteds().addAll(JsonParser.toUsers(in));
                ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                break;
            case FIND_USERS_CONFIRMED:
            case FIND_BLOCK_USERS_CONFIRMED:
                this.objData.getConfirmedUsers().addAll(JsonParser.toUsers(in));
                ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                break;
            default:
                ServerInfo.RESPONSE_CODE = ServerInfo.ERROR_UNKNOWN;
        }
    }
}
