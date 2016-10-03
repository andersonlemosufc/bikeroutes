package es.upv.sdm.labs.bikeroutes.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class ImgSerializer implements Serializable{

    public static String serialize(Bitmap bitmap){
        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, ba);
            byte[] b = ba.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (NullPointerException e){
            return "";
        }
    }

    public static Bitmap deserialize(String s){
        try {
            byte[] b = Base64.decode(s, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public static byte[] toByteArray(Bitmap bitmap){
        if(bitmap==null) return new byte[0];
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, ba);
        return ba.toByteArray();
    }

    public static Bitmap fromByteArray(byte[] b){
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

}
