package es.upv.sdm.labs.bikeroutes.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.dao.UserDAO;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.PasswordHelper;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class MySettingsActivity extends AppCompatActivity {

    private static final int ACTIVITY_RESULT_UPDATE_ACCOUNT = 0;
    Spinner spLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_settings);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLanguage = (Spinner) findViewById(R.id.spLanguages);
        spLanguage.setAdapter(adapter);
        Log.d("LOCALE", Locale.getDefault().getLanguage());
        spLanguage.setSelection(PreferenceManager.getDefaultSharedPreferences(this).getInt("language", localeToInt(Locale.getDefault().getLanguage())));
        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putInt("language", position);
                editor.apply();
                changeLanguage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int localeToInt(String locale){
        switch (locale){
            case "es":
            case "es_ES":
            case "es_US": return 1;
            case "pt_PT":
            case "pt":
            case "pt_BR": return 2;
            case "pl":
            case "pl_PL": return 3;
            case "fi":
            case "fi_FI": return 4;
            case "de":
            case "de_AT":
            case "de_DE":
            case "de_LI":
            case "de_CH": return 5;
            default: return 0;
        }
    }

    private void changeLanguage(int code){
        String l = "en_US";
        switch (code){
            case 1: l = "es_ES";break;
            case 2: l = "pt_BR";break;
            case 3: l = "pl_PL";break;
            case 4: l = "fi_FI";break;
            case 5: l = "de_DE";break;
        }
        Locale locale = new Locale(l);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, null);

        Log.d("LOCALE", Locale.getDefault().getLanguage());
    }

    public void changePasswordButtonClicked(View view){
        LayoutInflater layoutInflater =  LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_change_password, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText etPassword = (EditText) promptView.findViewById(R.id.etPassword);
        final EditText etRepeat = (EditText) promptView.findViewById(R.id.etRepeatPassword);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String password = etPassword.getText().toString();
                        String repeat = etRepeat.getText().toString();
                        if (password.isEmpty() || repeat.isEmpty()) {
                            Toast.makeText(MySettingsActivity.this, getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
                        } else if (!password.equals(repeat)) {
                            Toast.makeText(MySettingsActivity.this, getString(R.string.error_password_not_match), Toast.LENGTH_LONG).show();
                        } else {
                            final UserDAO dao = new UserDAO(getApplicationContext());
                            final User user = dao.findById(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("user_id", 0));
                            user.setPassword(PasswordHelper.md5(password));
                            new UserService().update(user, new PostExecute() {
                                @Override
                                public void postExecute(int option) {
                                    if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {
                                        Toast.makeText(MySettingsActivity.this, getString(R.string.password_update), Toast.LENGTH_LONG).show();
                                        dao.update(user);
                                    } else {
                                        Toast.makeText(MySettingsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            dao.close();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public void changeMailButtonClicked(View view){
        LayoutInflater layoutInflater =  LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.dialog_change_mail, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText etMail = (EditText) promptView.findViewById(R.id.etMail);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String mail = etMail.getText().toString();
                        if (mail.isEmpty()) {
                            Toast.makeText(MySettingsActivity.this, getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
                        } else {
                            final UserDAO dao = new UserDAO(getApplicationContext());
                            final User user = dao.findById(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("user_id", 0));
                            user.setMail(mail);
                            Log.i("Anderson", user.toJson());
                            new UserService().update(user, new PostExecute() {
                                @Override
                                public void postExecute(int option) {

                                    if (ServerInfo.RESPONSE_CODE == ServerInfo.RESPONSE_OK) {
                                        Toast.makeText(MySettingsActivity.this, getString(R.string.mail_update), Toast.LENGTH_LONG).show();
                                        dao.update(user);
                                    } else if (ServerInfo.RESPONSE_CODE == UserService.ERROR_ADD_USER_MAIL) {
                                        Toast.makeText(MySettingsActivity.this, getString(R.string.error_mail_already_registered), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MySettingsActivity.this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            dao.close();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    public void editAccountButtonClicked(View view){
        Intent intent = new Intent(this, EditAccountActivity.class);
        startActivityForResult(intent, ACTIVITY_RESULT_UPDATE_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ACTIVITY_RESULT_UPDATE_ACCOUNT){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("name");
            editor.remove("sex");
            editor.remove("description");
            editor.apply();
        }
    }
}
