package com.stasl.datacollectionandpredictionfinanceappandroid.currency;

public class Currency
{
    private String Cur_Abbreviation;

    private String Date;

    private String Cur_Scale;

    private String Cur_ID;

    private String Cur_OfficialRate;

    private String Cur_Name;

    public String getCur_Abbreviation ()
    {
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation (String Cur_Abbreviation)
    {
        this.Cur_Abbreviation = Cur_Abbreviation;
    }

    public String getDate ()
    {
        return Date;
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getCur_Scale ()
    {
        return Cur_Scale;
    }

    public void setCur_Scale (String Cur_Scale)
    {
        this.Cur_Scale = Cur_Scale;
    }

    public String getCur_ID ()
    {
        return Cur_ID;
    }

    public void setCur_ID (String Cur_ID)
    {
        this.Cur_ID = Cur_ID;
    }

    public String getCur_OfficialRate ()
    {
        return Cur_OfficialRate;
    }

    public void setCur_OfficialRate (String Cur_OfficialRate)
    {
        this.Cur_OfficialRate = Cur_OfficialRate;
    }

    public String getCur_Name ()
    {
        return Cur_Name;
    }

    public void setCur_Name (String Cur_Name)
    {
        this.Cur_Name = Cur_Name;
    }

    @Override
    public String toString()
    {
        return "Currency [Cur_Abbreviation = "+Cur_Abbreviation+", Date = "+Date+", Cur_Scale = "+Cur_Scale+", Cur_ID = "+Cur_ID+", Cur_OfficialRate = "+Cur_OfficialRate+", Cur_Name = "+Cur_Name+"]";
    }
}
