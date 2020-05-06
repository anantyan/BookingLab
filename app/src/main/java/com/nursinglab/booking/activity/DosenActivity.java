package com.nursinglab.booking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.adapter.DosenAdapter;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.listener.RecyclerOnItemListener;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.nursinglab.booking.activity.CreateBookingActivity.EXTRA_DOSEN_ID;
import static com.nursinglab.booking.activity.CreateBookingActivity.EXTRA_DOSEN_NAME;


public class DosenActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id._select_data) RecyclerView recyclerView;
    @BindView(R.id._progress_bar) ProgressBar progressBar;
    @BindView(R.id._swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private SearchView searchView;
    private List<BookingIdComponent> booking = new ArrayList<>();
    private DosenAdapter dosenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Pilih Dosen");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getData();
        recyclerView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //LoadData
                getData();

                //StopAnimate with Delay
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void recyclerView() {
        dosenAdapter = new DosenAdapter(DosenActivity.this, booking);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DosenActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(DosenActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(dosenAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(DosenActivity.this, recyclerView, new RecyclerOnItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookingIdComponent bookingIdComponent = booking.get(position);
                Intent i = new Intent();
                i.putExtra(EXTRA_DOSEN_ID, bookingIdComponent.getId());
                i.putExtra(EXTRA_DOSEN_NAME, bookingIdComponent.getNama_dosen());
                setResult(-1, i);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getData() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking bkng = retrofit.create(Booking.class);
        Call<ResponseComponent> call = bkng.booking("dosen");
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                booking = response.body() != null ? response.body().getBooking() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        dosenAdapter = new DosenAdapter(DosenActivity.this, booking);
                        recyclerView.setAdapter(dosenAdapter);
                    }else{
                        Toast.makeText(DosenActivity.this, status, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(DosenActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id._dosen), "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_all_booking, menu);

        MenuItem menuItem = menu.findItem(R.id.action_pencarian);
        searchView = (SearchView) menuItem.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DosenActivity.this);
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
        }else if(id == R.id.action_pencarian){
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    dosenAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
