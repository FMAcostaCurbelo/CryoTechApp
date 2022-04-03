package com.femaaccu.cryotechapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText searchEDT;
    private RecyclerView currenciesRV;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEDT = findViewById(R.id.IDEdtSearch);
        currenciesRV= findViewById(R.id.IDRCurrencies);
        loadingPB = findViewById(R.id.IDPBLoading);

        currencyRVModalArrayList = new ArrayList<>();
        currencyRVAdapter = new CurrencyRVAdapter(currencyRVModalArrayList, this);

        currenciesRV.setLayoutManager(new LinearLayoutManager(this));
        currenciesRV.setAdapter(currencyRVAdapter);

        getCurrencyData();

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
    private void filterCurrencies(String currency){
        ArrayList<CurrencyRVModal> filteredList = new ArrayList<>();
        for (CurrencyRVModal item: currencyRVModalArrayList){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No currency found for search query", Toast.LENGTH_SHORT);
        }else{
            currencyRVAdapter.filterList(filteredList);
        }
    }
    private void getCurrencyData(){
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=eur&order=market_cap_desc&per_page=100&page=1&sparkline=false";
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
}