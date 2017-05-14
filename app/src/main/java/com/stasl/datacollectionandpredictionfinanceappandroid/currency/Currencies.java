package com.stasl.datacollectionandpredictionfinanceappandroid.currency;

public enum Currencies
{
    USD(145),EUR(292),RUB(298);
    private int code;

    Currencies(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }
}
