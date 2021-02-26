package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public List<Reservation> fetchAllReservation( ) {
        List<Reservation> reservationList = new ArrayList<>();

        cursor = super.query(RESERVATION_TABLE, RESERVATION_COLUMNS, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = cursorToEntity(cursor);
                reservationList.add(reservation);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return reservationList;
    }

    public List<Reservation> fetchAllReservationByTravel(Integer travel_id) {
        final String[] selectionArgs = { String.valueOf(travel_id) };
        final String selection = RESERVATION_TRAVEL_ID + " = ?";
        List<Reservation> reservationList = new ArrayList<>();
        cursor = super.query(RESERVATION_TABLE, RESERVATION_COLUMNS, selection, selectionArgs, RESERVATION_TRAVEL_ID);
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

    protected Reservation cursorToEntity(Cursor cursor) {

        Reservation reservation = new Reservation();

        int idIndex;
        int travel_idIndex;
        int accommodation_idIndex;
        int voucher_numberIndex;
        int checkin_dateIndex;
        int checkout_dateIndex;
        int apto_typeIndex;
        int daily_rateIndex;
        int other_rateIndex;
        int reservation_amountIndex;
        int amount_paidIndex;
        int noteIndex;
        int status_reservationIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(RESERVATION_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(RESERVATION_ID);
                reservation.setId(cursor.getInt(idIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_TRAVEL_ID) != -1) {
                travel_idIndex = cursor.getColumnIndexOrThrow(RESERVATION_TRAVEL_ID);
                reservation.setTravel_id(cursor.getInt(travel_idIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_ACCOMMODATION_ID) != -1) {
                accommodation_idIndex = cursor.getColumnIndexOrThrow(RESERVATION_ACCOMMODATION_ID);
                reservation.setAccommodation_id(cursor.getInt(accommodation_idIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_VOUCHER_NUMBER) != -1) {
                voucher_numberIndex = cursor.getColumnIndexOrThrow(RESERVATION_VOUCHER_NUMBER);
                reservation.setVoucher_number(cursor.getString(voucher_numberIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_CHECKIN_DATE) != -1) {
                checkin_dateIndex = cursor.getColumnIndexOrThrow(RESERVATION_CHECKIN_DATE);
                reservation.setCheckin_date(Utils.dateParse(cursor.getString(checkin_dateIndex)));
            }
            if (cursor.getColumnIndex(RESERVATION_CHECKOUT_DATE) != -1) {
                checkout_dateIndex = cursor.getColumnIndexOrThrow(RESERVATION_CHECKOUT_DATE);
                reservation.setCheckout_date(Utils.dateParse(cursor.getString(checkout_dateIndex)));
            }
            if (cursor.getColumnIndex(RESERVATION_APTO_TYPE) != -1) {
                apto_typeIndex = cursor.getColumnIndexOrThrow(RESERVATION_APTO_TYPE);
                reservation.setApto_type(cursor.getString(apto_typeIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_DAILY_RATE) != -1) {
                daily_rateIndex = cursor.getColumnIndexOrThrow(RESERVATION_DAILY_RATE);
                reservation.setDaily_rate(cursor.getDouble(daily_rateIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_OTHER_RATE) != -1) {
                other_rateIndex = cursor.getColumnIndexOrThrow(RESERVATION_OTHER_RATE);
                reservation.setOther_rate(cursor.getDouble(other_rateIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_RESERVATION_AMOUNT) != -1) {
                reservation_amountIndex = cursor.getColumnIndexOrThrow(RESERVATION_RESERVATION_AMOUNT);
                reservation.setReservation_amount(cursor.getDouble(reservation_amountIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_AMOUNT_PAID) != -1) {
                amount_paidIndex = cursor.getColumnIndexOrThrow(RESERVATION_AMOUNT_PAID);
                reservation.setAmount_paid(cursor.getDouble(amount_paidIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_NOTE) != -1) {
                noteIndex = cursor.getColumnIndexOrThrow(RESERVATION_NOTE);
                reservation.setNote(cursor.getString(noteIndex));
            }
            if (cursor.getColumnIndex(RESERVATION_STATUS_RESERVATION) != -1) {
                status_reservationIndex = cursor.getColumnIndexOrThrow(RESERVATION_STATUS_RESERVATION);
                reservation.setStatus_reservation(cursor.getInt(status_reservationIndex));
            }
        }
        return reservation;
    }

    private void setContentValue(Reservation reservation) {

        initialValues = new ContentValues();
        initialValues.put(RESERVATION_ID, reservation.id);
        initialValues.put(RESERVATION_TRAVEL_ID, reservation.travel_id);
        initialValues.put(RESERVATION_ACCOMMODATION_ID, reservation.accommodation_id);
        initialValues.put(RESERVATION_VOUCHER_NUMBER, reservation.voucher_number);
        initialValues.put(RESERVATION_CHECKIN_DATE, Utils.dateFormat(reservation.checkin_date));
        initialValues.put(RESERVATION_CHECKOUT_DATE, Utils.dateFormat(reservation.checkout_date));
        initialValues.put(RESERVATION_APTO_TYPE, reservation.apto_type);
        initialValues.put(RESERVATION_DAILY_RATE, reservation.daily_rate);
        initialValues.put(RESERVATION_OTHER_RATE, reservation.other_rate);
        initialValues.put(RESERVATION_RESERVATION_AMOUNT, reservation.reservation_amount);
        initialValues.put(RESERVATION_AMOUNT_PAID, reservation.amount_paid);
        initialValues.put(RESERVATION_NOTE, reservation.note);
        initialValues.put(RESERVATION_STATUS_RESERVATION, reservation.status_reservation);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
