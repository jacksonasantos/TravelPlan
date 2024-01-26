package com.jacksonasantos.travelplan.dao;

public class AchievementResume {

    public String country;
    public String state;
    public int type_achievement;
    public int vl_conquered;
    public int vl_total;
    public float perc_conquered;

    public AchievementResume() { }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public int getType_achievement() { return type_achievement; }
    public void setType_achievement(int type_achievement) { this.type_achievement = type_achievement; }

    public int getVl_conquered() { return vl_conquered; }
    public void setVl_conquered(int vl_conquered) { this.vl_conquered = vl_conquered; }

    public int getVl_total() { return vl_total; }
    public void setVl_total(int vl_total) {this.vl_total = vl_total; }

    public float getPerc_conquered() {return perc_conquered; }
    public void setPerc_conquered(float perc_conquered) {this.perc_conquered = perc_conquered; }
}
