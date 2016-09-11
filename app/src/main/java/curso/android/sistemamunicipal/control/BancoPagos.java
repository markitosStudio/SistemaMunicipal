package curso.android.sistemamunicipal.control;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import curso.android.sistemamunicipal.R;

public class BancoPagos extends AppCompatActivity {

    String valor="";
    private WebView webPagoBanco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banco_pagos);

        webPagoBanco=(WebView) findViewById(R.id.webPagoBanco);
        webPagoBanco.setWebViewClient(new MyWebViewClient());
        // Enable Javascript
        WebSettings webSettings = webPagoBanco.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Bundle b = this.getIntent().getExtras();
        valor=b.getString("link");
        webPagoBanco.loadUrl(valor);
    }

    // custom web view client class who extends WebViewClient
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }
    }
}
