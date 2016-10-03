package es.upv.sdm.labs.bikeroutes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import es.upv.sdm.labs.bikeroutes.R;
import es.upv.sdm.labs.bikeroutes.model.User;
import es.upv.sdm.labs.bikeroutes.util.JsonParser;

public class TesteActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CICLO DE VIDA", "ONCREATE"+this.getClass().getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("CICLO DE VIDA", "ONSTOP"+this.getClass().getName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("CICLO DE VIDA", "ONSTART"+this.getClass().getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CICLO DE VIDA", "ONRESUME"+this.getClass().getName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("CICLO DE VIDA", "ONRESTART"+this.getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("CICLO DE VIDA", "ONPAUSE"+this.getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("CICLO DE VIDA", "ONDESTROY"+this.getClass().getName());
    }
}
