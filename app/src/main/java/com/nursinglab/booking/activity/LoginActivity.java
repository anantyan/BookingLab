package com.nursinglab.booking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Auth;
import com.nursinglab.booking.component.RecordsComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.util.RetrofitUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id._username) EditText txtUsername;
    @BindView(R.id._password) EditText txtPassword;
    @BindView(R.id._btn_login) Button btnLogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);

        //cek session pengguna telah masuk
        if(SharedPreferenceComponent.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin();
    }

    private void btnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(username.equals("")) {
                    Toast.makeText(LoginActivity.this, "Username harus diisi!", Toast.LENGTH_LONG).show();
                    txtUsername.setError("Username harus diisi!");
                    txtUsername.requestFocus();
                } else if(password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Passowrd harus diisi!", Toast.LENGTH_LONG).show();
                    txtPassword.setError("Password harus diisi!");
                    txtPassword.requestFocus();
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Tunggu...");
                    progressDialog.show();

                    Retrofit retrofit = RetrofitUtil.getClient();
                    Auth auth = retrofit.create(Auth.class);
                    Call<ResponseComponent> call = auth.login(username, password);
                    call.enqueue(new Callback<ResponseComponent>() {
                        @Override
                        public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                            Integer error = response.body() != null ? response.body().getError() : null;
                            String status = response.body() != null ? response.body().getStatus() : null;
                            RecordsComponent records = response.body() != null ? response.body().getRecords() : null;
                            progressDialog.dismiss();
                            if(response.isSuccessful()){
                                assert error != null;
                                if(error.equals(1)) {
                                    SharedPreferenceComponent.setLoggedIn(LoginActivity.this, true); //membuat sharedPreference true login
                                    SharedPreferenceComponent data = new SharedPreferenceComponent(LoginActivity.this); //variabel sharedPreference
                                    String id = records != null ? records.getId() : null;
                                    data.setDataIn(id); //menyimpan sharePreference id
                                    Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                                Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseComponent> call, Throwable t) {

                            progressDialog.dismiss();
                            Snackbar.make(findViewById(R.id._login), "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
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
            }
        });
    }
}
