package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class Travel {
    public Long id;
    public String description;
    public Date departure_date;
    public Date return_date;
    public String note;
    public int status;

    public Travel() {
        this.id = id;
        this.description = description;
        this.departure_date = departure_date;
        this.return_date = return_date;
        this.note = note;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
}