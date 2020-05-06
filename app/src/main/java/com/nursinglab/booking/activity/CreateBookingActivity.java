package com.nursinglab.booking.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateBookingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.txt_waktu_mulai) EditText txtWaktuMulai;
    @BindView(R.id.txt_waktu_selesai) EditText txtWaktuSelesai;
    @BindView(R.id.txt_tanggal) EditText txtTanggal;
    @BindView(R.id.txt_dosen) EditText txtDosen;
    @BindView(R.id.txt_lab) EditText txtLab;
    @BindView(R.id.txt_praktikum) EditText txtPraktikum;
    @BindView(R.id._btn_save) Button btnSave;

    //reqest untuk result data kembali
    private static String dosen_id, lab_id, praktikum_id;
    public static final String EXTRA_DOSEN_ID = "extra_dosen_id";
    public static final String EXTRA_DOSEN_NAME = "extra_dosen_name";
    public static final String EXTRA_LAB_ID = "extra_lab_id";
    public static final String EXTRA_LAB_NAME = "extra_lab_name";
    public static final String EXTRA_PRAKTIKUM_ID = "extra_praktikum_id";
    public static final String EXTRA_PRAKTIKUM_NAME = "extra_praktikum_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_booking);
        ButterKnife.bind(CreateBookingActivity.this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Create BookingLab");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtClick();
    }

    private void txtClick() {
        txtWaktuMulai.setFocusable(false);
        txtWaktuSelesai.setFocusable(false);
        txtTanggal.setFocusable(false);
        txtDosen.setFocusable(false);
        txtLab.setFocusable(false);
        txtPraktikum.setFocusable(false);

        txtWaktuMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtWaktuMulai.setText(i + ":" + i1);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(CreateBookingActivity.this));
                timePickerDialog.show();
            }
        });
        txtWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtWaktuSelesai.setText(i + ":" + i1);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(CreateBookingActivity.this));
                timePickerDialog.show();
            }
        });
        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtTanggal.setText(i + "/" + (i1+1) + "/" + i2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        txtDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, DosenActivity.class);
                startActivityForResult(i, 1);
            }
        });
        txtLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, LabActivity.class);
                startActivityForResult(i, 2);
            }
        });
        txtPraktikum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, PraktikumActivity.class);
                startActivityForResult(i, 3);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = txtWaktuMulai.getText().toString().trim();
                String b = txtWaktuSelesai.getText().toString().trim();
                String c = txtTanggal.getText().toString().trim();
                String[] data = new String[]{a, b, c, dosen_id, lab_id, praktikum_id};
                saveData(data);
            }
        });
    }

    private void saveData(String[] data) {
        ProgressDialog progressDialog = new ProgressDialog(CreateBookingActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tunggu...");
        progressDialog.show();
        String id = new SharedPreferenceComponent(CreateBookingActivity.this).getDataId();

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking bkng = retrofit.create(Booking.class);
        Call<ResponseComponent> call = bkng.insert(id, data[0], data[1], data[2], data[3], data[4], data[5]);
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                progressDialog.dismiss();
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        new Intent(CreateBookingActivity.this, MainActivity.class);
                        setResult(-1);
                        finish();
                    }else{
                        Toast.makeText(CreateBookingActivity.this, status, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(CreateBookingActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(R.id._created_booking), "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
                        .setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setDuration(3000)
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == -1) {
            dosen_id = data != null ? data.getStringExtra(EXTRA_DOSEN_ID).trim() : "";
            String b = data != null ? data.getStringExtra(EXTRA_DOSEN_NAME).trim() : "";
            txtDosen.setText(b);
        }
        if(requestCode == 2 && resultCode == -2) {
            lab_id = data != null ? data.getStringExtra(EXTRA_LAB_ID).trim() : "";
            String b = data != null ? data.getStringExtra(EXTRA_LAB_NAME).trim() : "";
            txtLab.setText(b);
        }
        if(requestCode == 3 && resultCode == -3) {
            praktikum_id = data != null ? data.getStringExtra(EXTRA_PRAKTIKUM_ID).trim() : "";
            String b = data != null ? data.getStringExtra(EXTRA_PRAKTIKUM_NAME).trim() : "";
            txtPraktikum.setText(b);
        }
    }
}
