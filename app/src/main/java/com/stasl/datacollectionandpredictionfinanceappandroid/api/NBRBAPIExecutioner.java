package com.stasl.datacollectionandpredictionfinanceappandroid.api;

import android.os.AsyncTask;

import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NBRBAPIExecutioner extends AsyncTask<NBRBAPIParameters, Void, List<Currency>>
{
    private static final String baseURL = "http://www.nbrb.by/API/ExRates/";

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

    @Override
    protected List<Currency> doInBackground(NBRBAPIParameters... params) {
        try {
            return getCurrencyRateByInternalCurrencyCodeAndStartEndDate(String.valueOf(params[0].getCurrencyID()), params[0].getStartDate(), params[0].getEndDate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
