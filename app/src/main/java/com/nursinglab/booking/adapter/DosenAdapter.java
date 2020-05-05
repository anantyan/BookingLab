package com.nursinglab.booking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.nursinglab.booking.R;
import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.component.ResultComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DosenAdapter extends RecyclerView.Adapter<DosenAdapter.ViewHolder> implements Filterable {

    private List<BookingIdComponent> booking;
    private List<BookingIdComponent> bookingFull;
    private Context context;

    public DosenAdapter(Context context, List<BookingIdComponent> booking) {
        this.context = context;
        this.booking = booking;
        this.bookingFull = booking;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id._nama) TextView nama;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return pencarian;
    }

    private Filter pencarian = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookingIdComponent> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(bookingFull);
            }else{
                String filterPatern = constraint.toString().toLowerCase().trim();
                for(BookingIdComponent bookingIdComponent : bookingFull){
                    if(bookingIdComponent.getNama_dosen().toLowerCase().contains(filterPatern)){
                        filteredList.add(bookingIdComponent);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            booking = (List) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public DosenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_booking, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DosenAdapter.ViewHolder viewHolder, int i) {
        BookingIdComponent bookingIdComponent = booking.get(i);
        viewHolder.nama.setText(bookingIdComponent.getNama_dosen());
    }

    @Override
    public int getItemCount() {
        return booking.size();
    }
}
