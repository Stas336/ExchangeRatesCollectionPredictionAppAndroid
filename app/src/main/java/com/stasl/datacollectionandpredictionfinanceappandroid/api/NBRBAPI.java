package com.stasl.datacollectionandpredictionfinanceappandroid.api;


import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NBRBAPI
{
    @GET("Rates")
    Call<List<Currency>> getAllCurrencyRates(@Query("Periodicity") int periodicity);
    @GET("Rates")
    Call<List<Currency>> getAllCurrencyRatesByDate(@Query("Periodicity") int periodicity, @Query("onDate") String date); // 2016-7-6 yyyy-mm-dd
    @GET("Rates/{currencyID}")
    Call<Currency> getCurrencyRateByInternalCurrencyCode(@Path("currencyID") String currencyID); // 145
    @GET("Rates/{currencyID}")
    Call<Currency> getCurrencyRateByInternalCurrencyCodeAndDate(@Path("currencyID") String currencyID, @Query("onDate") String date);
    @GET("Rates/{currencyID}")
    Call<Currency> getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCode(@Path("currencyID") String currencyID, @Query("ParamMode") int paramMode); // 840 1 OR USD 2
    @GET("Rates/{currencyID}")
    Call<Currency> getCurrencyRateByDigitalCurrencyCodeOrByAlphabeticCurrencyCodeAndDate(@Path("currencyID") String currencyID, @Query("ParamMode") int paramMode, @Query("onDate") String date);
    @GET("Rates/Dynamics/{currencyID}") // Only from 2016-07-01
    Call<List<Currency>> getCurrencyRateByInternalCurrencyCodeAndStartEndDate(@Path("currencyID") String currencyID, @Query("startDate") String startDate, @Query("endDate") String endDate);
}
