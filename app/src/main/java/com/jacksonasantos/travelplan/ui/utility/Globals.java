package com.jacksonasantos.travelplan.ui.utility;

public class Globals {

    private static Globals instance;

    private Integer idVehicle;
    private boolean blFilterVehicle;
    private String txLanguage = "pt";
    private String txCountry = "BR";
    private Integer nrIdCurrency;
    private String txMeasureCost;
    private int nrMeasureIndexInMeter;
    private String txMeasureCapacity;
    private String txMeasureConsumption;
    private String txDateFormat;
    private String txTimeFormat;
    private String txLatitudeHome;
    private String txLongitudeHome;
    private int nrKMsPreviousAlert;
    private int nrDaysPreviousAlert;

    private Globals() { }

    public Integer getIdVehicle() { return this.idVehicle; }
    public void setIdVehicle(Integer id) { this.idVehicle = id; }

    public Boolean getFilterVehicle() { return this.blFilterVehicle; }
    public void setFilterVehicle(Boolean filter) { this.blFilterVehicle = filter; }

    public String getLanguage() { return this.txLanguage; }
    public void setLanguage(String lang) { this.txLanguage = lang; }

    public String getCountry() { return this.txCountry; }
    public void setCountry(String country) { this.txCountry = country; }

    public Integer getIdCurrency() { return this.nrIdCurrency; }
    public void setIdCurrency(Integer idCurrency) { this.nrIdCurrency = idCurrency; }

    public String getMeasureConsumption() { return this.txMeasureConsumption; }
    public void setMeasureConsumption(String MeasureConsumption) { this.txMeasureConsumption = MeasureConsumption; }

    public String getMeasureCost() { return this.txMeasureCost; }
    public void setMeasureCost(String MeasureCost) { this.txMeasureCost = MeasureCost; }

    public int getMeasureIndexInMeter() { return this.nrMeasureIndexInMeter; }
    public void setMeasureIndexInMeter(int MeasureIndexInMeter) { this.nrMeasureIndexInMeter = MeasureIndexInMeter; }

    public String getMeasureCapacity() { return this.txMeasureCapacity; }
    public void setMeasureCapacity(String MeasureCapacity) { this.txMeasureCapacity = MeasureCapacity; }

    public String getDateFormat() { return this.txDateFormat; }
    public void setDateFormat(String df) { this.txDateFormat = df; }

    public String getTimeFormat() { return this.txTimeFormat; }
    public void setTimeFormat(String tf) { this.txTimeFormat = tf; }

    public String getLatitudeHome() { return txLatitudeHome; }
    public void setLatitudeHome(String txLatitudeHome) { this.txLatitudeHome = txLatitudeHome; }

    public String getLongitudeHome() { return txLongitudeHome;}
    public void setLongitudeHome(String txLongitudeHome) { this.txLongitudeHome = txLongitudeHome;}

    public int getKMsPreviousAlert() { return this.nrKMsPreviousAlert; }
    public void setKMsPreviousAlert(int nrKMsPreviousAlert) { this.nrKMsPreviousAlert = nrKMsPreviousAlert; }

    public int getDaysPreviousAlert() { return this.nrDaysPreviousAlert; }
    public void setDaysPreviousAlert(int nrDaysPreviousAlert) { this.nrDaysPreviousAlert = nrDaysPreviousAlert; }

    public static synchronized Globals getInstance() { if(instance==null){ instance=new Globals(); } return instance;
    }
}