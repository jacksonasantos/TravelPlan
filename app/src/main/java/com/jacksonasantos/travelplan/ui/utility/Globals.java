package com.jacksonasantos.travelplan.ui.utility;


public class Globals{

    private static Globals instance;
    private Long idVehicle;
    private boolean blFilterVehicle;

    private Globals() { }

    public Long getIdVehicle() { return this.idVehicle; }
    public void setIdVehicle(Long id) { this.idVehicle = id; }

    public Boolean getFilterVehicle() { return this.blFilterVehicle; }
    public void setFilterVehicle(Boolean filter) { this.blFilterVehicle = filter; }

    public static synchronized Globals getInstance() { if(instance==null){ instance=new Globals(); } return instance;
    }
}