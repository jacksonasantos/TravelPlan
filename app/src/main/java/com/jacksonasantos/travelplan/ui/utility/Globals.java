package com.jacksonasantos.travelplan.ui.utility;

public class Globals{

    private static Globals instance;
    private Integer idVehicle;
    private boolean blFilterVehicle;
    private String txLanguage = "pt";
    private String txCountry = "BR";
    private Integer nrIdCurrency = 0;
    private String txMeasureConsumption = "km/l";
    private String txDateFormat = "yyyy-MM-dd HH:mm:ss";

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

    public String getDateFormat() { return this.txDateFormat; }
    public void setDateFormat(String df) { this.txDateFormat = df; }

    public static synchronized Globals getInstance() { if(instance==null){ instance=new Globals(); } return instance;
    }
}