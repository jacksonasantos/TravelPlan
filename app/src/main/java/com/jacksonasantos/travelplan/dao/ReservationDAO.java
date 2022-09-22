package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.ReservationISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReservationDAO extends DbContentProvider implements ReservationISchema, ReservationIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public ReservationDAO(SQLiteDatabase db) {
        super(db);
    }

    public Reservation fetchReservationById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = RESERVATION_ID + " = ?";
        Reservation reservation = new Reservation();
        cursor = super.query(RESERVATION_TABLE, RESERVATION_COLUMNS, selection, selectionArgs, RESERVATION_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                reservation = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return reservation;
    }

    public List<Reservation> fetchAllReservationByTravel(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = RESERVATION_TRAVEL_ID + " = ?";
        List<Reservation> reservationList = new ArrayList<>();
        cursor = super.query(RESERVATION_TABLE, RESERVATION_COLUMNS, selection, selectionArgs, RESERVATION_CHECKIN_DATE);
        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = cursorToEntity(cursor);
                reservationList.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reservationList;
    }

    public void deleteReservation(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = RESERVATION_ID + " = ?";
        super.delete(RESERVATION_TABLE, selection, selectionArgs);
    }

    public boolean updateReservation(Reservation reservation) {
        setContentValue(reservation);
        final String[] selectionArgs = { String.valueOf(reservation.getId()) };
        final String selection = RESERVATION_ID + " = ?";
        return (super.update(RESERVATION_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addReservation(Reservation reservation) {
        setContentValue(reservation);
        return (super.insert(RESERVATION_TABLE, getContentValue()) > 0);
    }

    protected Reservation cursorToEntity(Cursor c) {
        Reservation r = new Reservation();
        if (c != null) {
            if (c.getColumnIndex(RESERVATION_ID) != -1)                 {r.setId(c.getInt(c.getColumnIndexOrThrow(RESERVATION_ID))); }
            if (c.getColumnIndex(RESERVATION_TRAVEL_ID) != -1)          {r.setTravel_id(c.getInt(c.getColumnIndexOrThrow(RESERVATION_TRAVEL_ID))); }
            if (c.getColumnIndex(RESERVATION_ACCOMMODATION_ID) != -1)   {r.setAccommodation_id(c.getInt(c.getColumnIndexOrThrow(RESERVATION_ACCOMMODATION_ID))); }
            if (c.getColumnIndex(RESERVATION_VOUCHER_NUMBER) != -1)     {r.setVoucher_number(c.getString(c.getColumnIndexOrThrow(RESERVATION_VOUCHER_NUMBER))); }
            if (c.getColumnIndex(RESERVATION_CHECKIN_DATE) != -1)       {r.setCheckin_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(RESERVATION_CHECKIN_DATE)))); }
            if (c.getColumnIndex(RESERVATION_CHECKOUT_DATE) != -1)      {r.setCheckout_date(Utils.dateParse(c.getString(c.getColumnIndexOrThrow(RESERVATION_CHECKOUT_DATE)))); }
            if (c.getColumnIndex(RESERVATION_APT_TYPE) != -1)           {r.setApt_type(c.getString(c.getColumnIndexOrThrow(RESERVATION_APT_TYPE))); }
            if (c.getColumnIndex(RESERVATION_DAILY_RATE) != -1)         {r.setDaily_rate(c.getDouble(c.getColumnIndexOrThrow(RESERVATION_DAILY_RATE))); }
            if (c.getColumnIndex(RESERVATION_OTHER_RATE) != -1)         {r.setOther_rate(c.getDouble(c.getColumnIndexOrThrow(RESERVATION_OTHER_RATE))); }
            if (c.getColumnIndex(RESERVATION_RESERVATION_AMOUNT) != -1) {r.setReservation_amount(c.getDouble(c.getColumnIndexOrThrow(RESERVATION_RESERVATION_AMOUNT))); }
            if (c.getColumnIndex(RESERVATION_AMOUNT_PAID) != -1)        {r.setAmount_paid(c.getDouble(c.getColumnIndexOrThrow(RESERVATION_AMOUNT_PAID))); }
            if (c.getColumnIndex(RESERVATION_NOTE) != -1)               {r.setNote(c.getString(c.getColumnIndexOrThrow(RESERVATION_NOTE))); }
            if (c.getColumnIndex(RESERVATION_STATUS_RESERVATION) != -1) {r.setStatus_reservation(c.getInt(c.getColumnIndexOrThrow(RESERVATION_STATUS_RESERVATION))); }
        }
        return r;
    }

    private void setContentValue(Reservation r) {
        initialValues = new ContentValues();
        initialValues.put(RESERVATION_ID, r.id);
        initialValues.put(RESERVATION_TRAVEL_ID, r.travel_id);
        initialValues.put(RESERVATION_ACCOMMODATION_ID, r.accommodation_id);
        initialValues.put(RESERVATION_VOUCHER_NUMBER, r.voucher_number);
        initialValues.put(RESERVATION_CHECKIN_DATE, Utils.dateFormat(r.checkin_date));
        initialValues.put(RESERVATION_CHECKOUT_DATE, Utils.dateFormat(r.checkout_date));
        initialValues.put(RESERVATION_APT_TYPE, r.apt_type);
        initialValues.put(RESERVATION_DAILY_RATE, r.daily_rate);
        initialValues.put(RESERVATION_OTHER_RATE, r.other_rate);
        initialValues.put(RESERVATION_RESERVATION_AMOUNT, r.reservation_amount);
        initialValues.put(RESERVATION_AMOUNT_PAID, r.amount_paid);
        initialValues.put(RESERVATION_NOTE, r.note);
        initialValues.put(RESERVATION_STATUS_RESERVATION, r.status_reservation);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
