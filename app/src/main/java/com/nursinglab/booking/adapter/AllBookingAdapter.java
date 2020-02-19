package com.nursinglab.booking.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.nursinglab.booking.component.ResultComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllBookingAdapter extends RecyclerView.Adapter<AllBookingAdapter.ViewHolder> implements Filterable {

    private List<ResultComponent> result;
    private List<ResultComponent> resultFull;
    private Context context;
    /*private ClickListener clickListener;*/

    public AllBookingAdapter(Context context, List<ResultComponent> result) {
        this.context = context;
        this.result = result;
        resultFull = new ArrayList<>(result);
    }

    /*public void setClickListener(Context context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }*/

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id._nama_lab) TextView namaLab;
        @BindView(R.id._kelas) TextView namaKelas;
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
    public AllBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_recycler_all_booking, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
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


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*clickListener.Click(view, result.get(viewHolder.getAdapterPosition()));*/
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                alertDialogBuilder.setTitle(result.get(viewHolder.getAdapterPosition()).getNama_lab());
                alertDialogBuilder
                        .setMessage("Nim : "+result.get(viewHolder.getAdapterPosition()).getNim_mahasiswa()+"\n"+
                                "Waktu mulai : "+result.get(viewHolder.getAdapterPosition()).getWaktu_mulai()+"\n"+
                                "Waku selesai : "+result.get(viewHolder.getAdapterPosition()).getWaktu_selesai()+"\n\n"+
                                "Dosen : "+result.get(viewHolder.getAdapterPosition()).getNama_dosen()+"\n"+
                                "Praktikum : "+result.get(viewHolder.getAdapterPosition()).getNama_praktikum()+"\n\n"+
                                "Kelas : "+result.get(viewHolder.getAdapterPosition()).getKelas()+"\n"+
                                "Tanggal : "+result.get(viewHolder.getAdapterPosition()).getTanggal())
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /*clickListener.LongClick(view, result.get(viewHolder.getAdapterPosition()));*/
                String nama_lab = result.get(viewHolder.getAdapterPosition()).getNama_lab();
                Toast.makeText(context, nama_lab, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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
            result.clear();
            result.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    /*public interface ClickListener {
        void Click(View view, ResultComponent resultComponent);
        void LongClick(View view, ResultComponent resultComponent);
    }*/
}
