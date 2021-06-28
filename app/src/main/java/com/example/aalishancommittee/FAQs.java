package com.example.aalishancommittee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FAQs extends AppCompatActivity {

    TextView tvContactDeveloper, tvContactDeveloper1, tvDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        tvContactDeveloper = findViewById(R.id.tvContactDeveloper);
        tvContactDeveloper1 = findViewById(R.id.tvContactDeveloper1);
        tvDeleteAccount = findViewById(R.id.tvDeleteAccount);

        tvDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] address = new String[]{"bhargavjoshi1811@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Delete account");
                intent.putExtra(Intent.EXTRA_TEXT, "Please take time to explain why you want to delete account");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        tvContactDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FAQs.this, ContactDeveloper.class));
            }
        });

        tvContactDeveloper1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FAQs.this, ContactDeveloper.class));
            }
        });

    }
}