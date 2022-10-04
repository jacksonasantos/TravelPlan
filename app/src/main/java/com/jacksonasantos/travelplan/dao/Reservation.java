package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;

import java.util.Date;

public class Reservation {

    public Integer id;
    public Integer travel_id;
    public Integer accommodation_id;
    public String voucher_number;
    public Date checkin_date;
    public Date checkout_date;
    public String apt_type;
    public double daily_rate;
    public double other_rate;
    public double reservation_amount;
    public double amount_paid;
    public String note;
    public int status_reservation;

    public Reservation() {
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

    public String getApt_type() {return apt_type;}
    public void setApt_type(String apt_type) {this.apt_type = apt_type;}

    public double getDaily_rate() {return daily_rate;}
    public void setDaily_rate(double daily_rate) {this.daily_rate = daily_rate; }

    public double getOther_rate() {return other_rate; }
    public void setOther_rate(double other_rate) { this.other_rate = other_rate;}

    public double getReservation_amount() {return reservation_amount; }
    public void setReservation_amount(double reservation_amount) { this.reservation_amount = reservation_amount;}

    public double getAmount_paid() {return amount_paid; }
    public void setAmount_paid(double amount_paid) { this.amount_paid = amount_paid;}

    public String getNote() {return note;}
    public void setNote(String note) {this.note = note;}

    public int getStatus_reservation() {return status_reservation;}
    public void setStatus_reservation(int status_reservation) {this.status_reservation = status_reservation;}

    public int getImage_Status_reservation( int status_reservation ) {
        int draw;
        switch(status_reservation) {
            case 0: draw = R.drawable.ic_send; break;
            case 1: draw = R.drawable.ic_executed; break;
            case 2: draw = R.drawable.ic_pay; break;
            case 3: draw = R.drawable.ic_done_all; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }
}
