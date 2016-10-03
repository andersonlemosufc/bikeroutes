package es.upv.sdm.labs.bikeroutes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.services.ServerInfo;
import es.upv.sdm.labs.bikeroutes.services.UserService;
import es.upv.sdm.labs.bikeroutes.util.PasswordHelper;
import es.upv.sdm.labs.bikeroutes.util.async.PostExecute;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etResetPassword = (EditText) findViewById(R.id.etResetPassword);
    }

    public void resetPassword(View view){
        String mail = etResetPassword.getText().toString();
        if(mail.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_empty_fields), Toast.LENGTH_LONG).show();
        } else {
            new UserService().retrievePassword(mail, new PostExecute() {
                @Override
                public void postExecute(int option) {
                    if(ServerInfo.RESPONSE_CODE==ServerInfo.RESPONSE_OK){
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.reset_password), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_user_not_found), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
