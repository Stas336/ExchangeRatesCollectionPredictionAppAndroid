package com.stasl.datacollectionandpredictionfinanceappandroid.api;

public class NBRBAPIParameters
{
    private int currencyID;
    private String startDate;
    private String endDate;

    public NBRBAPIParameters(int currencyID, String startDate, String endDate)
    {
        this.currencyID = currencyID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(int currencyID) {
        this.currencyID = currencyID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
