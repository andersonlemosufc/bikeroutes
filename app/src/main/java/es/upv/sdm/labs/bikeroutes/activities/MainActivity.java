package es.upv.sdm.labs.bikeroutes.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.Signature;
import java.util.Arrays;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.dao.UserDAO;
import es.upv.sdm.labs.bikeroutes.enumerations.Gender;
import es.upv.sdm.labs.bikeroutes.interfaces.AsyncExecutable;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.PasswordHelper;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

//login page
public class MainActivity extends AppCompatActivity {

    public static final User user = new User();
    LoginButton btLogin;
    CallbackManager callbackManager;
    EditText etMail, etPassword;
    ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int id = prefs.getInt("user_id", 0);
        if(id==0) {
            etMail = (EditText) findViewById(R.id.etMail);
            etPassword = (EditText) findViewById(R.id.etPassword);
            btLogin = (LoginButton) findViewById(R.id.btnFb);
            pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
            pbLogin.setVisibility(View.GONE);
            btLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();
            btLogin.registerCallback(callbackManager, new MyFacebookCallback());
        } else {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }
    }

    public void createAccount(View view) {
        startActivity(new Intent(this, CreateAccountActivity.class));
    }

    public void resetPassword(View view) {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    public void loginButtonClicked(View view){
        String mail = etMail.getText().toString();
        String password = etPassword.getText().toString();
        if(mail.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
        } else {
            final User user = new User();
            new UserService().findByLogin(mail, PasswordHelper.md5(password), user, new AsyncExecutable() {
                @Override
                public void postExecute(int option) {
                    if(ServerInfo.RESPONSE_CODE==UserService.ERROR_INCORRECT_DATA){
                        pbLogin.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, getString(R.string.error_incorrect_data_login), Toast.LENGTH_LONG).show();
                    } else {
                        MainActivity.user.copy(user);
                        login();
                    }
                }

                @Override
                public void preExecute(int option) {
                    pbLogin.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void login(){
        if(user.getMail()==null || user.getMail().isEmpty()) return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", user.getId());
        editor.apply();
        UserDAO dao = new UserDAO(this);
        dao.insert(user);
        dao.close();
        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        finish();
    }

    private class MyFacebookCallback implements FacebookCallback<LoginResult>{
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    getFacebookData(object);
                    LoginManager.getInstance().logOut();
                    final UserService us = new UserService();
                    final User other = new User();
                    us.findByMail(user.getMail(), other, new AsyncExecutable(){
                        @Override
                        public void postExecute(int option) {
                            if(ServerInfo.RESPONSE_CODE==UserService.ERROR_USER_NOT_FOUND){
                                user.setPassword(PasswordHelper.md5(PasswordHelper.generatePassword()));
                                us.insert(user, new PostExecute() {
                                    @Override
                                    public void postExecute(int option) {
                                        login();
                                    }
                                });
                            } else {
                                user.copy(other);
                                login();
                            }
                        }

                        @Override
                        public void preExecute(int option) {
                            pbLogin.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {}

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getApplicationContext(), getString(R.string.facebook_error), Toast.LENGTH_SHORT).show();
        }

        private void getFacebookData(JSONObject object) {
            if (!object.has("email")) return;
            try {
                final String id = object.getString("id");
                Bitmap image = null;
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        try {
                            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
                            Log.i("PIC", profile_pic.toString());
                            user.setImage(BitmapFactory.decodeStream(profile_pic.openConnection().getInputStream()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                String name = "", mail="", gender="";
                if (object.has("first_name"))
                    name += object.getString("first_name");
                if (object.has("last_name"))
                    name += " "+object.getString("last_name");
                if (object.has("email"))
                    mail = object.getString("email");
                if (object.has("gender"))
                    gender = object.getString("gender");
                user.setMail(mail);
                user.setName(name);
                user.setGender(gender.equals("male") ? Gender.MALE : gender.equals("female") ? Gender.FEMALE : Gender.UNINFORMED);
                user.setImage(image);
                t.join();
          } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
