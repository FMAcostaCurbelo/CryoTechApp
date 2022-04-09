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

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private String localCurrencySymbol;
    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rc_item,parent,false );

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String local_currency = sharedPref.getString("list", "eur");
        if (local_currency.equals("eur"))localCurrencySymbol="€";
        if (local_currency.equals("usd"))localCurrencySymbol="$";
        return new CurrencyRVAdapter.ViewHolder(view);
    }

    public CurrencyRVAdapter(ArrayList<CurrencyRVModal> currencyRVModalArrayList, Context context) {
        this.currencyRVModalArrayList = currencyRVModalArrayList;
        this.context = context;
    }
    public void filterList(ArrayList<CurrencyRVModal> filteredList){
        this.currencyRVModalArrayList = filteredList;
        notifyDataSetChanged();

    }
    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyRVModal currencyRVModal = currencyRVModalArrayList.get(position);
        holder.currencyName.setText(currencyRVModal.getName());
        holder.symbolTV.setText(currencyRVModal.getSymbol());
        holder.price.setText(localCurrencySymbol+df2.format(currencyRVModal.getPrice()));
        holder.currencyId = currencyRVModal.getId();
        holder.changeRate = (currencyRVModal.getRate());
        holder.iconImage = currencyRVModal.getImage();
        holder.currentPrice = currencyRVModal.getPrice();
        double rate = currencyRVModal.getRate();
        holder.rateTV.setText("%"+df2.format(currencyRVModal.getRate()));
        if (rate >= 0){
            holder.arrow.setImageResource(R.drawable.greenarrow);
        }else{
            holder.arrow.setImageResource(R.drawable.redarrowdown);
        }
        Picasso.get().load(currencyRVModal.getImage()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView currencyName, symbolTV, rateTV, price;
        private ImageView imagen, arrow;
        private String currencyId, iconImage;
        private double changeRate, currentPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            currencyName = itemView.findViewById(R.id.IDTVCurrencyName);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            price = itemView.findViewById(R.id.IDTVCurrencyPrice);
            imagen = itemView.findViewById(R.id.IDImagenView);
            rateTV = itemView.findViewById(R.id.IDCurrencyRate);
            arrow = itemView.findViewById(R.id.IDarrowImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ItemDetails.class);

                    //enviamos la información al layout del controlador y lo iniciamos mediante un intent
                    intent.putExtra("changeRate", changeRate);
                    intent.putExtra("currencyId", currencyId);
                    intent.putExtra("iconImage", iconImage);
                    intent.putExtra("currentPrice", currentPrice);
                    //intent.putExtra("imagenes", images);
                    context.startActivity(intent);
                }
            });

        }
    }
}
