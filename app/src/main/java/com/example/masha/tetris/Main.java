package com.example.masha.tetris;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import service.MyService;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Main extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private EditText shareEditText;
    private View loginLayout , shareLayout;
    private ProgressDialog pDialog;

    public static double scrh = 0 , scrw = 0 ;

    private static final String MY_SETTINGS = "my_settings" , PREF_NAME = "sample_twitter_pref" , PREF_KEY_OAUTH_TOKEN = "oauth_token" , PREF_KEY_OAUTH_SECRET = "oauth_token_secret" , PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin" , PREF_USER_NAME = "twitter_user_name";
    private String consumerKey = null , consumerSecret = null , callbackUrl = null , oAuthVerifier = null;

    public static final int WEBVIEW_REQUEST_CODE = 100;

    private static Twitter twitter;
    private static RequestToken requestToken;
    private static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startService(new Intent(this, MyService.class));
        super.onCreate(savedInstanceState);

        initTwitterConfigs();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        findViewById(R.id.bttnPlay).setOnClickListener(this);
        findViewById(R.id.bttnTutorial).setOnClickListener(this);
        findViewById(R.id.bttnSettings).setOnClickListener(this);
        findViewById(R.id.bttnTwitter).setOnClickListener(this);
        findViewById(R.id.bttnExit).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);

        scrh = screenSizeH();
        scrw = screenSizeW();

        loginLayout = (LinearLayout) findViewById(R.id.login_layout);
        shareLayout = (LinearLayout) findViewById(R.id.share_layout);

        shareEditText = (EditText) findViewById(R.id.share_text);

        sharedPreferences = getSharedPreferences(PREF_NAME, 0);

        loginLayout.setVisibility(View.VISIBLE);
        shareLayout.setVisibility(View.GONE);

        Uri uri = getIntent().getData();

        if(uri != null && uri.toString().startsWith(callbackUrl)) {

            String verifier = uri.getQueryParameter(oAuthVerifier);

            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                long userId = accessToken.getUserId();
                final User user = twitter.showUser(userId);
                final String username = user.getName();

                saveTwitterInfo(accessToken);

                loginLayout.setVisibility(View.GONE);
                shareLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean hasVisited = sp.getBoolean("hasVisited", false);    // проверяем, первый ли раз открывается программа
        if (!hasVisited) {
            intent = new Intent (this, Start.class);
            startActivity(intent);

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bttnPlay:
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                break;

            case R.id.bttnTutorial:
                intent = new Intent (this, GamePlay.class);
                startActivity(intent);
                break;

            case R.id.bttnSettings:
                intent = new Intent (this, Settings.class);
                startActivity(intent);
                break;

            case R.id.bttnExit:
                finish();
                break;

            case R.id.bttnTwitter:
                loginToTwitter();
                break;

            case R.id.btn_share:
                final String status = shareEditText.getText().toString();

                if(status.trim().length() > 0) {
                    new updateTwitterStatus().execute(status);
                } else {
                    Toast.makeText(this, "Message is empty!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
    }


    public double screenSizeW()
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        return  metricsB.widthPixels;
    }


    public double screenSizeH()
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        return metricsB.heightPixels;
    }


    private void initTwitterConfigs() {
        consumerKey = getString(R.string.twitter_consumer_key);
        consumerSecret = getString(R.string.twitter_consumer_secret);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);
    }


    private void saveTwitterInfo(AccessToken accessToken) {

        long userId = accessToken.getUserId();
        User user;

        try {
            user = twitter.showUser(userId);
            String username = user.getName();

            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }


    private void loginToTwitter() {

        boolean isLoggedIn = sharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

        if(!isLoggedIn) {
            final ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(consumerKey);
            builder.setOAuthConsumerSecret(consumerSecret);

            final Configuration configuration = builder.build();
            final TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter.getOAuthRequestToken(callbackUrl);

                final Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
                startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            loginLayout.setVisibility(View.GONE);
            shareLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK) {
            String verifier = data.getExtras().getString(oAuthVerifier);

            try {
                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

                saveTwitterInfo(accessToken);

                loginLayout.setVisibility(View.GONE);
                shareLayout.setVisibility(View.VISIBLE);

            } catch (Exception e) {e.printStackTrace();}
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    class updateTwitterStatus extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Posting to Twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(String... params) {

            String status = params[0];

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(consumerKey);
                builder.setOAuthConsumerSecret(consumerSecret);

                String access_token = sharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                String acces_token_secret = sharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, acces_token_secret);

                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                StatusUpdate statusUpdate = new StatusUpdate(status);

                twitter4j.Status response = twitter.updateStatus(statusUpdate);

            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            pDialog.dismiss();

            Toast.makeText(Main.this, "Posted to Twitter!", Toast.LENGTH_SHORT);

            shareEditText.setText("");
        }
    }
}