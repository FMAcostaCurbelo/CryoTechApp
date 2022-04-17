package com.femaaccu.cryotechapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TargetRVAdapter extends RecyclerView.Adapter<TargetRVAdapter.ViewHolder>{
    private ArrayList<TargetRVModal> targetRVModalArrayList;
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
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
    public void filterList(ArrayList<TargetRVModal> filteredList){
        this.targetRVModalArrayList = filteredList;
        notifyDataSetChanged();

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
        private TextView name, progress, currentprice, targetprice, basePrice ;
        private ImageView imagen, arrow;
        private String currencyId, iconImageurl, currencyName, currentPriceTarget;
        private double changeRate, currentPrice, targetPrice;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.IDTVCurrencyNameTarget);
            progress = itemView.findViewById(R.id.IDTVCurrencyProgressTarget);
            imagen = itemView.findViewById(R.id.IDImagenViewTarget);
            arrow = itemView.findViewById(R.id.IDarrowImageTarget);
            currentprice = itemView.findViewById(R.id.IDTVCurrencyCurrentPriceTarget);
            targetprice = itemView.findViewById(R.id.IDTVTargetPrice);
            basePrice = itemView.findViewById(R.id.IDTVCurrencyBasePriceTarget);

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
                    //intent.putExtra("imagenes", images);
                    context.startActivity(intent);
                }
            });

        }
    }
}
