package es.upv.sdm.labs.bikeroutes.enumerations;


/**
 * Created by anderson on 11/04/2016.
 */
public enum Gender {

    MALE, FEMALE, UNINFORMED;

    public static Gender getGender(String gender){
        if(gender.equals("MALE")) return MALE;
        if(gender.equals("FEMALE")) return FEMALE;
        return UNINFORMED;
    }
}
