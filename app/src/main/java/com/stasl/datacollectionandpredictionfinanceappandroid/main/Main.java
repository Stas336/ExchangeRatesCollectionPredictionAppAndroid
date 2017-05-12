package com.stasl.datacollectionandpredictionfinanceappandroid.main;

import com.stasl.datacollectionandpredictionfinanceappandroid.api.NBRBAPI;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currencies;
import com.stasl.datacollectionandpredictionfinanceappandroid.predict.Predict;
import com.stasl.datacollectionandpredictionfinanceappandroid.currency.Currency;

import org.joda.time.DateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weka.core.Instances;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main
{
    private static final String baseURL = "http://www.nbrb.by/API/ExRates/";
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String predictStartDate = "2016-07-01";

    public static void main(String[] args)
    {
        System.out.println("Select action:");
        System.out.println("1. Get all currency rates daily");
        System.out.println("2. Get all currency rates daily by date");
        System.out.println("3. Get all currency rates monthly");
        System.out.println("4. Get all currency rates monthly by date");
        System.out.println("5. Get currency rate");
        System.out.println("6. Get currency rate by date");
        System.out.println("7. Predict");
        System.out.println("8. Prediction test");
        try
        {
            switch (Integer.parseInt(ValueReader.readValue()))
            {
                case 1:
                    for (Currency currency:getAllCurrencyRatesDaily())
                    {
                        showCurrencyInfo(currency);
                    }
                    break;
                case 2:
                    System.out.println("Enter date");
                    for (Currency currency:getAllCurrencyRatesDailyByDate(ValueReader.readValue()))
                    {
                        showCurrencyInfo(currency);
                    }
                    break;
                case 3:
                    for (Currency currency:getAllCurrencyRatesMonthly())
                    {
                        showCurrencyInfo(currency);
                    }
                    break;
                case 4:
                    System.out.println("Enter date");
                    for (Currency currency:getAllCurrencyRatesMonthlyByDate(ValueReader.readValue()))
                    {
                        showCurrencyInfo(currency);
                    }
                    break;
                case 5:
                    System.out.println("Enter currency from list");
                    System.out.println(Arrays.asList(Currencies.values()));
                    showCurrencyInfo(getCurrencyRateByAlphabeticCurrencyCode(ValueReader.readValue()));
                    break;
                case 6:
                    System.out.println("Enter currency from list");
                    System.out.println(Arrays.asList(Currencies.values()));
                    String currency = ValueReader.readValue();
                    System.out.println("Enter date");
                    showCurrencyInfo(getCurrencyRateByAlphabeticCurrencyCodeAndDate(currency, ValueReader.readValue()));
                    break;
                case 7:
                    System.out.println("Enter currency from list");
                    System.out.println(Arrays.asList(Currencies.values()));
                    String currencyID = ValueReader.readValue();
                    System.out.println("Enter steps");
                    int steps = Integer.parseInt(ValueReader.readValue());
                    SimpleDateFormat date = new SimpleDateFormat(dateFormat);
                    Map<String, Double> data = new HashMap<>();
                    System.out.println("Received Data");
                    for (Currency currency1:getCurrencyRateByInternalCurrencyCodeAndStartEndDate(String.valueOf(Currencies.valueOf("USD").getCode()), predictStartDate, date.format(new Date())))
                    {
                        System.out.println(date.format(date.parse(currency1.getDate())) + " " + currency1.getCur_OfficialRate());
                        data.put(date.format(date.parse(currency1.getDate())), Double.valueOf(currency1.getCur_OfficialRate()));
                    }
                    Instances trainingSet = Predict.initializeTrainingSet(data);
                    System.out.println("Predicted Data");
                    Predict.predict(trainingSet, Predict.initializeForecaster(trainingSet), steps);
                    break;
                case 8: // TODO NOT WORKING PROPERLY
                    date = new SimpleDateFormat(dateFormat);
                    DateTime dateTime = new DateTime("2017-03-01");
                    int forecastSteps = 30;
                    data = new HashMap<>();
                    for (Currency currency1:getCurrencyRateByInternalCurrencyCodeAndStartEndDate(String.valueOf(Currencies.valueOf("USD").getCode()), predictStartDate, dateTime.toString(dateFormat)))
                    {
                        System.out.println(date.format(date.parse(currency1.getDate())) + " " + currency1.getCur_OfficialRate());
                        data.put(date.format(date.parse(currency1.getDate())), Double.valueOf(currency1.getCur_OfficialRate()));
                    }
                    trainingSet = Predict.initializeTrainingSet(data);
                    System.out.println("Real Data");
                    for (Currency currency1:getCurrencyRateByInternalCurrencyCodeAndStartEndDate(String.valueOf(Currencies.valueOf("USD").getCode()), dateTime.plusDays(1).toString(dateFormat), dateTime.plusDays(forecastSteps).toString(dateFormat)))
                    {
                        System.out.println(date.format(date.parse(currency1.getDate())) + " " + currency1.getCur_OfficialRate());
                        data.put(date.format(date.parse(currency1.getDate())), Double.valueOf(currency1.getCur_OfficialRate()));
                    }
                    System.out.println("Predicted Data");
                    Map<String,Double> result = Predict.predict(trainingSet, Predict.initializeForecaster(trainingSet), forecastSteps);
                    System.out.println("Checking");
                    int i = 2;
                    int trueAnswers = 0;
                    int falseAnswers = 0;
                    for (String result1:result.keySet())
                    {
                        if (data.get(dateTime.plusDays(i).toString(dateFormat)) > data.get(dateTime.plusDays(i - 1).toString(dateFormat)) && result.get(dateTime.plusDays(i).toString(dateFormat)) > result.get(dateTime.plusDays(i - 1).toString(dateFormat)))
                        {
                            System.out.printf("Up true. (Real Data: %f, Predicted Data: %f) > %f\n", data.get(dateTime.plusDays(i).toString(dateFormat)), result.get(dateTime.plusDays(i).toString(dateFormat)), data.get(dateTime.plusDays(i - 1).toString(dateFormat)));
                            trueAnswers++;
                        }
                        else if (data.get(dateTime.plusDays(i).toString(dateFormat)) < data.get(dateTime.plusDays(i - 1).toString(dateFormat)) && result.get(dateTime.plusDays(i).toString(dateFormat)) < result.get(dateTime.plusDays(i - 1).toString(dateFormat)))
                        {
                            System.out.printf("Down true. (Real Data: %f, Predicted Data: %f) < %f\n", data.get(dateTime.plusDays(i).toString(dateFormat)), result.get(dateTime.plusDays(i).toString(dateFormat)), data.get(dateTime.plusDays(i - 1).toString(dateFormat)));
                            trueAnswers++;
                        }
                        else
                        {
                            System.out.printf("False. (Real Data: %f, Predicted Data: %f) %f\n", data.get(dateTime.plusDays(i).toString(dateFormat)), result.get(dateTime.plusDays(i).toString(dateFormat)), data.get(dateTime.plusDays(i - 1).toString(dateFormat)));
                            falseAnswers++;
                        }
                        i++;
                        if (i + 1 == result.keySet().size())
                        {
                            double percentage = ((double)trueAnswers / (double)forecastSteps) * 100;
                            System.out.printf("Prediction percentage %f%%\n", percentage);
                            break;
                        }
                    }
                    break;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
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
}
