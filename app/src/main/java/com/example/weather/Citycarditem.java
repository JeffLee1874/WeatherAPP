package com.example.weather;

public class Citycarditem {
    private String cityname;
    private String temwea;

    public Citycarditem(String cityname, String temwea)
    {
        this.cityname = cityname;
        this.temwea = temwea;
    }

    public String getCityname() {
        return cityname;
    }

    public String getTemwea() {
        return temwea;
    }
}
