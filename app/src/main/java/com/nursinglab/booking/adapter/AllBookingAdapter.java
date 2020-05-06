package com.nursinglab.booking.adapter;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nursinglab.booking.R;
import com.nursinglab.booking.component.ResultComponent;
import com.nursinglab.booking.helper.ItemClickHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllBookingAdapter extends RecyclerView.Adapter<AllBookingAdapter.ViewHolder> implements Filterable {

    private List<ResultComponent> result;
    private List<ResultComponent> resultFull;
    private ItemClickHelper itemClickHelper;

    public AllBookingAdapter(ItemClickHelper itemClickHelper, List<ResultComponent> result) {
        this.itemClickHelper = itemClickHelper;
        this.result = result;
        this.resultFull = result;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id._nama_lab) TextView namaLab;
        @BindView(R.id._kelas) TextView namaKelas;
        @BindView(R.id._nama_dosen) TextView namaDosen;
        @BindView(R.id._waktu_mulai) TextView waktuMulai;
        @BindView(R.id._expired) TextView expired;

        ViewHolder(@NonNull View itemView, final ItemClickHelper itemClickHelper) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickHelper != null) {
                        itemClickHelper.onItemClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(itemClickHelper != null) {
                        itemClickHelper.onLongItemClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public AllBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_all_booking, viewGroup, false);
        ViewHolder holder = new ViewHolder(v, itemClickHelper);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AllBookingAdapter.ViewHolder viewHolder, int i) {
        ResultComponent resultComponent = result.get(i);

        String a = resultComponent.getNama_dosen();
        int b = Integer.parseInt(resultComponent.getAction());

        viewHolder.namaLab.setText(resultComponent.getNama_lab());
        viewHolder.namaKelas.setText(resultComponent.getKelas());
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
                    if(resultComponent.getKelas().toLowerCase().contains(filterPatern)){
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
            result = (List) results.values;
            notifyDataSetChanged();
        }
    };
}
