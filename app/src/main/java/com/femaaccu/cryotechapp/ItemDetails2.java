package com.femaaccu.cryotechapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.femaaccu.cryotechapp.databinding.ActivityItemDetails2Binding;
import com.femaaccu.cryotechapp.databinding.ActivityItemDetailsBinding;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ItemDetails2 extends AppCompatActivity {
    private ActivityItemDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());


        Bundle extras = getIntent().getExtras();
        View view = binding.getRoot();
        setContentView(view);

        String currencyname = extras.getString("currencyName");

        setCandleStickChart(currencyname);

    }

    public void setCandleStickChart(String currencyname){
        String fixedCurrencyName = currencyname.toLowerCase();
        String url = "https://api.coingecko.com/api/v3/coins/"+fixedCurrencyName+"/ohlc?vs_currency=eur&days=1";
        String resultado="";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response


                        ArrayList<CandleEntry> candlestickentry = new ArrayList<>();
                        ArrayList<String> xvalue = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONArray array = response.getJSONArray(i);
                                String open = array.getString(1);
                                String high = array.getString(2);
                                String low = array.getString(3);
                                String close = array.getString(4);

                                float floatOpen = Float.parseFloat(open);
                                float floatHigh = Float.parseFloat(high);
                                float floatLow = Float.parseFloat(low);
                                float floatClose = Float.parseFloat(close);
                                xvalue.add("00:0"+i);
                                candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                candlestickentry.add(new CandleEntry(i, 11,23,54,77));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        CandleDataSet candledataset = new CandleDataSet(candlestickentry, currencyname);


                        candledataset.setColor(Color.rgb(80,80,80));
                        candledataset.setShadowColor(android.R.color.holo_blue_bright);
                        candledataset.setShadowWidth(1f);
                        candledataset.setDecreasingColor(Color.RED);
                        candledataset.setDecreasingPaintStyle(Paint.Style.FILL);
                        candledataset.setIncreasingColor(Color.GREEN);
                        candledataset.setIncreasingPaintStyle(Paint.Style.FILL);


                        CandleData candledata = new CandleData(xvalue, candledataset);

                        CandleStickChart candlechart = binding.candlechart;
                        candlechart.setData(candledata);
                        candlechart.setBackgroundColor(Color.WHITE);
                        candlechart.animateXY(2000,2000);

                        XAxis xval = candlechart.getXAxis();
                        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xval.setDrawGridLines(false);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemDetails2.this, "ERROR IS"+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
             // add it to the RequestQueue
            queue.add(getRequest);

    }

}