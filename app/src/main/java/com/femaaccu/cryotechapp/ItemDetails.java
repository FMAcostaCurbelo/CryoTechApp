package com.femaaccu.cryotechapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.femaaccu.cryotechapp.database.AppDataBase;
import com.femaaccu.cryotechapp.database.FavoriteDAO;
import com.femaaccu.cryotechapp.database.Favorites;
import com.femaaccu.cryotechapp.database.Target;
import com.femaaccu.cryotechapp.database.TargetDAO;
import com.femaaccu.cryotechapp.databinding.ActivityItemDetailsBinding;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class ItemDetails extends AppCompatActivity {
    private ActivityItemDetailsBinding binding;
    Bundle extras;
    double currentPrice, iniPrice, floatValuefordayone;
    Button bDay, bWeek, bMonth, bYear, bMax;
    ImageView arrowImage, tviconImage, iVArrowTarget;
    TextView tvchange, tvIniValue, tvLastValue, tvCurrencyName, tvTargetPriceDetails, tvPriceBase, tvChangeToTarget;
    String local_currency, localCurrencySymbol;
    String currencyId, currencyName;
    ImageButton iButtonFav;
    ImageButton iButtonAddStats;
    AppDataBase db;
    FavoriteDAO favoriteDAO;
    TargetDAO targetDAO;
    Context context;
    ConstraintLayout targetPriceLayout;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());

        context = this.getApplicationContext();

        db = AppDataBase.getInstance(context);
        favoriteDAO = db.favouriteDAO();
        targetDAO = db.targetDAO();
        extras = getIntent().getExtras();
        View view = binding.getRoot();
        setContentView(view);

        currencyId = extras.getString("currencyId");
        currentPrice = extras.getDouble("currentPrice");
        currencyName = extras.getString("currencyName");
        String iconImage = extras.getString("iconImage");



        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        local_currency = sharedPref.getString("list", "eur");

        if (local_currency.equals("eur"))localCurrencySymbol="€";
        if (local_currency.equals("usd"))localCurrencySymbol="$";

        bDay = binding.buttonChartDay;
        bWeek = binding.buttonChartWeek;
        bMonth = binding.buttonChartMonth;
        bYear = binding.buttonChartYear;
        bMax = binding.buttonChartMax;
        arrowImage = binding.IDDetailArrow;
        tvchange = binding.idTextViewChange;
        tviconImage = binding.TViconImage;
        tvIniValue = binding.textViewIniPrice;
        tvLastValue = binding.textViewLastPrice;
        tvCurrencyName = binding.textViewCurrencyName;
        iButtonFav = binding.imageButtonFav;
        targetPriceLayout = binding.ConstraintLayoutTargetPrice;
        tvPriceBase = binding.textViewPriceBase;
        tvChangeToTarget = binding.textViewChangetoTarget;
        tvTargetPriceDetails = binding.textViewTargetPriceDetails;
        iVArrowTarget = binding.imageViewArroTarget;
        iButtonAddStats = binding.imageButtonStats;
        Picasso.get().load(iconImage).into(tviconImage);

        Favorites checkifFav = favoriteDAO.findByName(currencyId);
        if(checkifFav!=null)
            iButtonFav.setImageResource(R.drawable.fill_star);

        tvCurrencyName.setText(currencyName);
        String currentValue=localCurrencySymbol+df2.format(currentPrice);
        tvLastValue.setText(currentValue);

        setCandleStickChart(currencyId, "1");

        loadTargetPrice();

        bDay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setCandleStickChart(currencyId, "1");
            }
        });
        bWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setCandleStickChart(currencyId, "7");


            }
        });
        bMonth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "30");

            }
        });
        bYear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "365");

            }
        });
        bMax.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                setCandleStickChart(currencyId, "max");

            }
        });
        iButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Favorites checkIfAlreadyInserted = favoriteDAO.findByName(currencyId);
                if (checkIfAlreadyInserted==null) {
                    checkIfAlreadyInserted = new Favorites(currencyId);
                    favoriteDAO.insert(checkIfAlreadyInserted);
                    Toast.makeText(ItemDetails.this, checkIfAlreadyInserted.getCurrency_id()+" Added to Favorites" , Toast.LENGTH_SHORT).show();
                    iButtonFav.setImageResource(R.drawable.fill_star);
                }else{
                    favoriteDAO.delete(checkIfAlreadyInserted);
                    Toast.makeText(ItemDetails.this, currencyId+" Removed from Favorites" , Toast.LENGTH_SHORT).show();
                    iButtonFav.setImageResource(R.drawable.empty_star);
                }
            }
        });
        targetPriceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), ItemTarget.class);

                intent.putExtra("currencyName", currencyName);
                intent.putExtra("currencyPrice", currentPrice);
                intent.putExtra("currencyImage", iconImage);
                view.getContext().startActivity(intent);
            }
        });
        iButtonAddStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemTarget.class);

                intent.putExtra("currencyName", currencyName);
                intent.putExtra("currencyPrice", currentPrice);
                intent.putExtra("currencyImage", iconImage);
                view.getContext().startActivity(intent);

            }
        });
    }


    private void setCandleStickChart(String currencyID, String days){

        String url = "https://api.coingecko.com/api/v3/coins/"+currencyID+"/ohlc?vs_currency="+ local_currency +"&days="+days;

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
                                String epochTime = array.getString(0);
                                Date time = new Date(Long.parseLong(epochTime));
                                dateObject realTime= new dateObject(time.toString());

                                if (i==0){
                                    InitValue(currencyID, realTime, days);
                                }

                                String open = array.getString(1);
                                String high = array.getString(2);
                                String low = array.getString(3);
                                String close = array.getString(4);

                                float floatOpen = Float.parseFloat(open);
                                float floatHigh = Float.parseFloat(high);
                                float floatLow = Float.parseFloat(low);
                                float floatClose = Float.parseFloat(close);

                                switch (days){
                                    case "1":
                                        if (i==0)floatValuefordayone=floatOpen;
                                        xvalue.add(realTime.hour+":"+realTime.minute);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    case "7":
                                        xvalue.add(realTime.dayName+" "+realTime.dayNumber);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    case "30":
                                    case "365":
                                        xvalue.add(realTime.dayNumber+" "+realTime.monthName);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));

                                        break;
                                    case "max":
                                        xvalue.add(realTime.monthName+" "+realTime.year);
                                        candlestickentry.add(new CandleEntry(i, floatHigh, floatLow, floatOpen, floatClose));
                                        break;
                                    default:
                                        xvalue.add("error not API data");
                                        break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        String currencyname = extras.getString("currencyName");

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
                        candlechart.animateXY(1500,1000);
                        XAxis xval = candlechart.getXAxis();
                        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xval.setDrawGridLines(false);
                       candlechart.setDescription("");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemDetails.this, "ERROR IS"+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
             // add it to the RequestQueue
            queue.add(getRequest);
    }
    private void InitValue(String coin, dateObject dateobj, String days){

        String date = dateobj.dayNumber+"-"+dateobj.getMonthNumber()+"-"+dateobj.year;
        String url = "https://api.coingecko.com/api/v3/coins/"+coin+"/history?date="+date+"&localization=false";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject market_data = response.getJSONObject("market_data");
                    JSONObject current_price = market_data.getJSONObject("current_price");
                    double doubleCurrentPrice = Double.parseDouble(current_price.getString(local_currency));
                    String currentPrice = localCurrencySymbol+df2.format(doubleCurrentPrice);
                    iniPrice = doubleCurrentPrice;
                    tvIniValue.setText(currentPrice);
                    loadDifferenceRate(doubleCurrentPrice, days);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ItemDetails.this, "ERROR IS"+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(getRequest);

    }
    private void loadDifferenceRate(double iniPrice, String days){
        if (days.equals("1")){
            double changerate = extras.getDouble("changeRate");
            tvchange.setText("%"+df2.format(changerate));

            tvIniValue.setText(localCurrencySymbol+df2.format(floatValuefordayone));
            if (changerate >= 0){
                arrowImage.setImageResource(R.drawable.greenarrow);
            }else{
                arrowImage.setImageResource(R.drawable.redarrowdown);
            }

        }else{
            if (currentPrice>=iniPrice){
                arrowImage.setImageResource(R.drawable.greenarrow);
            }else{
                arrowImage.setImageResource(R.drawable.redarrowdown);
            }
            double changeRate = (((currentPrice-iniPrice)*100)/iniPrice);
            String formatValue = "%"+df2.format(changeRate);
            tvchange.setText(formatValue);
        }


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private void loadTargetPrice(){
        Target targetObject = targetDAO.findByName(currencyName);
        if (targetObject!=null) {
            double basePrice;
            double porcentage;
            double targetPrice;
            targetPrice = targetObject.getTarget_price();
            basePrice = targetObject.getBase_price();
            String basePricestring = localCurrencySymbol+df2.format(basePrice);
            String targetPricestring = localCurrencySymbol+df2.format(targetPrice);
            porcentage = (((currentPrice-basePrice)*100/(targetPrice-basePrice)));

            if (porcentage >= 0){
                iVArrowTarget.setImageResource(R.drawable.greenarrow);
            }else{
                iVArrowTarget.setImageResource(R.drawable.redarrowdown);
            }
            String porctengaeString = "%"+df2.format(porcentage);
            tvPriceBase.setText(basePricestring);
            tvTargetPriceDetails.setText(targetPricestring);
            tvChangeToTarget.setText(porctengaeString);
        }

    }
    private static class dateObject {
        //Thu Apr 07 13:30:00 GMT+02:00 2022
        String date;
        String dayName;
        String monthName;
        String dayNumber;
        String hour;
        String minute;
        String year;

        public dateObject(String date){
            this.date = date;
            this.dayName = makedayName(date);
            this.monthName = makemonthName(date);
            this.dayNumber = makedayNumber(date);
            this.hour = makeHour(date);
            this.minute = makeMinute(date);
            this.year = makeYear(date);
        }
        private String makedayName(String date){
            return date.substring(0, 3);
        }
        private String makemonthName(String date){
            return date.substring(4, 7);
        }
        private String makedayNumber(String date){
            return date.substring(8, 10);
        }
        private String makeHour(String date){
            return date.substring(11, 13);

        }
        private String makeMinute(String date){
            return date.substring(14, 16);
        }
        private String makeYear(String date){
            String mYear = "Error reading Date";
            if (date.length()>33)
            mYear = date.substring(30, 34);
            return mYear;
        }

        public String getMonthNumber(){
            switch(monthName){
                case("Jan"):return "01";
                case("Feb"):return "02";
                case("Mar"):return "03";
                case("Apr"):return "04";
                case("May"):return "05";
                case("Jun"):return "06";
                case("Jul"):return "07";
                case("Aug"):return "08";
                case("Sep"):return "09";
                case("Oct"):return "10";
                case("Nov"):return "11";
                case("Dec"):return "12";
                default: return "null";
            }
        }
    }


}