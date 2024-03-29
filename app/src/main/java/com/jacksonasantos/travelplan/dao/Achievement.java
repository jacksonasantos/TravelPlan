package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;

public class Achievement {
    public Integer id;
    public String short_name;
    public String name;
    public byte[] image;
    public String city;
    public String state;
    public String latlng_source;
    public String city_end;
    public String state_end;
    public String latlng_target;
    public String country;
    public String note;
    public String latlng_achievement;
    public double length_achievement;
    public int status_achievement;
    public int type_achievement;

    public Achievement() {
    }

    @NonNull
    @Override
    public String toString() { return name; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getShort_name() {return short_name;}
    public void setShort_name(String short_name) {this.short_name = short_name;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public byte[] getImage() {return image;}
    public void setImage(byte[] image) {this.image = image;}

    public String getCity() {return city;}
    public void setCity(String city) {this.city = city; }

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}

    public String getLatlng_source() {return latlng_source;}
    public void setLatlng_source(String latlng_source) {this.latlng_source = latlng_source;}

    public String getCity_end() {return city_end;}
    public void setCity_end(String city_end) {this.city_end = city_end; }

    public String getState_end() {return state_end;}
    public void setState_end(String state_end) {this.state_end = state_end;}

    public String getLatlng_target() {return latlng_target;}
    public void setLatlng_target(String latlng_target) {this.latlng_target = latlng_target;}

    public String getCountry() { return country;    }
    public void setCountry(String country) { this.country = country; }

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note; }

    public String getLatlng_achievement() {return latlng_achievement;}
    public void setLatlng_achievement(String latlng_achievement) {this.latlng_achievement = latlng_achievement;}

    public double getLength_achievement() {return length_achievement;}
    public void setLength_achievement(double length) {this.length_achievement = length;}

    public int getStatus_achievement() {return status_achievement;}
    public void setStatus_achievement(int status_achievement) {this.status_achievement = status_achievement;}

    public int getType_achievement() {return type_achievement;}
    public void setType_achievement(int type_achievement) {this.type_achievement = type_achievement;}

    public int getAchievement_typeImage( int type_achievement ) {
        int draw;
        switch(type_achievement) {
            case 1: draw = R.drawable.ic_achievement_mountain_range; break;
            case 2: draw = R.drawable.ic_achievement_road; break;
            case 3: draw = R.drawable.ic_achievement_cave; break;
            case 4: draw = R.drawable.ic_achievement_tourist_spot; break;
            case 5: draw = R.drawable.ic_achievement_waterfall; break;
            case 6: draw = R.drawable.ic_achievement_church; break;
            case 7: draw = R.drawable.ic_achievement_park; break;
            case 8: draw = R.drawable.ic_achievement_castle; break;
            case 9: draw = R.drawable.ic_achievement_museum; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }
}
