package es.upv.sdm.labs.bikeroutes.services;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.interfaces.AsyncExecutable;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.ImgSerializer;
import es.upv.sdm.labs.bikeroutes.util.JsonParser;

/**
 * Created by anderson on 12/04/2016.
 */
public class UserService extends AbstractService<User> {

    public static final int ADD_USER                        = 0;
    public static final int FIND_USER_BY_ID                 = 1;
    public static final int FIND_USER_BY_MAIL               = 2;
    public static final int UPDATE_USER                     = 3;
    public static final int REMOVE_USER                     = 4;
    public static final int FIND_ALL_USERS                  = 5;
    public static final int FIND_BLOCK_USERS                = 6;
    public static final int FIND_EVENTS_CONFIRMED           = 7;
    public static final int FIND_EVENTS_INVITED             = 8;
    public static final int FIND_EVENTS_HISTORIC            = 9;
    public static final int FIND_BLOCK_EVENTS_CONFIRMED     = 10;
    public static final int FIND_BLOCK_EVENTS_INVITED       = 11;
    public static final int FIND_BLOCK_EVENTS_HISTORIC      = 12;
    public static final int ADD_FRIEND                      = 13;
    public static final int REMOVE_FRIEND                   = 14;
    public static final int FIND_FRIENDS                    = 15;
    public static final int FIND_BLOCK_FRIENDS              = 16;
    public static final int FIND_ALL_EVENTS                 = 17;
    public static final int FIND_BLOCK_EVENTS               = 18;
    public static final int FIND_BY_NAME_OR_MAIL            = 19;
    public static final int FIND_BY_LOGIN                   = 20;
    public static final int RETRIEVE_PASSWORD               = 21;
    public static final int FIND_BLOCK_BY_NAME_OR_MAIL      = 22;



    public static final int ERROR_ADD_USER_MAIL         = 101;
    public static final int ERROR_REMOVE_USER           = 102;
    public static final int ERROR_ADD_FRIEND            = 103;
    public static final int ERROR_REMOVE_FRIEND         = 104;
    public static final int ERROR_ALREADY_FRIENDS       = 105;
    public static final int ERROR_USER_NOT_FOUND        = 106;
    public static final int ERROR_INCORRECT_DATA        = 107;

    @Override
    public void insert(User user, AsyncExecutable exec) {
        this.objData = user;
        this.send(ADD_USER, "add_user", new String[]{"user","img"}, exec);
    }

    @Override
    public void update(User user, AsyncExecutable exec) {
        this.objData = user;
        this.send(UPDATE_USER, "update_user", new String[]{"user", "img"}, exec);
    }

    @Override
    public void remove(int id, AsyncExecutable exec) {
        this.send(REMOVE_USER, "remove_user", new String[]{"id"}, new String[]{String.valueOf(id)}, exec);
    }

    @Override
    public void findById(int id, User responseReference, AsyncExecutable exec) {
        this.objData = responseReference;
        this.request(FIND_USER_BY_ID, "find_user_by_id", new String[]{"id"}, new String[]{String.valueOf(id)}, exec);
    }

    @Override
    public void findAll(ArrayList<User> responseReference, AsyncExecutable exec) {
        this.listData = responseReference;
        this.request(FIND_ALL_USERS, "find_all_users", null, exec);
    }

    @Override
    public void findBlock(int position, int length, ArrayList<User> responseReference, AsyncExecutable exec) {
        this.listData = responseReference;
        this.request(FIND_BLOCK_USERS, "find_block_users", new String[]{"position", "length"},
                new String[]{String.valueOf(position), String.valueOf(length)}, exec);
    }

    public void findByLogin(String mail, String password, User responseReference, AsyncExecutable exec){
        this.objData = responseReference;
        this.request(FIND_BY_LOGIN, "find_by_login",new String[]{"mail","password"},new String[]{mail, password}, exec);
    }

    public void retrievePassword(String mail, AsyncExecutable exec){
        this.send(RETRIEVE_PASSWORD, "retrieve_password",new String[]{"mail"},new String[]{mail},exec);
    }

    public void findByMailOrName(String mailOrName, ArrayList<User> responseReference, AsyncExecutable exec){
        this.listData = responseReference;
        this.request(FIND_BY_NAME_OR_MAIL, "find_by_mail_or_name",new String[]{"str"},new String[]{mailOrName},exec);
    }

    public void findBlockByMailOrName(int position, int length, String mailOrName, ArrayList<User> responseReference, AsyncExecutable exec){
        this.listData = responseReference;
        this.request(FIND_BLOCK_BY_NAME_OR_MAIL, "find_block_by_mail_or_name",new String[]{"str","position","length"},
                new String[]{mailOrName,String.valueOf(position),String.valueOf(length)},exec);
    }

