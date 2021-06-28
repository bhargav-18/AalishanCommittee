package com.example.aalishancommittee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class Register extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    EditText etNameRegister, etMailRegister, etPasswordRegister, etReEnter;
    Button btnRegister1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        etNameRegister = findViewById(R.id.etNameRegister);
        etMailRegister = findViewById(R.id.etMailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etReEnter = findViewById(R.id.etReEnter);
        btnRegister1 = findViewById(R.id.btnRegister1);

        btnRegister1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etNameRegister.getText().toString().isEmpty() || etMailRegister.getText().toString().isEmpty() ||
                        etPasswordRegister.getText().toString().isEmpty() || etReEnter.getText().toString().isEmpty())
                {
                    Toast.makeText(Register.this, "Please enter all details!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(etPasswordRegister.getText().toString().trim().equals(etReEnter.getText().toString().trim()))
                    {
                        String name = etNameRegister.getText().toString().trim();
                        String email = etMailRegister.getText().toString().trim();
                        String password = etPasswordRegister.getText().toString().trim();

                        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            BackendlessUser user = new BackendlessUser();
                            user.setEmail(email);
                            user.setPassword(password);
                            user.setProperty("name", name);

                            showProgress(true);
                            tvLoad.setText("Please follow the registration link sent to your email to register successfully...");
                            Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {

                                    showProgress(false);
                                    Toast.makeText(Register.this, "User successfully registered!", Toast.LENGTH_SHORT).show();
                                    Register.this.finish();

                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    Toast.makeText(Register.this, "Error :" + fault.getDetail(), Toast.LENGTH_LONG).show();
                                    showProgress(false);
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Please make sure your both password and confirm password is same!", Toast.LENGTH_LONG).show();
                        etPasswordRegister.setText("");
                        etReEnter.setText("");
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login_register, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.faqs1)
        {
            startActivity(new Intent(Register.this, FAQs.class));
        }
        else if(item.getItemId() == R.id.contactDeveloper1)
        {
            startActivity(new Intent(Register.this, ContactDeveloper.class));
        }

        return super.onOptionsItemSelected(item);
    }

}