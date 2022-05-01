package com.femaaccu.cryotechapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.femaaccu.cryotechapp.database.AppDataBase;
import com.femaaccu.cryotechapp.database.Target;
import com.femaaccu.cryotechapp.database.TargetDAO;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TargetRVAdapter extends RecyclerView.Adapter<TargetRVAdapter.ViewHolder>{
    private ArrayList<TargetRVModal> targetRVModalArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private String localCurrencySymbol;

    @NonNull
    @Override
    public TargetRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_target,parent,false );

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String local_currency = sharedPref.getString("list", "eur");
        if (local_currency.equals("eur"))localCurrencySymbol="€";
        if (local_currency.equals("usd"))localCurrencySymbol="$";
        return new TargetRVAdapter.ViewHolder(view);
    }

    public TargetRVAdapter(ArrayList<TargetRVModal> targetRVModalArrayList, Context context) {
        this.targetRVModalArrayList = targetRVModalArrayList;
        this.context = context;

    }
    @Override
    public void onBindViewHolder(@NonNull TargetRVAdapter.ViewHolder holder, int position) {
        TargetRVModal targetRVModal = targetRVModalArrayList.get(position);
        holder.currencyName = targetRVModal.getName();
        holder.targetPrice = targetRVModal.getTargetPrice();
        holder.iconImageurl = targetRVModal.getImage();
        holder.changeRate = targetRVModal.getChangeRate();
        holder.currencyId = targetRVModal.getCurrencyID();
        holder.currentPrice = targetRVModal.getCurrentPriceTarget();
        holder.currentprice.setText(localCurrencySymbol+df2.format(targetRVModal.getCurrentPriceTarget()));
        holder.name.setText(targetRVModal.getName());
        holder.basePrice.setText(localCurrencySymbol+df2.format(targetRVModal.getBasePrice()));
        holder.progress.setText("%"+df2.format(targetRVModal.getProgressrate()));
        holder.targetprice.setText(localCurrencySymbol+df2.format(targetRVModal.getTargetPrice()));

        Picasso.get().load(holder.iconImageurl).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return targetRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, progress, currentprice, targetprice, basePrice;
        private ImageButton deleteButton;
        private ImageView imagen, arrow;
        private String currencyId, iconImageurl, currencyName, currentPriceTarget;
        private double changeRate, currentPrice, targetPrice;
       // private int position;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.IDTVCurrencyNameTarget);
            progress = itemView.findViewById(R.id.IDTVCurrencyProgressTarget);
            imagen = itemView.findViewById(R.id.IDImagenViewTarget);
            arrow = itemView.findViewById(R.id.IDarrowImageTarget);
            currentprice = itemView.findViewById(R.id.IDTVCurrencyCurrentPriceTarget);
            targetprice = itemView.findViewById(R.id.IDTVTargetPrice);
            basePrice = itemView.findViewById(R.id.IDTVCurrencyBasePriceTarget);
            deleteButton =  itemView.findViewById(R.id.IDTVDeleteButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ItemDetails.class);

                    //enviamos la información al layout del controlador y lo iniciamos mediante un intent
                    intent.putExtra("changeRate", changeRate);
                    intent.putExtra("currencyId", currencyId);
                    intent.putExtra("iconImage", iconImageurl);
                    intent.putExtra("currentPrice", currentPrice);
                    intent.putExtra("currencyName", currencyName);
                    context.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(itemView.getContext(), "long pressed ", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DeleteTarget(currencyName, getAdapterPosition());

                }
            });

        }
    }
    public void DeleteTarget(String currencyName, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Target targetObj = MainActivity.targetDAO.findByName(currencyName);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure do you want to delete "+currencyName+" alert? ");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // delete
                MainActivity.targetDAO.delete(targetObj);
                Toast.makeText(context, "Alert deleted", Toast.LENGTH_SHORT).show();
                targetRVModalArrayList.remove(targetRVModalArrayList.get(position));

                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

