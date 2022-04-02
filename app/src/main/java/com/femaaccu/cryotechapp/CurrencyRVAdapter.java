package com.femaaccu.cryotechapp;

import android.content.Context;
import android.content.Intent;
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
    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rc_item,parent,false );
        return new CurrencyRVAdapter.ViewHolder(view);
    }

    public CurrencyRVAdapter(ArrayList<CurrencyRVModal> currencyRVModalArrayList, Context context) {
        this.currencyRVModalArrayList = currencyRVModalArrayList;
        this.context = context;
    }
    public void filterList(ArrayList<CurrencyRVModal> filteredList){
        currencyRVModalArrayList = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        CurrencyRVModal currencyRVModal = currencyRVModalArrayList.get(position);
        holder.currencyName.setText(currencyRVModal.getName());
        holder.symbolTV.setText(currencyRVModal.getSymbol());
        holder.price.setText("$"+df2.format(currencyRVModal.getPrice()));
        holder.currencyId = currencyRVModal.getId();
        double rate = currencyRVModal.getRate();
        holder.rateTV.setText("%"+df2.format(currencyRVModal.getRate()));
        if (rate >= 0){
            holder.arrow.setImageResource(R.drawable.greena12);
        }else{
            holder.arrow.setImageResource(R.drawable.reda12);
        }
        Picasso.get().load(currencyRVModal.getImage()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView currencyName, symbolTV, rateTV, price;
        private ImageView imagen, arrow;
        private String currencyId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.IDTVCurrencyName);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            price = itemView.findViewById(R.id.IDTVCurrencyPrice);
            imagen = itemView.findViewById(R.id.IDImagenView);
            rateTV = itemView.findViewById(R.id.IDCurrencyRate);
            arrow = itemView.findViewById(R.id.IDarrow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // int[] images = {-1, -1};
                    Intent intent = new Intent(context, ItemDetails.class);

                    //enviamos la información al layout del controlador y lo iniciamos mediante un intent
                    intent.putExtra("currencyName", currencyName.getText());
                    intent.putExtra("currencyId", currencyId);
                    //intent.putExtra("imagenes", images);
                    context.startActivity(intent);
                }
            });

        }
    }
}
