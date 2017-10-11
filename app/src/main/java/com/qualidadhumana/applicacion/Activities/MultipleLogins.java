package com.qualidadhumana.applicacion.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaCas;
import android.service.textservice.SpellCheckerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.qualidadhumana.applicacion.R;
import com.qualidadhumana.applicacion.app.AppConfig;
import com.qualidadhumana.applicacion.app.AppController;
import com.qualidadhumana.applicacion.helper.SQLiteHandler;
import com.qualidadhumana.applicacion.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class MultipleLogins extends AppCompatActivity implements View.OnClickListener {

    public static final String PACKAGE = "com.qualidadhumana.applicacion";
    public static final String TAG = MultipleLogins.class.getSimpleName();
    private LoginButton loginButton;
    private CallbackManager manager;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_logins);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.qualidadhumana.applicacion",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

//        PackageInfo info;
//        try {
//            info = getPackageManager().getPackageInfo("com.qualidadhumana.applicacion", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                //String something = new String(Base64.encodeBytes(md.digest()));
//                Log.e("hash key", something);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("no such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("exception", e.toString());
//        }

        initializeControls();

//        authLinkedIn();

        loginFacebook();

        loggedIn();

        if (getIntent().getBooleanExtra("isClear", false)) {

            if (sessionManager.getUserDetails().get(SessionManager.KEY_MODE) != null) {
                String mode = sessionManager.getUserDetails().get(SessionManager.KEY_MODE);
                if (mode.equals(SessionManager.QUALIDAD))
                    sessionManager.logoutUser();
                else if (mode.equals(SessionManager.LINKEDIN))
                    LISessionManager.getInstance(this).clearSession();
                else if (mode.equals(SessionManager.FACEBOOK))
                    LoginManager.getInstance().logOut();
            }

            sessionManager.logoutUser();
        }

//        generateHashkey();
    }

    private void loggedIn() {
        // Check if user is already logged in or not
        if (sessionManager.isLoggedIn() || LISessionManager.getInstance(this).getSession().isValid()) {
            // User is already logged in. Take him to main activity
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginFacebook() {

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                hideDialog();
            }

            @Override
            public void onCancel() {
                hideDialog();
            }

            @Override
            public void onError(FacebookException error) {
                hideDialog();
            }
        });

        ProfileTracker profileTrecker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    hideDialog();
                                    try {
                                        String name = object.getString("name");
                                        String email = object.getString("email");

                                        sessionManager.createLoginSession(name, email, SessionManager.FACEBOOK);
                                        goToMainActivity();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "onCompleted: Error:", e);

                                    }
                                }

                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }
        };

        profileTrecker.startTracking();
    }

    private void authLinkedIn() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
            }
        }, true);
    }

    private void initializeControls() {
        (findViewById(R.id.login_button_linked)).setOnClickListener(this);
        (findViewById(R.id.btnLinkToRegisterScreen)).setOnClickListener(this);
        (findViewById(R.id.btnLogin)).setOnClickListener(this);
        manager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // Session manager
        sessionManager = new SessionManager(getApplicationContext());

//        (findViewById(R.id.logout_btn)).setVisibility(View.GONE);
//        name.setVisibility(View.GONE);
//        imageView.setVisibility(View.GONE);

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                attemptLogin();
                return true;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        manager.onActivityResult(requestCode, resultCode, data);

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);

    }

    // Build the list of member permissions our LinkedIn sessionManager requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    // Authenticate with linkedin and intialize Session.

    public void login(View view) {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

//                ((TextView) findViewById(R.id.display_name))
//                        .setText(Base64.encodeToString(md.digest(),
//                                Base64.NO_WRAP));
                Log.d(TAG, "generateHashkey: " + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button_linked:
                handleLogin();
                break;
            case R.id.login_button:
                pDialog.setMessage("Iniciando sesión ...");
                showDialog();
                break;
            case R.id.btnLogin:
                attemptLogin();
                break;
            case R.id.btnLinkToRegisterScreen:
                goToRegister();
                break;
        }
    }

    private void goToRegister() {
        Intent i = new Intent(this,
                RegisterActivity.class);
        startActivity(i);
        finish();
    }


    private void handleLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.e(TAG, "onAuthError: " + error.toString());
            }
        }, true);
    }

    private void fetchPersonalInfo() {
//        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";

        pDialog.setMessage("Iniciando sesión ...");
        showDialog();

        String url = "https://api.linkedin.com/v1/people/~:(first-name,last-name,email-address::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!

                hideDialog();
                JSONObject responseDataAsJson = apiResponse.getResponseDataAsJson();
                try {
                    String firstName = responseDataAsJson.getString("firstName");
                    String lastName = responseDataAsJson.getString("lastName");
//                    String pictureUrl = responseDataAsJson.getString("pictureUrl");
                    String emailAddress = responseDataAsJson.getString("emailAddress");
//                    Glide.with(MultipleLogins.this).load(pictureUrl)
//                            .thumbnail(0.5f)
//                            .crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(imageView);
//                    name.setText(firstName + " " + lastName + "\n" + emailAddress);

//                    Toast.makeText(MultipleLogins.this, firstName + " " + lastName + "\n" + emailAddress, Toast.LENGTH_SHORT).show();

                    sessionManager.createLoginSession(firstName, emailAddress, SessionManager.LINKEDIN);

                    goToMainActivity();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MultipleLogins.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                hideDialog();
                Log.e(TAG, "onApiError: " + liApiError.getMessage());
            }
        });
    }

    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        inputEmail.setError(null);
        inputPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.error_field_required));
            focusView = inputPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.error_field_required));
            focusView = inputEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            inputEmail.setError(getString(R.string.error_invalid_email));
            focusView = inputEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            pDialog.show();
            checkLogin(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Iniciando sesión ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login sessionManager
                        sessionManager.checkLogin();

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
//                        db.addUser(name, email, uid, created_at);

                        // Launch main activity
                        sessionManager.createLoginSession(name, email, SessionManager.QUALIDAD);
                        goToMainActivity();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
