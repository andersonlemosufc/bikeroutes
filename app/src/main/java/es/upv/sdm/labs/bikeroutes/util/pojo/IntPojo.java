package es.upv.sdm.labs.bikeroutes.util.pojo;

/**
 * Created by anderson on 18/04/2016.
 */
public class IntPojo extends AbstractPOJO {

    private Integer[] ints;

    public IntPojo(int tam){
        this.ints = new Integer[tam];
    }

    public IntPojo(Integer ints[]){
        this.ints = ints;
    }

    public IntPojo(){}

    public IntPojo(Integer i){
        this.ints = new Integer[1];
        this.ints[0] = i;
    }

    public Integer getInt(){
        if(this.ints==null || this.ints.length<1) return 0;
        return this.ints[0];
    }

    public Integer[] getInts() {
        return ints;
    }

    public void setInts(Integer[] ints) {
        this.ints = ints;
    }


}
