package com.jacksonasantos.travelplan.dao;

import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.Date;

public class Travel {
    public Integer id;
    public String description;
    public Date departure_date;
    public Date return_date;
    public String note;
    public int status;

    public Travel() {
    }
    @NonNull
    @Override
    public String toString() { return description; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDeparture_date() { return departure_date; }
    public void setDeparture_date(Date departure_date) { this.departure_date = departure_date; }

    public Date getReturn_date() { return return_date; }
    public void setReturn_date(Date return_date) { this.return_date = return_date;}

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public int getColorStatus() {
        int vColor = Color.GRAY;
        if (status == 1 ) {vColor = Color.YELLOW; }
        else if (status == 2 ) {vColor = Color.GREEN; }
        else if (status == 3 ) {vColor = Color.BLUE; }
        return vColor;
    }
}