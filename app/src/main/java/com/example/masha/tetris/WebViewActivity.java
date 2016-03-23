package com.example.masha.tetris;

 import android.app.Activity;
 import android.content.Intent;
 import android.net.Uri;
 import android.os.Bundle;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.webkit.WebView;
 import android.webkit.WebViewClient;


public class WebViewActivity extends Activity {

    private WebView webView;

    public static String EXTRA_URL = "extra_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        final String url = this.getIntent().getStringExtra(EXTRA_URL);

        if(url == null) {
            finish();
        }

        webView = (WebView) findViewById(R.id.weView);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if(url.contains(getResources().getString(R.string.twitter_callback))) {
//
//                Uri uri = Uri.parse(url);
//
//                String verifier = uri.getQueryParameter(getString(R.string.twitter_oauth_verifier));
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra(getString(R.string.twitter_oauth_verifier), verifier);
//                setResult(RESULT_OK, resultIntent);
//
//                finish();
//                return true;
//            }
            return false;
        }
    }
}