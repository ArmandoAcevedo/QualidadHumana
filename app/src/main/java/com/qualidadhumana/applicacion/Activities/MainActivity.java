package com.qualidadhumana.applicacion.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.FragmentosNosotros.NosotrosVistas;
import com.qualidadhumana.applicacion.Fragments.FragmentosIniciales.NosotrosFragment;
import com.qualidadhumana.applicacion.Fragments.MenuInicialFragment;
import com.qualidadhumana.applicacion.R;
import com.qualidadhumana.applicacion.helper.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements
        NosotrosVistas.OnNosotrosVistas,
        NosotrosFragment.OnNosotrosFragment {

    private Toolbar toolbar;

    private SessionManager session;

//    private static final String host = "api.linkedin.com";
//    private static final String url = "https://" + host
//            + "/v1/people/~:" +
//            "(email-address,formatted-name,phone-numbers,picture-urls::(original))";
//
//    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Session class instance
        session = new SessionManager(getApplicationContext());

//        progress= new ProgressDialog(this);
//        progress.setMessage("Retrieve data...");
//        progress.setCanceledOnTouchOutside(false);
//        progress.show();

//        Bundle extras = getIntent().getExtras();

        MenuInicialFragment fragment = new MenuInicialFragment();
//        fragment.setArguments(extras);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        String mode = user.get(SessionManager.KEY_MODE);

        getSupportFragmentManager().beginTransaction().add(R.id.conteiner, fragment).commit();

//        linkededinApiHelper();
    }


    static public class MyWebViewFragment extends Fragment {

        public WebView webView;

        private ProgressBar progressBar;

        private String[] links = {
                "http://qualidadhumana.com/index.php/empleos",
                "http://www.qualidadhumana.com/campusvirtual",
                "http://buscochamba.qualidadhumana.com",
                "http://qualidadhumana.com/index.php/contactenos"
        };


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.web_view, container, false);

            progressBar = view.findViewById(R.id.progressBar);

            webView = view.findViewById(R.id.webview);
            WebSettings webSettings = webView.getSettings();

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(android.webkit.WebView view, int progress) {
                    progressBar.setProgress(progress);

                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                    }
                }

            });

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });

            webSettings.setJavaScriptEnabled(true);
            int idWebs = getArguments().getInt("idWebs");
            webView.loadUrl(links[idWebs]);

            return view;

        }

        public WebView getWebView() {
            return webView;
        }
    }

    @Override
    public void onBackPressed() {

        WebView webView = null;

        try {
            webView = ((MyWebViewFragment) getSupportFragmentManager().findFragmentById(R.id.conteiner)).getWebView();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (webView != null) {
            webView = ((MyWebViewFragment) getSupportFragmentManager().findFragmentById(R.id.conteiner)).getWebView();

            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                webView.destroy();
                super.onBackPressed();
            }
        } else if (0 == getSupportFragmentManager().getBackStackEntryCount()) {
            finish();
            ActivityCompat.finishAffinity(this);
        } else
            super.onBackPressed();


    }

    @Override
    public void onCambiarSubtitulo(String subtitulo) {
        toolbar.setSubtitle(subtitulo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, MultipleLogins.class);
            intent.putExtra("isClear", true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

//    public void linkededinApiHelper() {
//        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
//        apiHelper.getRequest(MainActivity.this, url, new ApiListener() {
//            @Override
//            public void onApiSuccess(ApiResponse result) {
//                try {
//                    showResult(result.getResponseDataAsJson());
//                    progress.dismiss();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onApiError(LIApiError error) {
//
//            }
//        });
//    }

//    public void showResult(JSONObject response) {
//
//        try {
//            String emailAddress = response.get("emailAddress").toString();
//            String formattedName = response.get("formattedName").toString();
//
//            Toast.makeText(this, emailAddress + "\n" + formattedName, Toast.LENGTH_SHORT).show();
//
////            Picasso.with(this).load(response.getString("pictureUrl"))
////                    .into(profile_picture);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
