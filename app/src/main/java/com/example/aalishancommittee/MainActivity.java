package com.example.aalishancommittee;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    private static final int UNIQUE_CODE = 1;

    ListView lvList;

    FlatsAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File directory = new File(Environment.getExternalStorageDirectory() + "/Aalishan Committee");
        if(!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        lvList = findViewById(R.id.lvList);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FlatInfo.class);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setGroupBy("flatNo");

        showProgress(true);
        tvLoad.setText("Getting all details... Please wait...");

        Backendless.Persistence.of(Flats.class).find(queryBuilder, new AsyncCallback<List<Flats>>() {
            @Override
            public void handleResponse(List<Flats> response) {
                ApplicationClass.flats = response;
                adapter = new FlatsAdapter(MainActivity.this, ApplicationClass.flats);
                lvList.setAdapter(adapter);
                showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "Error: "+ fault.getDetail(), Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UNIQUE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            adapter.notifyDataSetChanged();
        }

    }

    @SuppressLint("ObsoleteSdkInt")
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == UNIQUE_CODE)
        {
            if(grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("This permission is required to export data to excel file, if you deny you will not be able to use that feature")
                            .setTitle("Important permission required");
                    dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermission();
                        }
                    });
                    dialog.setNegativeButton("NO Thanks!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "You will not be able to use some features!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else
                {
                    Toast.makeText(this, "Please accept storage permission to use some features!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.faqs) {
            startActivity(new Intent(MainActivity.this, FAQs.class));
        } else if (item.getItemId() == R.id.contactDeveloper) {
            startActivity(new Intent(MainActivity.this, ContactDeveloper.class));
        } else if (item.getItemId() == R.id.createExcel) {
            File filePath = new File(Environment.getExternalStorageDirectory() + "/Aalishan Committee/Data.xls");

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet hssfSheet = hssfWorkbook.createSheet("Data");

                HSSFRow hssfRow = hssfSheet.createRow(0);

                HSSFCell hssfCell = hssfRow.createCell(0);
                hssfCell.setCellValue("Flat No");

                hssfCell = hssfRow.createCell(1);
                hssfCell.setCellValue("Owner Name");

                hssfCell = hssfRow.createCell(2);
                hssfCell.setCellValue("Owner Number");

                hssfCell = hssfRow.createCell(3);
                hssfCell.setCellValue("Living in house");

                hssfCell = hssfRow.createCell(4);
                hssfCell.setCellValue("Maintenance");

                hssfSheet.setColumnWidth(0, (15 * 150));
                hssfSheet.setColumnWidth(1, (15 * 200));
                hssfSheet.setColumnWidth(2, (15 * 220));
                hssfSheet.setColumnWidth(3, (15 * 230));
                hssfSheet.setColumnWidth(4, (15 * 210));

                for (int i = 0; i < ApplicationClass.flats.size(); i++) {
                    int j = 0;
                    hssfRow = hssfSheet.createRow(i + 1);
                    hssfCell = hssfRow.createCell(j);
                    j++;
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getFlatNo());
                    hssfCell = hssfRow.createCell(j);
                    j++;
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerName());
                    hssfCell = hssfRow.createCell(j);
                    j++;
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerNumber());
                    hssfCell = hssfRow.createCell(j);
                    j++;
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getLivingInHouse());
                    hssfCell = hssfRow.createCell(j);
                    hssfCell.setCellValue(ApplicationClass.flats.get(i).getLastMaintenanceMonth() + " " +
                            ApplicationClass.flats.get(i).getLastMaintenanceYear());
                }
                try {
                    if (!filePath.exists()) {
                        filePath.createNewFile();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                    hssfWorkbook.write(fileOutputStream);
                    Toast.makeText(this, "File created at Storage/Aalishan Committee/Data.xml", Toast.LENGTH_SHORT).show();

                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (item.getItemId() == R.id.createExcelFilter) {
            File filePath = new File(Environment.getExternalStorageDirectory() + "/Aalishan Committee/Data with filter.xls");

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {

                HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                HSSFSheet hssfSheet = hssfWorkbook.createSheet("Data with filter");

                int k = 0;

                for (int i = 0; i < ApplicationClass.flats.size(); i++) {

                    int year1 = Integer.parseInt(ApplicationClass.flats.get(i).getLastMaintenanceYear());
                    String month1 = ApplicationClass.flats.get(i).getLastMaintenanceMonth().trim();
                    int mon = getMonthIndex(month1);

                    if (year1 <= year && mon <= month) {

                        HSSFRow hssfRow = hssfSheet.createRow(0);

                        HSSFCell hssfCell = hssfRow.createCell(0);
                        hssfCell.setCellValue("Flat No");

                        hssfCell = hssfRow.createCell(1);
                        hssfCell.setCellValue("Owner Name");

                        hssfCell = hssfRow.createCell(2);
                        hssfCell.setCellValue("Owner Number");

                        hssfCell = hssfRow.createCell(3);
                        hssfCell.setCellValue("Living in house");

                        hssfCell = hssfRow.createCell(4);
                        hssfCell.setCellValue("Maintenance");

                        hssfSheet.setColumnWidth(0, (15 * 150));
                        hssfSheet.setColumnWidth(1, (15 * 200));
                        hssfSheet.setColumnWidth(2, (15 * 220));
                        hssfSheet.setColumnWidth(3, (15 * 230));
                        hssfSheet.setColumnWidth(4, (15 * 210));

                        int j = 0;
                        hssfRow = hssfSheet.createRow(k + 1);
                        hssfCell = hssfRow.createCell(j);
                        j++;
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getFlatNo());
                        hssfCell = hssfRow.createCell(j);
                        j++;
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerName());
                        hssfCell = hssfRow.createCell(j);
                        j++;
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getOwnerNumber());
                        hssfCell = hssfRow.createCell(j);
                        j++;
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getLivingInHouse());
                        hssfCell = hssfRow.createCell(j);
                        hssfCell.setCellValue(ApplicationClass.flats.get(i).getLastMaintenanceMonth() + " " +
                                ApplicationClass.flats.get(i).getLastMaintenanceYear());
                        k++;

                        try {
                            if (!filePath.exists()) {
                                filePath.createNewFile();
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                            hssfWorkbook.write(fileOutputStream);
                            Toast.makeText(this, "File created at Storage/Aalishan Committee/Data with filter.xml", Toast.LENGTH_SHORT).show();

                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else if(item.getItemId() == R.id.societyRules)
        {
            startActivity(new Intent(MainActivity.this, SocietyRules.class));
        }
        else if(item.getItemId() == R.id.logout)
        {
            showProgress(true);
            tvLoad.setText("Logging you out... Please wait...");
            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void response) {
                    Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    MainActivity.this.finish();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(MainActivity.this, "Error: " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        finishAffinity();
    }


    private int getMonthIndex(String month) {
        int monIndex = 0;
        switch(month)
        {
            case "January":
                monIndex = 0;
                break;
            case "February":
                monIndex = 1;
                break;
            case "March":
                monIndex = 2;
                break;
            case "April":
                monIndex = 3;
                break;
            case "May":
                monIndex = 4;
                break;
            case "June":
                monIndex = 5;
                break;
            case "July":
                monIndex = 6;
                break;
            case "August":
                monIndex = 7;
                break;
            case "September":
                monIndex = 8;
                break;
            case "October":
                monIndex = 9;
                break;
            case "November":
                monIndex = 10;
                break;
            case "December":
                monIndex = 11;
                break;
        }
        return monIndex;
    }
}