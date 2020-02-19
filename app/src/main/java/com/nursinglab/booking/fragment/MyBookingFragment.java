package com.nursinglab.booking.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nursinglab.booking.R;
import com.nursinglab.booking.activity.CreateBookingActivity;
import com.nursinglab.booking.adapter.MyBookingAdapter;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.RecordsComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.ResultComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
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

public class MyBookingFragment extends Fragment {

    @BindView(R.id._select_data) RecyclerView recyclerView;
    @BindView(R.id._progress_bar) ProgressBar progressBar;
    @BindView(R.id._swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id._my_booking) View rootLayout;
    @BindView(R.id._float_create) FloatingActionButton floatingCreate;

    private MenuItem menuItem;
    private SearchView searchView;
    private List<ResultComponent> result = new ArrayList<>();
    private MyBookingAdapter myBookingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_booking, container, false);
        ButterKnife.bind(this, view);

        //sharedPreference get ID
        String id = new SharedPreferenceComponent(this.getActivity()).getDataId();
        getData(id);
        recyclerView();
        floatingCreate();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //LoadData
                getData(id);

                //StopAnimate with Delay
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        return view;
    }

    private void floatingCreate() {
        floatingCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateBookingActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void getData(String id) {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking booking = retrofit.create(Booking.class);
        Call<ResponseComponent> call = booking.select(id);
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                RecordsComponent records = response.body() != null ? response.body().getRecords() : null;
                result = response.body() != null ? response.body().getResult() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        myBookingAdapter = new MyBookingAdapter(getActivity(), result);
                        recyclerView.setAdapter(myBookingAdapter);
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
                recyclerView.setVisibility(View.GONE);
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

    private void recyclerView() {
        myBookingAdapter = new MyBookingAdapter(getActivity(), result);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.rootLayout.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(myBookingAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingCreate.getVisibility() == View.VISIBLE) {
                    floatingCreate.hide();
                } else if (dy < 0 && floatingCreate.getVisibility() != View.VISIBLE) {
                    floatingCreate.show();
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(this.getActivity(), recyclerView, new RecyclerOnItemListener.ClickListener() {
            ResultComponent resultComponent;
            @Override
            public void onClick(View view, int position) {
                resultComponent = result.get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootLayout.getContext());
                alertDialogBuilder.setTitle(resultComponent.getNama_lab());
                alertDialogBuilder
                        .setMessage("Waktu mulai : "+resultComponent.getWaktu_mulai()+"\n"+
                                "Waku selesai : "+resultComponent.getWaktu_selesai()+"\n\n"+
                                "Dosen : "+resultComponent.getNama_dosen()+"\n"+
                                "Praktikum : "+resultComponent.getNama_praktikum()+"\n\n"+
                                "Tanggal : "+resultComponent.getTanggal())
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, int position) {
                resultComponent = result.get(position);
                String id_booking = resultComponent.getId();
                String id_user = new SharedPreferenceComponent(rootLayout.getContext()).getDataId();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootLayout.getContext());
                alertDialogBuilder.setTitle(resultComponent.getNama_lab());
                alertDialogBuilder
                        .setMessage("Apakah anda tidak keberatan untuk membatalkan yang anda booking?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Retrofit retrofit = RetrofitUtil.getClient();
                                Booking bkng = retrofit.create(Booking.class);
                                Call<ResponseComponent> call = bkng.delete(id_booking);
                                call.enqueue(new Callback<ResponseComponent>() {
                                    @Override
                                    public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                                        Integer error = response.body() != null ? response.body().getError() : null;
                                        String status = response.body() != null ? response.body().getStatus() : null;
                                        if(response.isSuccessful()){
                                            assert error != null;
                                            if(error.equals(1)) {
                                                Toast.makeText(rootLayout.getContext(), status, Toast.LENGTH_SHORT).show();
                                                getData(id_user);
                                            }else{
                                                Toast.makeText(rootLayout.getContext(), status, Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                                            Toast.makeText(rootLayout.getContext(), errorBody, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseComponent> call, Throwable t) {
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
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_my_booking, menu);

        menuItem = menu.findItem(R.id.action_pencarian);
        searchView = (SearchView) menuItem.getActionView();

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
        }else if(id == R.id.action_pencarian){
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    myBookingAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == -1) {
            String id = new SharedPreferenceComponent(rootLayout.getContext()).getDataId();
            getData(id);
        }
    }
}