package com.jacksonasantos.travelplan.dao;

public class VehicleStatisticsYearResponse {
    public int supply_reason_type;
    public int ano1;
    public int ano2;
    public int ano3;
    public int ano4;
    public int total_type;

    public VehicleStatisticsYearResponse() { }

    public int getSupply_reason_type() {return supply_reason_type; }
    public void setSupply_reason_type(int supply_reason_type) { this.supply_reason_type = supply_reason_type; }

    public int getAno1() {
        return ano1;
    }
    public void setAno1(int ano1) { this.ano1 = ano1; }

    public int getAno2() {
        return ano2;
    }
    public void setAno2(int ano2) { this.ano2 = ano2; }

    public int getAno3() {
        return ano3;
    }
    public void setAno3(int ano3) { this.ano3 = ano3; }

    public int getAno4() {
        return ano4;
    }
    public void setAno4(int ano4) { this.ano4 = ano4; }

    public int getTotal_type() {
        return total_type;
    }
    public void setTotal_type(int total_type) { this.total_type = total_type; }
}
