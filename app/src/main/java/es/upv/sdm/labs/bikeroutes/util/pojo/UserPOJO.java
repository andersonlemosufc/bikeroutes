package es.upv.sdm.labs.bikeroutes.util.pojo;

import java.util.ArrayList;

import es.upv.sdm.labs.bikeroutes.enumerations.Gender;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.ImgSerializer;

/**
 * Created by anderson on 12/04/2016.
 */
public class UserPOJO extends AbstractPOJO {

    private Users[] users;

    public UserPOJO(){}

    public UserPOJO(User user){
        this.users = new Users[1];
        users[0] = new Users(user);
    }

    public User getUser(){
        if(this.users==null || this.users.length==0) return null;
        return users[0].getUser();
    }

    public ArrayList<User> toUsers(){
        ArrayList<User> res = new ArrayList<User>();
        for (Users u:this.users) {
            res.add(u.getUser());
        }
        return res;
    }

    public Users[] getUsers() {
        return users;
    }

    public void setUsers(Users[] users) {
        this.users = users;
    }

    public class Users{

        public Users(){
            this.image = "";
        }

        public Users(User user){
            this.setId(user.getId());
            this.setName(user.getName());
            this.setDescription(user.getDescription());
            this.setGender(user.getGender().toString());
            this.setPassword(user.getPassword());
            this.setMail(user.getMail());
            this.setImage("");
        }

        private int id;
        private String name;
        private String mail;
        private String password;
        private String description;
        private String gender;
        private String image;

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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public User getUser(){
            User res = new User();
            res.setId(this.getId());
            res.setName(this.getName());
            res.setMail(this.getMail());
            res.setPassword(this.getPassword());
            res.setDescription(this.getDescription());
            res.setGender(Gender.getGender(this.getGender()));
            if(this.getImage()!=null && !this.getImage().isEmpty())res.setImage(ImgSerializer.deserialize(this.getImage()));
            return res;
        }
    }
}
