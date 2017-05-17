package com.stasl.datacollectionandpredictionfinanceappandroid.predict;

import android.util.Log;

import com.stasl.datacollectionandpredictionfinanceappandroid.activity.BankListActivity;

import org.joda.time.DateTime;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.timeseries.eval.TSEvaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.attribute.TSLagMaker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Predict
{
    private static Instance createInstance(String date, double price) throws Exception
    {
        if (date == null)
        {
            throw new Exception("Date is null");
        }
        else if (price < 0)
        {
            throw new Exception("Price should be > 0");
        }
        List<Attribute> attributes = getAttributes();
        Instance instance = new DenseInstance(attributes.size());
        Attribute att = new Attribute("date", BankListActivity.getDateFormat());
        instance.setValue(attributes.get(0), att.parseDate(date));
        instance.setValue(attributes.get(1), price);
        return instance;
    }
    private static void evaluateData(Instances trainingSet, WekaForecaster forecaster) throws Exception
    {
        if (trainingSet == null)
        {
            throw new Exception("Training set is null");
        }
        else if (forecaster == null)
        {
            throw new Exception("Forecaster is null");
        }
        // a new evaluation object (evaluation on the training data)
        TSEvaluation eval = new TSEvaluation(trainingSet, 0);
        // generate and evaluate predictions for up to 5 steps ahead
        eval.setHorizon(5);
        // prime with enough data to cover our maximum lag
        eval.setPrimeWindowSize(1);
        eval.evaluateForecaster(forecaster);
        Log.d("Training set evaluation", eval.toSummaryString());
    }
    private static ArrayList<Attribute> getAttributes()
    {
        Attribute time = new Attribute("date", BankListActivity.getDateFormat());
        Attribute price = new Attribute("price");
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(time);
        attributes.add(price);
        new Instances("", attributes, 1); // Workaround fix for m_index (-1 -> 0)
        return attributes;
    }
    public static Instances initializeTrainingSet(Map<String, Double> data) throws Exception
    {
        if (data == null)
        {
            throw new Exception("Data is null");
        }
        Instances trainingSet = new Instances("currencyPrediction", getAttributes(), 1);
        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
        while (trainingSet.size() != data.size())
        {
            for (String str:data.keySet())
            {
                trainingSet.add(createInstance(str, data.get(str)));
            }
        }
        trainingSet.sort(0);
        return trainingSet;
    }
    public static WekaForecaster initializeForecaster(Instances trainingSet) throws Exception
    {
        if (trainingSet == null)
        {
            throw new Exception("Training set is null");
        }
        WekaForecaster forecaster = new WekaForecaster();
        // set the targets we want to forecast. This method calls
        // setFieldsToLag() on the lag maker object for us
        forecaster.setFieldsToForecast("price");
        forecaster.setBaseForecaster(new GaussianProcesses()); // LinearRegression or GaussianProcesses or MultilayerPerceptron
        forecaster.getTSLagMaker().setTimeStampField("date"); // date time stamp
        forecaster.getTSLagMaker().setMinLag(1);
        forecaster.getTSLagMaker().setMaxLag(12); // monthly data 12 or 31
        forecaster.getTSLagMaker().setPeriodicity(TSLagMaker.Periodicity.DAILY);
        forecaster.buildForecaster(trainingSet);
        forecaster.primeForecaster(trainingSet);
        return forecaster;
    }
    public static Map<String,Double> predict(Instances trainingSet, WekaForecaster forecaster, int steps) throws Exception
    {
        if (trainingSet == null)
        {
            throw new Exception("Training set is null");
        }
        else if (forecaster == null)
        {
            throw new Exception("Forecaster is null");
        }
        else if (steps == 0)
        {
            throw new Exception("Steps should be > 0");
        }
        Map<String,Double> result = new HashMap<>();
        double predicted;
        DateTime currentDt = advanceTime(forecaster.getTSLagMaker(), getCurrentDateTime(forecaster.getTSLagMaker()));
        for (List<NumericPrediction> list:forecaster.forecast(steps))
        {
            for (NumericPrediction prediction:list)
            {
                predicted = new BigDecimal(prediction.predicted()).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                result.put(currentDt.toString(BankListActivity.getDateFormat()),predicted);
                Log.d("Prediction", currentDt.toString(BankListActivity.getDateFormat()) + " " + predicted);
                currentDt = advanceTime(forecaster.getTSLagMaker(), currentDt);
            }
        }
        return result;
    }
    private static DateTime getCurrentDateTime(TSLagMaker lm) throws Exception {
        return new DateTime((long)lm.getCurrentTimeStampValue());
    }
    private static DateTime advanceTime(TSLagMaker lm, DateTime dt) {
        return new DateTime((long)lm.advanceSuppliedTimeValue(dt.getMillis()));
    }
}
