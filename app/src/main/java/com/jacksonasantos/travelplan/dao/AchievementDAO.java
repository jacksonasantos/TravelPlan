package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementISchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AchievementDAO extends DbContentProvider implements AchievementISchema, AchievementIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public AchievementDAO(SQLiteDatabase db) {
        super(db);
    }

    public Achievement fetchAchievementById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACHIEVEMENT_ID + " = ?";
        Achievement achievement = new Achievement();
        cursor = super.query(ACHIEVEMENT_TABLE, ACHIEVEMENT_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                achievement = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return achievement;
    }

    public List<Achievement> fetchAllAchievement() {
        List<Achievement> achievementList = new ArrayList<>();
        cursor = super.query(ACHIEVEMENT_TABLE, ACHIEVEMENT_COLUMNS, null,null, ACHIEVEMENT_NAME);
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = cursorToEntity(cursor);
                achievementList.add(achievement);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return achievementList;
    }

    public List<Marker> fetchAllAchievementWithTravel(Integer achievement_id ) {
        final String[] selectionArgs = { String.valueOf(achievement_id), String.valueOf(achievement_id) };
        List<Marker> markerList = new ArrayList<>();
        cursor = super.rawQuery("SELECT m.* " +
                                     " FROM " + MarkerDAO.MARKER_TABLE + " m " +
                                     " JOIN " + ACHIEVEMENT_TABLE + " a ON M." + MarkerDAO.MARKER_ACHIEVEMENT_ID + " = a." + ACHIEVEMENT_ID +
                                    " WHERE m." + MarkerDAO.MARKER_ACHIEVEMENT_ID + " = ? " +
                                    " UNION " +
                                   " SELECT m.* " +
                                     " FROM " + MarkerDAO.MARKER_TABLE + " m " +
                                     " JOIN " + TourDAO.TOUR_TABLE + " t ON M." + MarkerDAO.MARKER_ID + " = t." + TourDAO.TOUR_MARKER_ID +
                                    " WHERE t." + TourDAO.TOUR_ACHIEVEMENT_ID + " = ? "
                , selectionArgs);
        if (cursor.moveToFirst()) {
            do {
                Marker marker = Database.mMarkerDao.cursorToEntity(cursor);
                markerList.add(marker);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return markerList;
    }

   public List<Achievement> fetchAllAchievementByTravel( Integer travel_id ) {
       final String[] selectionArgs = { String.valueOf(travel_id), String.valueOf(travel_id) };
       List<Achievement> achievementList = new ArrayList<>();
       cursor = super.rawQuery(
           "SELECT a.* " +
                " FROM " + MarkerDAO.MARKER_TABLE + " m " +
                " JOIN " + ACHIEVEMENT_TABLE + " a ON M." + MarkerDAO.MARKER_ACHIEVEMENT_ID + " = a." + ACHIEVEMENT_ID +
               " WHERE m." + MarkerDAO.MARKER_TRAVEL_ID + " = ? " +
                 " AND m." + MarkerDAO.MARKER_ACHIEVEMENT_ID + " IS NOT NULL" +
               " UNION " +
              " SELECT a.* " +
                " FROM " + TourDAO.TOUR_TABLE + " t " +
                " JOIN " + ACHIEVEMENT_TABLE + " a ON t." + TourDAO.TOUR_ACHIEVEMENT_ID + " = a." + ACHIEVEMENT_ID +
               " WHERE t." + TourDAO.TOUR_TRAVEL_ID + " = ? " +
                 " AND t." + TourDAO.TOUR_ACHIEVEMENT_ID + " IS NOT NULL"
               , selectionArgs);
       if (cursor.moveToFirst()) {
           do {
               Achievement achievement = cursorToEntity(cursor);
               achievementList.add(achievement);
           } while (cursor.moveToNext());
           cursor.close();
       }
       return achievementList;
    }

   public List<Achievement> fetchAllAchievementByStatusAchievement( Integer status ) {
        final String[] selectionArgs = status==null ? null : new String[]{String.valueOf(status)};
        final String selection = status==null ? null : ACHIEVEMENT_STATUS_ACHIEVEMENT + " = ?";
        List<Achievement> achievementList = new ArrayList<>();
        cursor = super.query(ACHIEVEMENT_TABLE, ACHIEVEMENT_COLUMNS, selection, selectionArgs, ACHIEVEMENT_NAME);
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = cursorToEntity(cursor);
                achievementList.add(achievement);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return achievementList;
   }

   public List<Achievement> fetchAllAchievementByTypeAchievement( Integer type ) {
        final String[] selectionArgs;
        final String selection;

        if (type==null) {
            selectionArgs =  null;
            selection = null;
        //} else if (type==0) {
        //    selectionArgs = null;
        //    selection = ACHIEVEMENT_TYPE_ACHIEVEMENT + " IS NULL";
        } else {
            selectionArgs = new String[]{String.valueOf(type)};
            selection = ACHIEVEMENT_TYPE_ACHIEVEMENT + " = ?";
            }

        List<Achievement> achievementList = new ArrayList<>();
        cursor = super.query(ACHIEVEMENT_TABLE, ACHIEVEMENT_COLUMNS, selection, selectionArgs, ACHIEVEMENT_NAME);
        if (cursor.moveToFirst()) {
            do {
                Achievement achievement = cursorToEntity(cursor);
                achievementList.add(achievement);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return achievementList;
   }

   public Cursor fetchArrayAchievement() {
        return super.rawQuery( "SELECT '' _id, '' text1 UNION " +
                                   "SELECT " + ACHIEVEMENT_ID + ", " +
                                              ACHIEVEMENT_NAME + " " +
                                    "FROM " + ACHIEVEMENT_TABLE + " " +
                                "ORDER BY " + ACHIEVEMENT_NAME, null);
   }

    public void deleteAchievement(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACHIEVEMENT_ID + " = ?";
        super.delete(ACHIEVEMENT_TABLE, selection, selectionArgs);
    }

    public boolean updateAchievement(Achievement achievement) {
        setContentValue(achievement);
        final String[] selectionArgs = { String.valueOf(achievement.getId()) };
        final String selection = ACHIEVEMENT_ID + " = ?";
        try {
            return (super.update(ACHIEVEMENT_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    public Integer addAchievement(Achievement achievement) {
        setContentValue(achievement);
        try {
            return Math.toIntExact(super.insert(ACHIEVEMENT_TABLE, getContentValue()));
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return -1;
        }
    }

    protected Achievement cursorToEntity(Cursor c) {
        Achievement a = new Achievement();
        if (c != null) {
            if (c.getColumnIndex(ACHIEVEMENT_ID) != -1)                   {a.setId(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_ID))); }
            if (c.getColumnIndex(ACHIEVEMENT_SHORT_NAME) != -1)           {a.setShort_name(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_SHORT_NAME))); }
            if (c.getColumnIndex(ACHIEVEMENT_NAME) != -1)                 {a.setName(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_NAME))); }
            if (c.getColumnIndex(ACHIEVEMENT_IMAGE) != -1)                {a.setImage(c.getBlob(c.getColumnIndexOrThrow(ACHIEVEMENT_IMAGE))); }
            if (c.getColumnIndex(ACHIEVEMENT_CITY) != -1)                 {a.setCity(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_CITY))); }
            if (c.getColumnIndex(ACHIEVEMENT_STATE) != -1)                {a.setState(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_STATE))); }
            if (c.getColumnIndex(ACHIEVEMENT_LATLNG_SOURCE) != -1)        {a.setLatlng_source(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_LATLNG_SOURCE))); }
            if (c.getColumnIndex(ACHIEVEMENT_CITY_END) != -1)             {a.setCity_end(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_CITY_END))); }
            if (c.getColumnIndex(ACHIEVEMENT_STATE_END) != -1)            {a.setState_end(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_STATE_END))); }
            if (c.getColumnIndex(ACHIEVEMENT_LATLNG_TARGET) != -1)        {a.setLatlng_target(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_LATLNG_TARGET))); }
            if (c.getColumnIndex(ACHIEVEMENT_COUNTRY) != -1)              {a.setCountry(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_COUNTRY))); }
            if (c.getColumnIndex(ACHIEVEMENT_NOTE) != -1)                 {a.setNote(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_NOTE))); }
            if (c.getColumnIndex(ACHIEVEMENT_LATLNG_ACHIEVEMENT) != -1)   {a.setLatlng_achievement(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_LATLNG_ACHIEVEMENT))); }
            if (c.getColumnIndex(ACHIEVEMENT_LENGTH_ACHIEVEMENT) != -1)   {a.setLength_achievement(c.getDouble(c.getColumnIndexOrThrow(ACHIEVEMENT_LENGTH_ACHIEVEMENT))); }
            if (c.getColumnIndex(ACHIEVEMENT_STATUS_ACHIEVEMENT) != -1)   {a.setStatus_achievement(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_STATUS_ACHIEVEMENT))); }
            if (c.getColumnIndex(ACHIEVEMENT_TYPE_ACHIEVEMENT) != -1)     {a.setType_achievement(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_TYPE_ACHIEVEMENT))); }
        }
        return a;
    }

    private void setContentValue(Achievement a) {
        initialValues = new ContentValues();
        initialValues.put(ACHIEVEMENT_ID, a.id);
        initialValues.put(ACHIEVEMENT_SHORT_NAME, a.short_name);
        initialValues.put(ACHIEVEMENT_NAME, a.name);
        initialValues.put(ACHIEVEMENT_IMAGE, a.image);
        initialValues.put(ACHIEVEMENT_CITY, a.city);
        initialValues.put(ACHIEVEMENT_STATE, a.state);
        initialValues.put(ACHIEVEMENT_LATLNG_SOURCE, a.latlng_source);
        initialValues.put(ACHIEVEMENT_CITY_END, a.city_end);
        initialValues.put(ACHIEVEMENT_STATE_END, a.state_end);
        initialValues.put(ACHIEVEMENT_LATLNG_TARGET, a.latlng_target);
        initialValues.put(ACHIEVEMENT_COUNTRY, a.country);
        initialValues.put(ACHIEVEMENT_NOTE, a.note);
        initialValues.put(ACHIEVEMENT_LATLNG_ACHIEVEMENT, a.latlng_achievement);
        initialValues.put(ACHIEVEMENT_LENGTH_ACHIEVEMENT, a.length_achievement);
        initialValues.put(ACHIEVEMENT_STATUS_ACHIEVEMENT, a.status_achievement);
        initialValues.put(ACHIEVEMENT_TYPE_ACHIEVEMENT, a.type_achievement);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}