package com.example.aalishancommittee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.File;

public class FlatInfo extends AppCompatActivity {

    TextView tvOwnerName, tvOwnerNumber, tvFlatType, tvLiveInFlat, tvMaintenance, tvTenantName, tvTenantNumber, tvFlatNoInfo, tvTenantNameH, tvTenantNumberH;
    Button btnAddMaintenance, btnAddYearMaintenance, btnManualMaintenance;
    ImageView ivCall, ivFlatEdit, ivCallTenant, ivMsg, ivWp, ivMsgTenant, ivWpTenant;
    EditText etAddMaintenance;

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvOwnerNumber = findViewById(R.id.tvOwnerNumber);
        tvFlatType = findViewById(R.id.tvFlatType);
        tvLiveInFlat = findViewById(R.id.tvLiveInFlat);
        tvMaintenance = findViewById(R.id.tvMaintenance);
        tvTenantName = findViewById(R.id.tvTenantName);
        tvTenantNumber = findViewById(R.id.tvTenantNumber);
        tvFlatNoInfo = findViewById(R.id.tvFlatNoInfo);
        tvTenantNameH = findViewById(R.id.tvTenantNameH);
        tvTenantNumberH = findViewById(R.id.tvTenantNumberH);
        btnAddMaintenance = findViewById(R.id.btnAddMaintenance);
        btnAddYearMaintenance = findViewById(R.id.btnAddYearMaintenance);
        ivCall = findViewById(R.id.ivCall);
        ivCallTenant = findViewById(R.id.ivCallTenant);
        ivFlatEdit = findViewById(R.id.ivFlatEdit);
        ivMsg = findViewById(R.id.ivMsg);
        ivWp = findViewById(R.id.ivWp);
        ivMsgTenant = findViewById(R.id.ivMsgTenant);
        ivWpTenant = findViewById(R.id.ivWpTenant);
        etAddMaintenance = findViewById(R.id.etAddMaintenance);
        btnManualMaintenance = findViewById(R.id.btnManualMaintenance);

        tvTenantName.setVisibility(View.GONE);
        tvTenantNameH.setVisibility(View.GONE);
        tvTenantNumber.setVisibility(View.GONE);
        tvTenantNumberH.setVisibility(View.GONE);
        ivCallTenant.setVisibility(View.GONE);
        ivMsgTenant.setVisibility(View.GONE);
        ivWpTenant.setVisibility(View.GONE);

        final int index = getIntent().getIntExtra("index", 0);

        tvOwnerName.setText(ApplicationClass.flats.get(index).getOwnerName());
        tvOwnerNumber.setText(ApplicationClass.flats.get(index).getOwnerNumber());
        tvFlatType.setText(ApplicationClass.flats.get(index).getFlatType());
        tvLiveInFlat.setText(ApplicationClass.flats.get(index).getLivingInHouse());
        tvTenantName.setText(ApplicationClass.flats.get(index).getTenantName());
        tvTenantNumber.setText(ApplicationClass.flats.get(index).getTenantNumber());
        String maintenance = ApplicationClass.flats.get(index).getLastMaintenanceMonth() + " " +
                ApplicationClass.flats.get(index).getLastMaintenanceYear();
        tvMaintenance.setText(maintenance);
        tvFlatNoInfo.setText(ApplicationClass.flats.get(index).getFlatNo());

        if(tvLiveInFlat.getText().toString().trim().equals("Tenant"))
        {
            tvTenantName.setVisibility(View.VISIBLE);
            tvTenantNumber.setVisibility(View.VISIBLE);
            ivCallTenant.setVisibility(View.VISIBLE);
            ivMsgTenant.setVisibility(View.VISIBLE);
            ivWpTenant.setVisibility(View.VISIBLE);
            tvTenantNameH.setVisibility(View.VISIBLE);
            tvTenantNumberH.setVisibility(View.VISIBLE);
        }

