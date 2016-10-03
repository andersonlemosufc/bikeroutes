package es.upv.sdm.labs.bikeroutes.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.PasswordHelper;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etMail;
    EditText etName;
    EditText etPassword;
    EditText etRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etMail = (EditText) findViewById(R.id.etCreateMail);
        etName = (EditText) findViewById(R.id.etCreateName);
        etPassword = (EditText) findViewById(R.id.etCreatePassword);
        etRepeatPassword = (EditText) findViewById(R.id.etRepeatPassword);
    }

    @Override
    protected void onPause() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("create_mail", etMail.getText().toString());
        editor.putString("create_name", etName.getText().toString());
        editor.putString("create_password", etPassword.getText().toString());
        editor.putString("create_repeat", etRepeatPassword.getText().toString());
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        etMail.setText(prefs.getString("create_mail", ""));
        etName.setText(prefs.getString("create_name", ""));
        etPassword.setText(prefs.getString("create_password", ""));
        etRepeatPassword.setText(prefs.getString("create_repeat", ""));
        super.onResume();
    }

    public void createAccount(View view){
        final String mail = etMail.getText().toString();
        final String name = etName.getText().toString();
        final String password = etPassword.getText().toString();
        final String repeatPassword = etRepeatPassword.getText().toString();
        if(mail.isEmpty() || name.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            Toast.makeText(CreateAccountActivity.this, getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
        } else if(!password.equals(repeatPassword)){
            Toast.makeText(CreateAccountActivity.this, getString(R.string.error_password_not_match), Toast.LENGTH_LONG).show();
        } else {
            final UserService us = new UserService();
            us.findByMail(mail, new User(), new PostExecute() {
                @Override
                public void postExecute(int option) {
                    if (ServerInfo.RESPONSE_CODE==UserService.ERROR_USER_NOT_FOUND){
                        User user = new User(name, mail, PasswordHelper.md5(password), null);
                        us.insert(user, new PostExecute() {
                            @Override
                            public void postExecute(int option) {
                                if(ServerInfo.RESPONSE_CODE==ServerInfo.RESPONSE_OK){
                                    Toast.makeText(CreateAccountActivity.this, getString(R.string.registration_completed), Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(CreateAccountActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CreateAccountActivity.this, getString(R.string.error_mail_already_registered), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}


