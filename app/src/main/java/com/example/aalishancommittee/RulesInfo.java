package com.example.aalishancommittee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RulesInfo extends AppCompatActivity {

    FloatingActionButton fabUpdate, fabDelete;
    EditText etTitleInfo, etDescriptionInfo;
    TextView tvSrNoInfo;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_info);

        int index = getIntent().getIntExtra("index", 0);

        fabUpdate = findViewById(R.id.fabUpdate);
        fabDelete = findViewById(R.id.fabDelete);
        etTitleInfo = findViewById(R.id.etTitleInfo);
        etDescriptionInfo = findViewById(R.id.etDescriptionInfo);
        tvSrNoInfo = findViewById(R.id.tvSrNoInfo);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvSrNoInfo.setText(ApplicationClass.rules.get(index).getSrNo());
        etTitleInfo.setText(ApplicationClass.rules.get(index).getTitle());
        etDescriptionInfo.setText(ApplicationClass.rules.get(index).getDescription());

        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(etTitleInfo.getText().toString().isEmpty() || etDescriptionInfo.getText().toString().isEmpty())
                {
                    Toast.makeText(RulesInfo.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ApplicationClass.rules.get(index).setTitle(etTitleInfo.getText().toString().trim());
                    ApplicationClass.rules.get(index).setDescription(etDescriptionInfo.getText().toString().trim());

                    showProgress(true);
                    tvLoad.setText("Updating Rule... Please wait...");
                    Backendless.Persistence.save(ApplicationClass.rules.get(index), new AsyncCallback<Rules>() {
                        @Override
                        public void handleResponse(Rules response) {
                            Toast.makeText(RulesInfo.this, "Rule successfully updated", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            RulesInfo.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(RulesInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(RulesInfo.this);
                dialog.setMessage("Are you sure you want to delete the rule ?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        tvLoad.setText("Deleting Rule... Please wait...");
                        Backendless.Persistence.of(Rules.class).remove(ApplicationClass.rules.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                ApplicationClass.rules.remove(index);
                                for(int i = 0; i < ApplicationClass.rules.size(); i++)
                                {
                                    ApplicationClass.rules.get(i).setSrNo(String.valueOf(i + 1));
                                    Backendless.Persistence.save(ApplicationClass.rules.get(i), new AsyncCallback<Rules>() {
                                        @Override
                                        public void handleResponse(Rules response) {

                                        }

                                        @Override
                                        public void handleFault(BackendlessFault fault) {
                                            Toast.makeText(RulesInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                showProgress(false);
                                Toast.makeText(RulesInfo.this, "Rule successfully removed!", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                RulesInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(RulesInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.show();
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