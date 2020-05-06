package com.nursinglab.booking.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nursinglab.booking.R;
import com.nursinglab.booking.component.BookingIdComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.ViewHolder> implements Filterable {

    private List<BookingIdComponent> booking;
    private List<BookingIdComponent> bookingFull;
    private Context context;

    public LabAdapter(Context context, List<BookingIdComponent> booking) {
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
                    if(bookingIdComponent.getNama().toLowerCase().contains(filterPatern)){
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
    public LabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_booking, viewGroup, false);
        LabAdapter.ViewHolder holder = new LabAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LabAdapter.ViewHolder viewHolder, int i) {
        BookingIdComponent bookingIdComponent = booking.get(i);
        viewHolder.nama.setText(bookingIdComponent.getNama());
    }

    @Override
    public int getItemCount() {
        return booking.size();
    }
}
