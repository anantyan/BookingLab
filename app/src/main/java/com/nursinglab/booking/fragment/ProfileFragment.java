package com.nursinglab.booking.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Auth;
import com.nursinglab.booking.component.RecordsComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.util.GlideUtil;
import com.nursinglab.booking.util.RetrofitUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment {

    @BindView(R.id._photo_profile) ImageView photoProfile;
    @BindView(R.id._username_profile) TextView usernameProfile;
    @BindView(R.id._created_at_profile) TextView createdAtProfile;
    @BindView(R.id._nama_profile) TextView namaProfile;
    @BindView(R.id._nim_profile) TextView nimProfile;
    @BindView(R.id._profile) View rootLayout;
    @BindView(R.id._progress_bar) ProgressBar progressBar;
    @BindView(R.id._hide_profile) LinearLayout hideProfile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //sharedPreference get ID
        String id = new SharedPreferenceComponent(this.getActivity()).getDataId();

        //getData profile from database
        getData(id);
    }

    private void getData(String id) {
        hideProfile.setVisibility(View.GONE);

        Retrofit retrofit = RetrofitUtil.getClient();
        Auth auth = retrofit.create(Auth.class);
        Call<ResponseComponent> call = auth.profile(id);
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                progressBar.setVisibility(View.GONE);
                hideProfile.setVisibility(View.VISIBLE);
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                RecordsComponent records = response.body() != null ? response.body().getRecords() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        assert records != null;
                        String username = records.getUsername();
                        String nim = records.getNim();
                        String nama_mahasiswa = records.getNama_mahasiswa();
                        String foto_file = records.getFoto_file();
                        String created_at = records.getCreated_at();

                        String test = RetrofitUtil.BASE_URL_NURSINGLAB+"uploads/foto_mahasiswa/"+foto_file;

                        usernameProfile.setText(username);
                        namaProfile.setText(nama_mahasiswa);
                        nimProfile.setText(nim);
                        createdAtProfile.setText(created_at);
                        new GlideUtil(getActivity(), null).setGlideWithAccent(test, photoProfile);
                    }else{
                        String getId = records != null ? records.getId() : "Empty";
                        Toast.makeText(getActivity(), status+" "+getId, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(getActivity(), errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                hideProfile.setVisibility(View.GONE);
                Snackbar.make(rootLayout, "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_profile, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.rootLayout.getContext());
            alertDialogBuilder
                    .setTitle("Tentang Aplikasi")
                    .setMessage("BookingLab\n" +
                            "Applications version 1.0")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
