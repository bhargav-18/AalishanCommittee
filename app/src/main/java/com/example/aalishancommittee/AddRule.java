package com.example.aalishancommittee;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddRule extends AppCompatActivity {

    EditText etAddTitle, etAddDescription;
    FloatingActionButton fabDone;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rule);

        etAddTitle = findViewById(R.id.etAddTitle);
        etAddDescription = findViewById(R.id.etAddDescription);
        fabDone = findViewById(R.id.fabDone);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        fabDone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(etAddTitle.getText().toString().isEmpty() || etAddDescription.getText().toString().isEmpty())
                {
                    Toast.makeText(AddRule.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String title = etAddTitle.getText().toString().trim();
                    String description = etAddDescription.getText().toString().trim();

                    Rules rules = new Rules();
                    rules.setTitle(title);
                    rules.setDescription(description);
                    rules.setSrNo(String.valueOf(ApplicationClass.rules.size() + 1));

                    showProgress(true);
                    tvLoad.setText("Creating new Rule... Please wait...");

                    Backendless.Persistence.save(rules, new AsyncCallback<Rules>() {
                        @Override
                        public void handleResponse(Rules response) {
                            Toast.makeText(AddRule.this, "New Rule saved successfully!", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            Intent intent = new Intent(AddRule.this, SocietyRules.class);
                            startActivity(intent);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(AddRule.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }

        else
        {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}