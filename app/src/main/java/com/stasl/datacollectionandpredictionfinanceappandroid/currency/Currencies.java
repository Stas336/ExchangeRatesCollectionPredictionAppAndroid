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
    /*public int getCode(Currencies currencies) throws Exception {
        if (currencies == Currencies.USD)
        {
            return USD.code;
        }
        else if (currencies == Currencies.EUR)
        {
            return EUR.code;
        }
        else if (currencies == Currencies.RUB)
        {
            return RUB.code;
        }
        throw new Exception("Currency not found");
    }
    public int getCode(String currency) throws Exception {
        if (currency.equals("USD"))
        {
            return USD.code;
        }
        else if (currency.equals("EUR"))
        {
            return EUR.code;
        }
        else if (currency.equals("RUB"))
        {
            return RUB.code;
        }
        throw new Exception("Currency not found");
    }*/
}
