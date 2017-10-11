package com.qualidadhumana.applicacion.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.qualidadhumana.applicacion.BackgroundTaksHTML;
import com.qualidadhumana.applicacion.R;

import java.util.ArrayList;

/**
 * Created by Armando Acevedo on 3/8/2017.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;


    private Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MultipleLogins.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

//        cargarDatos();

    }

    private void cargarDatos() {

        String url = getString(R.string.inicio);
        String key_html = getString(R.string.key);

        BackgroundTaksHTML backgroundTaksHTML = new BackgroundTaksHTML(new BackgroundTaksHTML.OnTaskExcute() {
            @Override
            public void onParseHTML(ArrayList<String> result) {

                bundle.putStringArrayList("result_inicio", result);

            }

        });

        backgroundTaksHTML.execute(url, key_html);

        final ArrayList<String> enlaces_text = new ArrayList<>();
        enlaces_text.add(getString(R.string.quienes_somos));
        enlaces_text.add(getString(R.string.mision));
        enlaces_text.add(getString(R.string.vision));
        enlaces_text.add(getString(R.string.valores));

        final ArrayList<String> parrafos = new ArrayList<>();

        for (int i = 0; i < enlaces_text.size(); i++) {
            final int finalI = i;
            backgroundTaksHTML = new BackgroundTaksHTML(new BackgroundTaksHTML.OnTaskExcute() {
                @Override
                public void onParseHTML(ArrayList<String> result) {
                    for (int j = 0; j < result.size(); j++) {
                        parrafos.add(result.get(j));
                    }

                    if (finalI == enlaces_text.size() - 1) {
                        bundle.putStringArrayList("parrafos", parrafos);
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                }

            });

            backgroundTaksHTML.execute(enlaces_text.get(i), "p");
        }

    }

}