    public void findByMail(String mail, User responseReference, AsyncExecutable exec){
        this.objData = responseReference;
        this.request(FIND_USER_BY_MAIL, "find_user_by_mail", new String[]{"mail"}, new String[]{mail}, exec);
    }

    public void findAllEvents(User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_ALL_EVENTS, "find_all_events_by_user", new String[]{"id"}, new String[]{String.valueOf(user.getId())}, exec);
    }

    public void findEventsConfirmed(User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_EVENTS_CONFIRMED, "find_events_confirmed_by_user",
                new String[]{"id"}, new String[]{String.valueOf(user.getId())}, exec);
    }

    public void findEventsInvited(User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_EVENTS_INVITED, "find_events_invited_by_user",
                new String[]{"id"}, new String[]{String.valueOf(user.getId())}, exec);
    }

    public void findHistoric(User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_EVENTS_HISTORIC, "find_events_historic_by_user",
                new String[]{"id"}, new String[]{String.valueOf(user.getId())}, exec);
    }

    public void findBlockEvents(int position, int length, User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_BLOCK_EVENTS, "find_block_events_by_user",new String[]{"id","position","length"},
                new String[]{String.valueOf(user.getId()),String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findBlockEventsConfirmed(int position, int length, User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_BLOCK_EVENTS_CONFIRMED, "find_block_events_confirmed_by_user",new String[]{"id","confirmed","position","length"},
                new String[]{String.valueOf(user.getId()),String.valueOf(1),String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findBlockEventsInvited(int position, int length, User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_BLOCK_EVENTS_INVITED, "find_block_events_invited_by_user",new String[]{"id","confirmed","position","length"},
                new String[]{String.valueOf(user.getId()),String.valueOf(0),String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findBlockHistoric(int position, int length, User user, AsyncExecutable exec){
        this.objData = user;
        this.request(FIND_BLOCK_EVENTS_HISTORIC, "find_block_events_historic_by_user",new String[]{"id","position","length"},
                new String[]{String.valueOf(user.getId()),String.valueOf(position),String.valueOf(length)}, exec);
    }

    public void findFriends(User user, AsyncExecutable exec){
        this.listData = user.getFriends();
        this.request(FIND_FRIENDS, "find_friends", new String[]{"user_id"}, new String[]{String.valueOf(user.getId())}, exec);
    }

    public void findBlockFriends(User user, int position, int length, AsyncExecutable exec){
        this.listData = user.getFriends();
        this.request(FIND_FRIENDS, "find_block_friends", new String[]{"user_id", "position", "length"},
                new String[]{String.valueOf(user.getId()), String.valueOf(position), String.valueOf(length)}, exec);
    }

    public void removeFriend(User user, User oldFriend, AsyncExecutable exec){
        this.objData = oldFriend;
        this.listData = user.getFriends();
        this.send(REMOVE_FRIEND, "remove_friend", new String[]{"user_id", "friend_id"},
                new String[]{String.valueOf(user.getId()), String.valueOf(oldFriend.getId())}, exec);
    }

    public void addFriend(User user, User newFriend, AsyncExecutable exec){
        this.objData = newFriend;
        this.listData = user.getFriends();
        this.send(ADD_FRIEND, "add_friend", new String[]{"user_id", "friend_id"},
                new String[]{String.valueOf(user.getId()), String.valueOf(newFriend.getId())}, exec);
    }

    public void findByMail(String mail, User responseReference) {
        findByMail(mail, responseReference, null);
    }

    public void findAllEvents(User user) {
        findAllEvents(user, null);
    }

    public void findEventsConfirmed(User user){
        findEventsConfirmed(user, null);
    }

    public void findEventsInvited(User user){
        findEventsInvited(user, null);
    }

    public void findHistoric(User user){
        findHistoric(user, null);
    }

    public void findBlockEventsConfirmed(int position, int length, User user){
        findBlockEventsConfirmed(position, length, user, null);
    }

    public void findBlockEvents(int position, int length, User user){
        findBlockEvents(position, length, user, null);
    }

    public void findBlockEventsInvited(int position, int length, User user){
        findBlockEventsInvited(position, length, user, null);
    }

    public void findBlockHistoric(int position, int length, User user){
        findBlockHistoric(position, length, user, null);
    }

    public void findFriends(User user){
        findFriends(user, null);
    }

    public void findBlockFriends(User user, int position, int length){
        findBlockFriends(user, position, length, null);
    }

    public void removeFriend(User user, User oldFriend){
        removeFriend(user, oldFriend, null);
    }

    public void addFriend(User user, User newFriend){
        addFriend(user, newFriend, null);
    }

    public void findByMailOrName(String mailOrName, ArrayList<User> responseReference){
        findByMailOrName(mailOrName, responseReference, null);
    }

    public void findBlockByMailOrName(int position, int length, String mailOrName, ArrayList<User> responseReference){
        findBlockByMailOrName(position, length, mailOrName, responseReference, null);
    }

    public void findByLogin(String mail, String password, User responseReference){
        findByLogin(mail, password, responseReference, null);
    }

    public void retrievePassword(String mail){
        retrievePassword(mail, null);
    }

    @Override
    public void putParams(int option, String[] params) {
        switch (option){
            case ADD_USER:
            case UPDATE_USER:
                params[0] = objData.toJson();
                params[1] = ImgSerializer.serialize(objData.getImage());
                try {
                    params[1] = URLEncoder.encode(params[1], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

     @Override
     public void onResponse(int option, InputStream in) {
         switch (option){
             case ADD_USER:
                 Integer res[] = JsonParser.toInts(in);
                 try {
                     ServerInfo.RESPONSE_CODE = res[0];
                     objData.setId(res[1]);
                 } catch (NullPointerException | ArrayIndexOutOfBoundsException e){
                     e.printStackTrace();
                     ServerInfo.RESPONSE_CODE = ServerInfo.ERROR_UNKNOWN;
                 }
                 break;
             case FIND_USER_BY_ID:
             case FIND_USER_BY_MAIL:
                 User u = JsonParser.toUser(in);
                 if(u!=null) {
                     objData.copy(u);
                     ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 } else ServerInfo.RESPONSE_CODE = ERROR_USER_NOT_FOUND;
                 break;
             case FIND_BLOCK_USERS:
             case FIND_ALL_USERS:
             case FIND_FRIENDS:
             case FIND_BLOCK_FRIENDS:
             case FIND_BLOCK_BY_NAME_OR_MAIL:
             case FIND_BY_NAME_OR_MAIL:
                 listData.addAll(JsonParser.toUsers(in));
                 ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 break;
             case FIND_EVENTS_CONFIRMED:
             case FIND_BLOCK_EVENTS_CONFIRMED:
                 this.objData.getConfirmedEvents().clear();
             case FIND_ALL_EVENTS:
             case FIND_BLOCK_EVENTS:
                 objData.getConfirmedEvents().addAll(JsonParser.toEvents(in));
                 ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 break;
             case FIND_BLOCK_EVENTS_INVITED:
             case FIND_EVENTS_INVITED:
                 objData.getInvited().clear();
                 objData.getInvited().addAll(JsonParser.toEvents(in));
                 ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 break;
             case FIND_BLOCK_EVENTS_HISTORIC:
             case FIND_EVENTS_HISTORIC:
                 objData.getHistoric().clear();
                 objData.getHistoric().addAll(JsonParser.toEvents(in));
                 ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 break;
             case UPDATE_USER:
                 ServerInfo.RESPONSE_CODE = JsonParser.toInt(in);
                 break;
             case RETRIEVE_PASSWORD:
                 ServerInfo.RESPONSE_CODE = (JsonParser.toInt(in)==0) ? ServerInfo.ERROR_UNKNOWN : ServerInfo.RESPONSE_OK;
                 break;
             case ADD_FRIEND:
                 int aux = JsonParser.toInt(in);
                 ServerInfo.RESPONSE_CODE = (aux==0) ? ERROR_ALREADY_FRIENDS : aux;
                 if(ServerInfo.RESPONSE_CODE==ServerInfo.RESPONSE_OK) listData.add(objData);
                 break;
             case REMOVE_FRIEND:
                 ServerInfo.RESPONSE_CODE = (JsonParser.toInt(in)==0) ? ERROR_REMOVE_FRIEND : ServerInfo.RESPONSE_OK;
                 if(ServerInfo.RESPONSE_CODE==ServerInfo.RESPONSE_OK) listData.remove(objData);
                 break;
             case REMOVE_USER:
                 ServerInfo.RESPONSE_CODE = (JsonParser.toInt(in)==0) ? ERROR_REMOVE_USER : ServerInfo.RESPONSE_OK;
                 break;
             case FIND_BY_LOGIN:
                 User uu = JsonParser.toUser(in);
                 if(uu==null) ServerInfo.RESPONSE_CODE = ERROR_INCORRECT_DATA;
                 else {
                     this.objData.copy(uu);
                     ServerInfo.RESPONSE_CODE = ServerInfo.RESPONSE_OK;
                 }
                 break;
             default:
                 ServerInfo.RESPONSE_CODE = ServerInfo.ERROR_UNKNOWN;
         }
    }
}
