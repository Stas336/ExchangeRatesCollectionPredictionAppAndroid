package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.adapter.BankListAdapter;
import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPI;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;
import com.stasl.datacollectionandpredictionfinanceappandroid.parser.SelectBYCoursesParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String baseURL = "http://www.nbrb.by/API/ExRates/";
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String predictStartDate = "2016-07-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BankListAdapter adapter = null;
        SelectBYCoursesParser selectBYCoursesParser = new SelectBYCoursesParser();
        try {
            adapter = new BankListAdapter(this, selectBYCoursesParser.execute().get());
        } catch (InterruptedException | ExecutionException e)
        {
            e.printStackTrace();
        }
        ListView list = (ListView)findViewById(R.id.List);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        //String formattedDate = df.format(c.getTime());
        list.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null, false));
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(df.format(new Date()));
        list.setAdapter(adapter);
    }
    public static void showCurrencyInfo(Currency currency)
    {
        System.out.printf("For %s %s - %s BYN (%s)\n", currency.getCur_Scale(), currency.getCur_Abbreviation(), currency.getCur_OfficialRate(), currency.getDate());
    }
    public static Currency getCurrencyRateByDigitalCurrencyCode(String currencyID) throws IOException
    {
        return getAPI().getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCode(currencyID, 1).execute().body();
    }
    public static Currency getCurrencyRateByAlphabeticCurrencyCode(String currencyID) throws IOException
    {
        return getAPI().getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCode(currencyID, 2).execute().body();
    }
    public static Currency getCurrencyRateByDigitalCurrencyCodeAndDate(String currencyID, String date) throws IOException
    {
        return getAPI().getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCodeAndDate(currencyID, 1, date).execute().body();
    }
    public static Currency getCurrencyRateByAlphabeticCurrencyCodeAndDate(String currencyID, String date) throws IOException
    {
        return getAPI().getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCodeAndDate(currencyID, 2, date).execute().body();
    }
    public static List<Currency> getAllCurrencyRatesDaily() throws IOException
    {
        return getAPI().getAllCurrencyRates(0).execute().body();
    }
    public static List<Currency> getAllCurrencyRatesDailyByDate(String date) throws IOException
    {
        return getAPI().getAllCurrencyRatesByDate(0, date).execute().body();
    }
    public static List<Currency> getAllCurrencyRatesMonthly() throws IOException
    {
        return getAPI().getAllCurrencyRates(1).execute().body();
    }
    public static List<Currency> getAllCurrencyRatesMonthlyByDate(String date) throws IOException
    {
        return getAPI().getAllCurrencyRatesByDate(1, date).execute().body();
    }
    public static List<Currency> getCurrencyRateByInternalCurrencyCodeAndStartEndDate(String currencyID, String startDate, String endDate) throws IOException
    {
        return getAPI().getCurrencyRateByInternalCurrencyCodeAndStartEndDate(currencyID, startDate, endDate).execute().body();
    }

    public static NBRBAPI getAPI()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(NBRBAPI.class);
    }
    public static String getDateFormat()
    {
        return dateFormat;
    }
}
