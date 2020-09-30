package com.jacksonasantos.travelplan.ui.utility;


public class Globals{

    private static Globals instance;

    private Long idVehicle;

    private Globals() { }

    public Long getIdVehicle() { return this.idVehicle; }

    public void setIdVehicle(Long id) { this.idVehicle = id; }

    public static synchronized Globals getInstance() { if(instance==null){ instance=new Globals(); } return instance;
    }
}