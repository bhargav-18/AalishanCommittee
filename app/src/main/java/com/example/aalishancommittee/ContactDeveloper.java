package com.example.aalishancommittee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactDeveloper extends AppCompatActivity {

    ImageView ivCallDeveloper, ivMailDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developer);

        ivCallDeveloper = findViewById(R.id.ivCallDeveloper);
        ivMailDeveloper = findViewById(R.id.ivMailDeveloper);

        ivCallDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:9687436512";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        ivMailDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] address = new String[]{"bhargavjoshi1811@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Aalishan Committee app related mail");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}