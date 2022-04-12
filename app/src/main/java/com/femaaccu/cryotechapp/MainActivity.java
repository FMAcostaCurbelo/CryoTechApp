package com.femaaccu.cryotechapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String local_currency;
    private EditText searchEDT;
    private RecyclerView currenciesRV;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        searchEDT = findViewById(R.id.IDEdtSearch);
        currenciesRV= findViewById(R.id.IDRCurrencies);
        loadingPB = findViewById(R.id.IDPBLoading);

        currencyRVModalArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyRVModalArrayList, this);

        currenciesRV.setLayoutManager(new LinearLayoutManager(this));
        currenciesRV.setAdapter(currencyRVAdapter);

        loadSharedPreferences();
        getCurrencyData(local_currency);

        searchEDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCurrencies(editable.toString());
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadSharedPreferences();
    }
    private void sortCurrencies(){

        Collections.sort(currencyRVModalArrayList, new Comparator<CurrencyRVModal>(){
            public int compare(CurrencyRVModal e1, CurrencyRVModal e2){
                 return (""+e2.getRate()).compareTo(""+e1.getRate());
            }
        });
        if(currencyRVModalArrayList.isEmpty()){
            Toast.makeText(this, "Item not found ", Toast.LENGTH_SHORT);
        }else{
            currencyRVAdapter.filterList(currencyRVModalArrayList);

        }
    }
    private void filterCurrencies(String currency){
        ArrayList<CurrencyRVModal> filteredList = new ArrayList<>();
        for (CurrencyRVModal item: currencyRVModalArrayList){
            String itemName = item.getName().toLowerCase();
            if (itemName.contains(currency.toLowerCase())) {

                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "Item not found ", Toast.LENGTH_SHORT);
        }else{
            currencyRVAdapter.filterList(filteredList);

        }
    }
    private void getCurrencyData(String local_currency){
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency="+local_currency+"&order=market_cap_desc&per_page=100&page=1&sparkline=false";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                try {

                    for(int i=0; i<response.length(); i++){
                        JSONObject dataObj = response.getJSONObject(i);
                        String id = dataObj.getString("id");
                        String image = dataObj.getString("image");
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");
                        double rate = dataObj.getDouble("price_change_percentage_24h");
                        double price = dataObj.getDouble("current_price");
                        currencyRVModalArrayList.add(new CurrencyRVModal(name, symbol, price, id, image, rate));
                    }
                    currencyRVAdapter.notifyDataSetChanged();

                }
                catch(JSONException e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Fail to exctract JSON data... "+e, Toast.LENGTH_LONG).show();
                    }
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Fail to get the Data "+error, Toast.LENGTH_LONG).show();
            }
        }){

        };
        requestQueue.add(getRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, Preferences.class);
            startActivity(i);
            return true;
        }else if (id == R.id.action_sort){
            sortCurrencies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    public void  loadSharedPreferences(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        local_currency = sharedPref.getString("list", "eur");

    }



}