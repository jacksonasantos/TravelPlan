package com.jacksonasantos.travelplan.DAO;

import java.sql.Date;

public class Travel {
    private static final long serialVersionUID = 263383301108440384L;
    public int id;
    public String name;
    public Date departure_date;
    public Date return_date;
    public String note;
}