package com.jacksonasantos.travelplan.dao;

import java.util.Date;

public class Reservation {

    public Integer id;
    public Integer travel_id;
    public Integer accommodation_id;
    public String voucher_number;
    public Date checkin_date;
    public Date checkout_date;
    public String apto_type;
    public Double daily_rate;
    public Double other_rate;
    public Double reservation_amount;
    public Double amount_paid;
    public String note;
    public int status_reservation;

    public Reservation() {
        this.id = id;
        this.travel_id = travel_id;
        this.accommodation_id = accommodation_id;
        this.voucher_number = voucher_number;
        this.checkin_date = checkin_date;
        this.checkout_date = checkout_date;
        this.apto_type = apto_type;
        this.daily_rate = daily_rate;
        this.other_rate = other_rate;
        this.reservation_amount = reservation_amount;
        this.amount_paid = amount_paid;
        this.note = note;
        this.status_reservation = status_reservation;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() {return travel_id;}
    public void setTravel_id(Integer travel_id) {this.travel_id = travel_id;}

    public Integer getAccommodation_id() {return accommodation_id;}
    public void setAccommodation_id(Integer accommodation_id) {this.accommodation_id = accommodation_id;}

    public String getVoucher_number() {return voucher_number;}
    public void setVoucher_number(String voucher_number) {this.voucher_number = voucher_number; }

    public Date getCheckin_date() {return checkin_date;}
    public void setCheckin_date(Date checkin_date) {this.checkin_date = checkin_date;}

    public Date getCheckout_date() { return checkout_date;    }
    public void setCheckout_date(Date checkout_date) { this.checkout_date = checkout_date; }

    public String getApto_type() {return apto_type;}
    public void setApto_type(String apto_type) {this.apto_type = apto_type;}

    public Double getDaily_rate() {return daily_rate;}
    public void setDaily_rate(Double daily_rate) {this.daily_rate = daily_rate; }

    public Double getOther_rate() {return other_rate; }
    public void setOther_rate(Double other_rate) { this.other_rate = other_rate;}

    public Double getReservation_amount() {return reservation_amount; }
    public void setReservation_amount(Double reservation_amount) { this.reservation_amount = reservation_amount;}

    public Double getAmount_paid() {return amount_paid; }
    public void setAmount_paid(Double amount_paid) { this.amount_paid = amount_paid;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}

    public int getStatus_reservation() {return status_reservation;}
    public void setStatus_reservation(int status_reservation) {this.status_reservation = status_reservation;}

}
