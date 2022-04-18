package com.femaaccu.cryotechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.femaaccu.cryotechapp.database.AppDataBase;
import com.femaaccu.cryotechapp.database.Target;
import com.femaaccu.cryotechapp.database.TargetDAO;
import com.femaaccu.cryotechapp.databinding.ActivityItemTargetBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ItemTarget extends AppCompatActivity {
    private ActivityItemTargetBinding binding;
    Bundle extras;
    String currencyName, currencyImage, localCurrencySymbol;
    double currentPrice;
    TextView currencyNameTV, currencyPriceTV, currencyChangeProcentageTV;
    ImageView currencyIV, arrowIV;
    EditText inputTargetPriceET;
    Button saveButton;
    AppDataBase db;
    TargetDAO targetDAO;
    Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_target);
        binding = ActivityItemTargetBinding.inflate(getLayoutInflater());

        extras = getIntent().getExtras();
        View view = binding.getRoot();
        setContentView(view);

        context = this.getApplicationContext();
        loadPreferences();
        db = AppDataBase.getInstance(context);
        targetDAO = db.targetDAO();

        currencyName = extras.getString("currencyName");
        currentPrice = extras.getDouble("currencyPrice");
        currencyImage = extras.getString("currencyImage");

        currencyNameTV = binding.textViewTargetCurrencyName;
        currencyPriceTV = binding.textViewTargetCurrencyPrice;
        currencyChangeProcentageTV = binding.textViewChangePorcentage;
        currencyIV = binding.imageViewTargetCurrencyIcon;
        arrowIV = binding.imageViewArrow;
        inputTargetPriceET = binding.editTextTargetPriceInput;
        saveButton = binding.buttonSave;

        currencyNameTV.setText(currencyName);
        Picasso.get().load(currencyImage).into(currencyIV);
        currencyPriceTV.setText(localCurrencySymbol+df2.format(currentPrice));

        inputTargetPriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String getValue = inputTargetPriceET.getText().toString();
                if (!getValue.equals("")) {
                    double targetPrice = Double.parseDouble(getValue);
                    currencyChangeProcentageTV.setText(PorcentageChange(targetPrice));
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getValue = "";
                getValue = inputTargetPriceET.getText().toString();
                if (!getValue.equals("")) {
                    double targetPrice = Double.parseDouble(getValue);
                    Target checkIfAlreadyInserted = targetDAO.findByName(currencyName);

                    if (checkIfAlreadyInserted == null) {
                        checkIfAlreadyInserted = new Target(currencyName, currentPrice/MainActivity.exchange, targetPrice/MainActivity.exchange);
                        targetDAO.insert(checkIfAlreadyInserted);
                        Toast.makeText(ItemTarget.this, checkIfAlreadyInserted.getCurrency_name() + " Added", Toast.LENGTH_SHORT).show();
                    } else {
                        targetDAO.delete(checkIfAlreadyInserted);
                        targetDAO.insert(checkIfAlreadyInserted);
                        Toast.makeText(ItemTarget.this,  " Added new crypto alert for "+currencyName , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ItemTarget.this, " Ingrese un valor ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadPreferences(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String local_currency = sharedPref.getString("list", "eur");

        if (local_currency.equals("eur"))localCurrencySymbol="€";
        if (local_currency.equals("usd"))localCurrencySymbol="$";
    }

    private String PorcentageChange(double targetPrice){

        Double change = ((targetPrice-currentPrice)*100)/currentPrice;
        if (change>=0){
            arrowIV.setImageResource(R.drawable.greenarrow);
        }
        else{
            arrowIV.setImageResource(R.drawable.redarrowdown);
        }
        return "%"+df2.format(change);
    }



}