        btnManualMaintenance.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(etAddMaintenance.getText().toString().isEmpty())
                {
                    Toast.makeText(FlatInfo.this, "Please Enter the amount to add", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(etAddMaintenance.getText().toString().trim()) % 300 != 0)
                {
                    Toast.makeText(FlatInfo.this, "Please enter correct amount. Amount multiple of 300", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int maintenance = Integer.parseInt(etAddMaintenance.getText().toString().trim()) / 300;

                    if(etAddMaintenance.getText().toString().trim().equals("3300"))
                    {
                        for (int i = 1; i <= maintenance + 1; i++) {
                            addMaintenanceOneMonth(index);
                        }

                        showProgress(true);
                        tvLoad.setText("Updating Maintenance... Please wait...");
                        Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void handleResponse(Flats response) {
                                tvMaintenance.setText(ApplicationClass.flats.get(index).getLastMaintenanceMonth() + " " +
                                        ApplicationClass.flats.get(index).getLastMaintenanceYear());
                                Toast.makeText(FlatInfo.this, "Maintenance of " + maintenance + " months added!", Toast.LENGTH_SHORT).show();
                                etAddMaintenance.setText("");
                                showProgress(false);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                etAddMaintenance.setText("");
                                showProgress(false);
                            }
                        });

                    }

                    else {
                        for (int i = 1; i <= maintenance; i++) {
                            addMaintenanceOneMonth(index);
                        }

                        showProgress(true);
                        tvLoad.setText("Updating Maintenance... Please wait...");
                        Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void handleResponse(Flats response) {
                                tvMaintenance.setText(ApplicationClass.flats.get(index).getLastMaintenanceMonth() + " " +
                                        ApplicationClass.flats.get(index).getLastMaintenanceYear());
                                Toast.makeText(FlatInfo.this, "Maintenance of " + maintenance + " months added!", Toast.LENGTH_SHORT).show();
                                etAddMaintenance.setText("");
                                showProgress(false);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                etAddMaintenance.setText("");
                                showProgress(false);
                            }
                        });

                    }
                }
            }
        });

        ivMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ApplicationClass.flats.get(index).getOwnerNumber();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });

        ivWp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = ApplicationClass.flats.get(index).getOwnerNumber();
                Uri uri = Uri.parse("smsto:" + smsNumber);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        });

        ivMsgTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ApplicationClass.flats.get(index).getTenantNumber();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            }
        });

        ivWpTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = ApplicationClass.flats.get(index).getTenantNumber();
                Uri uri = Uri.parse("smsto:" + smsNumber);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + ApplicationClass.flats.get(index).getOwnerNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        ivCallTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + ApplicationClass.flats.get(index).getTenantNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        ivFlatEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(FlatInfo.this);
                dialog.setContentView(R.layout.edit_data);

                EditText etEditOwnerName, etEditOwnerNumber, etEditTenantName, etEditTenantNumber;
                RadioGroup radioGroup;
                RadioButton rbOwner, rbTenant, rbEmpty;
                Button btnSave;

                etEditOwnerName = dialog.findViewById(R.id.etEditOwnerName);
                etEditOwnerNumber = dialog.findViewById(R.id.etEditOwnerNumber);
                etEditTenantName = dialog.findViewById(R.id.etEditTenantName);
                etEditTenantNumber = dialog.findViewById(R.id.etEditTenantNumber);
                radioGroup = dialog.findViewById(R.id.radioGroup);
                rbOwner = dialog.findViewById(R.id.rbOwner);
                rbTenant = dialog.findViewById(R.id.rbTenant);
                rbEmpty = dialog.findViewById(R.id.rbEmpty);

                btnSave = dialog.findViewById(R.id.btnSave);

                etEditOwnerName.setText(ApplicationClass.flats.get(index).getOwnerName());
                etEditOwnerNumber.setText(ApplicationClass.flats.get(index).getOwnerNumber());
                etEditTenantName.setText(ApplicationClass.flats.get(index).getTenantName());
                etEditTenantNumber.setText(ApplicationClass.flats.get(index).getTenantNumber());

                if(ApplicationClass.flats.get(index).getLivingInHouse().trim().equals("Owner"))
                {
                    rbOwner.setChecked(true);
                    rbTenant.setChecked(false);
                    rbEmpty.setChecked(false);
                }
                else if(ApplicationClass.flats.get(index).getLivingInHouse().trim().equals("Empty"))
                {
                    rbOwner.setChecked(false);
                    rbTenant.setChecked(false);
                    rbEmpty.setChecked(true);
                }
                else
                {
                    rbOwner.setChecked(false);
                    rbTenant.setChecked(true);
                    rbEmpty.setChecked(false);
                    etEditTenantName.setVisibility(View.VISIBLE);
                    etEditTenantNumber.setVisibility(View.VISIBLE);
                }

                dialog.show();

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId == R.id.rbTenant)
                        {
                            etEditTenantName.setVisibility(View.VISIBLE);
                            etEditTenantNumber.setVisibility(View.VISIBLE);
                        }
                        else if(checkedId == R.id.rbEmpty)
                        {
                            etEditTenantName.setVisibility(View.GONE);
                            etEditTenantNumber.setVisibility(View.GONE);
                        }
                        else if(checkedId == R.id.rbOwner)
                        {
                            etEditTenantName.setVisibility(View.GONE);
                            etEditTenantNumber.setVisibility(View.GONE);
                        }

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        if(radioGroup.getCheckedRadioButtonId() == R.id.rbOwner)
                        {
                            if(etEditOwnerName.getText().toString().isEmpty() || etEditOwnerNumber.getText().toString().isEmpty())
                            {
                                Toast.makeText(FlatInfo.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ApplicationClass.flats.get(index).setOwnerName(etEditOwnerName.getText().toString().trim());
                                ApplicationClass.flats.get(index).setOwnerNumber(etEditOwnerNumber.getText().toString().trim());
                                ApplicationClass.flats.get(index).setLivingInHouse("Owner");
                                ApplicationClass.flats.get(index).setTenantName("");
                                ApplicationClass.flats.get(index).setTenantNumber("");

                                dialog.dismiss();
                                showProgress(true);
                                tvLoad.setText("Updating Flat Info... Please wait...");
                                Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                                    @Override
                                    public void handleResponse(Flats response) {
                                        tvOwnerNumber.setText(ApplicationClass.flats.get(index).getOwnerNumber());
                                        tvOwnerName.setText(ApplicationClass.flats.get(index).getOwnerName());
                                        tvLiveInFlat.setText(ApplicationClass.flats.get(index).getLivingInHouse());
                                        tvTenantName.setVisibility(View.GONE);
                                        tvTenantNameH.setVisibility(View.GONE);
                                        tvTenantNumber.setVisibility(View.GONE);
                                        tvTenantNumberH.setVisibility(View.GONE);
                                        ivCallTenant.setVisibility(View.GONE);
                                        ivMsgTenant.setVisibility(View.GONE);
                                        ivWpTenant.setVisibility(View.GONE);
                                        Toast.makeText(FlatInfo.this, "Flat info successfully updated", Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                            }
                        }
                        else if(radioGroup.getCheckedRadioButtonId() == R.id.rbEmpty)
                        {
                            if(etEditOwnerName.getText().toString().isEmpty() || etEditOwnerNumber.getText().toString().isEmpty())
                            {
                                Toast.makeText(FlatInfo.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ApplicationClass.flats.get(index).setOwnerName(etEditOwnerName.getText().toString().trim());
                                ApplicationClass.flats.get(index).setOwnerNumber(etEditOwnerNumber.getText().toString().trim());
                                ApplicationClass.flats.get(index).setLivingInHouse("Empty");
                                ApplicationClass.flats.get(index).setTenantName("");
                                ApplicationClass.flats.get(index).setTenantNumber("");

                                dialog.dismiss();
                                showProgress(true);
                                tvLoad.setText("Updating Flat Info... Please wait...");
                                Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                                    @Override
                                    public void handleResponse(Flats response) {
                                        tvOwnerNumber.setText(ApplicationClass.flats.get(index).getOwnerNumber());
                                        tvOwnerName.setText(ApplicationClass.flats.get(index).getOwnerName());
                                        tvLiveInFlat.setText(ApplicationClass.flats.get(index).getLivingInHouse());
                                        tvTenantName.setVisibility(View.GONE);
                                        tvTenantNameH.setVisibility(View.GONE);
                                        tvTenantNumber.setVisibility(View.GONE);
                                        tvTenantNumberH.setVisibility(View.GONE);
                                        ivCallTenant.setVisibility(View.GONE);
                                        ivMsgTenant.setVisibility(View.GONE);
                                        ivWpTenant.setVisibility(View.GONE);
                                        Toast.makeText(FlatInfo.this, "Flat info successfully updated", Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                            }
                        }
                        else if(radioGroup.getCheckedRadioButtonId() == R.id.rbTenant)
                        {
                            if(etEditOwnerName.getText().toString().isEmpty() || etEditOwnerNumber.getText().toString().isEmpty() ||
                                etEditTenantName.getText().toString().isEmpty() || etEditTenantNumber.getText().toString().isEmpty())
                            {
                                Toast.makeText(FlatInfo.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                ApplicationClass.flats.get(index).setOwnerName(etEditOwnerName.getText().toString().trim());
                                ApplicationClass.flats.get(index).setOwnerNumber(etEditOwnerNumber.getText().toString().trim());
                                ApplicationClass.flats.get(index).setLivingInHouse("Tenant");
                                ApplicationClass.flats.get(index).setTenantName(etEditTenantName.getText().toString().trim());
                                ApplicationClass.flats.get(index).setTenantNumber(etEditTenantNumber.getText().toString().trim());

                                dialog.dismiss();
                                showProgress(true);
                                tvLoad.setText("Updating Flat Info... Please wait...");
                                Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                                    @Override
                                    public void handleResponse(Flats response) {
                                        tvOwnerName.setText(ApplicationClass.flats.get(index).getOwnerName());
                                        tvOwnerNumber.setText(ApplicationClass.flats.get(index).getOwnerNumber());
                                        tvTenantName.setText(ApplicationClass.flats.get(index).getTenantName());
                                        tvTenantNumber.setText(ApplicationClass.flats.get(index).getTenantNumber());
                                        tvLiveInFlat.setText(ApplicationClass.flats.get(index).getLivingInHouse());
                                        tvTenantName.setVisibility(View.VISIBLE);
                                        tvTenantNumber.setVisibility(View.VISIBLE);
                                        ivCallTenant.setVisibility(View.VISIBLE);
                                        ivMsgTenant.setVisibility(View.VISIBLE);
                                        ivWpTenant.setVisibility(View.VISIBLE);
                                        tvTenantNameH.setVisibility(View.VISIBLE);
                                        tvTenantNumberH.setVisibility(View.VISIBLE);
                                        Toast.makeText(FlatInfo.this, "Flat info successfully updated", Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                            }
                        }
                        else
                        {
                            Toast.makeText(FlatInfo.this, "Please select who is living in house", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });

        btnAddYearMaintenance.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String month = ApplicationClass.flats.get(index).getLastMaintenanceMonth();
                int year = Integer.parseInt(ApplicationClass.flats.get(index).getLastMaintenanceYear());
                year++;

                String year1 = String.valueOf(year);

                ApplicationClass.flats.get(index).setLastMaintenanceMonth(month);
                ApplicationClass.flats.get(index).setLastMaintenanceYear(year1);

                showProgress(true);
                tvLoad.setText("Updating Maintenance... Please wait...");
                Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleResponse(Flats response) {
                        tvMaintenance.setText(ApplicationClass.flats.get(index).getLastMaintenanceMonth() + " " +
                                ApplicationClass.flats.get(index).getLastMaintenanceYear());
                        Toast.makeText(FlatInfo.this, "Maintenance of 1 year added!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });

            }
        });

        btnAddMaintenance.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                addMaintenanceOneMonth(index);
                showProgress(true);
                tvLoad.setText("Updating Maintenance... Please wait...");
                Backendless.Persistence.save(ApplicationClass.flats.get(index), new AsyncCallback<Flats>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleResponse(Flats response) {
                        tvMaintenance.setText(ApplicationClass.flats.get(index).getLastMaintenanceMonth() + " " +
                                ApplicationClass.flats.get(index).getLastMaintenanceYear());
                        Toast.makeText(FlatInfo.this, "Maintenance of 1 month added!", Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });

            }
        });

    }

    private void addMaintenanceOneMonth(int index) {

        String month = ApplicationClass.flats.get(index).getLastMaintenanceMonth();
        int year = Integer.parseInt(ApplicationClass.flats.get(index).getLastMaintenanceYear());
        switch (month)
        {
            case "January":
                month = "February";
                break;
            case "February":
                month = "March";
                break;
            case "March":
                month = "April";
                break;
            case "April":
                month = "May";
                break;
            case "May":
                month = "June";
                break;
            case "June":
                month = "July";
                break;
            case "July":
                month = "August";
                break;
            case "August":
                month = "September";
                break;
            case "September":
                month = "October";
                break;
            case "October":
                month = "November";
                break;
            case "November":
                month = "December";
                break;
            case "December":
                month = "January";
                year++;
                break;
        }

        String year1 = String.valueOf(year);

        ApplicationClass.flats.get(index).setLastMaintenanceMonth(month);
        ApplicationClass.flats.get(index).setLastMaintenanceYear(year1);

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
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.faqs)
        {
            startActivity(new Intent(FlatInfo.this, FAQs.class));
        }
        else if(item.getItemId() == R.id.contactDeveloper)
        {
            startActivity(new Intent(FlatInfo.this, ContactDeveloper.class));
        }
        else if(item.getItemId() == R.id.logout)
        {
            showProgress(true);
            tvLoad.setText("Logging you out... Please wait...");
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void response) {
                    Toast.makeText(FlatInfo.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(FlatInfo.this, Login.class));
                    FlatInfo.this.finish();

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(FlatInfo.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

}