package com.jacksonasantos.travelplan.ui.utility;

public class Globals{

    private static Globals instance;
    private Long idVehicle;
    private boolean blFilterVehicle;
    private String txLanguage = "pt";
    private String txCountry = "BR";
    private String txMeasureConsumption = "km/l";

    private Globals() { }

    public Long getIdVehicle() { return this.idVehicle; }
    public void setIdVehicle(Long id) { this.idVehicle = id; }

    public Boolean getFilterVehicle() { return this.blFilterVehicle; }
    public void setFilterVehicle(Boolean filter) { this.blFilterVehicle = filter; }

    public String getLanguage() { return this.txLanguage; }
    public void setLanguage(String lang) { this.txLanguage = lang; }

    public String getCountry() { return this.txCountry; }
    public void setCountry(String country) { this.txCountry = country; }

    public String getMeasureConsumption() { return this.txMeasureConsumption; }
    public void setMeasureConsumption(String MeasureConsumption) { this.txMeasureConsumption = MeasureConsumption; }

    public static synchronized Globals getInstance() { if(instance==null){ instance=new Globals(); } return instance;
    }
}