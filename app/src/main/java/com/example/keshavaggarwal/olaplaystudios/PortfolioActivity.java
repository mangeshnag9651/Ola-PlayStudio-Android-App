package com.example.keshavaggarwal.olaplaystudios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import com.example.keshavaggarwal.olaplaystudios.widgets.CustomTextView;

public class PortfolioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("My Portfolio");
        init();
    }

    private void init() {
        CustomTextView tvLinkToCuvora = (CustomTextView) findViewById(R.id.tvLinkToCuvora);
        CustomTextView tvLinkToGithub = (CustomTextView) findViewById(R.id.tvLinkToGithub);

        tvLinkToCuvora.setMovementMethod(LinkMovementMethod.getInstance());
        tvLinkToGithub.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
