package com.example.weather;

public class City {

    private String cityname;
    private String provincename;
    private String id;

    public City(String cityname, String provincename, String id)
    {
        this.cityname = cityname;
        this.provincename = provincename;
        this.id = id;
    }

    public String getCityName()
    {
        return this.cityname;
    }

    public String getProvincename()
    {
        return this.provincename;
    }

    public String getId()
    {
        return this.id;
    }
}
