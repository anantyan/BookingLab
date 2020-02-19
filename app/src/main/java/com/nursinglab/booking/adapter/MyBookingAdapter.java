package com.nursinglab.booking.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.ResultComponent;
import com.nursinglab.booking.fragment.MyBookingFragment;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.ViewHolder> implements Filterable {

    private List<ResultComponent> result;
    private List<ResultComponent> resultFull;
    private Context context;

    public MyBookingAdapter(Context context, List<ResultComponent> result) {
        this.context = context;
        this.result = result;
        resultFull = new ArrayList<>(result);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id._nama_lab) TextView namaLab;
        @BindView(R.id._nama_dosen) TextView namaDosen;
        @BindView(R.id._waktu_mulai) TextView waktuMulai;
        @BindView(R.id._expired) TextView expired;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public MyBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_my_booking, viewGroup, false);
        MyBookingAdapter.ViewHolder holder = new MyBookingAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBookingAdapter.ViewHolder viewHolder, int i) {
        ResultComponent resultComponent = result.get(i);

        String a = resultComponent.getNama_dosen();
        int b = Integer.parseInt(resultComponent.getAction());

        viewHolder.namaLab.setText(resultComponent.getNama_lab());
        viewHolder.namaDosen.setText(String.format("(%s)", a));
        viewHolder.waktuMulai.setText(resultComponent.getWaktu_mulai());

        if(b == 0) {
            viewHolder.expired.setText("(Dipakai)");
            viewHolder.expired.setTextColor(Color.parseColor("#28a745"));
        } else if(b == 1) {
            viewHolder.expired.setText("(Expired)");
            viewHolder.expired.setTextColor(Color.parseColor("#dc3545"));
        } else {
            viewHolder.expired.setText("(404)");
            viewHolder.expired.setTextColor(Color.parseColor("#ffc107"));
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public Filter getFilter() {
        return pencarian;
    }

    private Filter pencarian = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResultComponent> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(resultFull);
            }else{
                String filterPatern = constraint.toString().toLowerCase().trim();
                for(ResultComponent resultComponent : resultFull){
                    if(resultComponent.getNama_lab().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                    if(resultComponent.getNama_dosen().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                    if(resultComponent.getWaktu_mulai().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            result.clear();
            result.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}