package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public ArrayList<Achievement> fetchArrayAchievement(){
        ArrayList<Achievement> achievementList = new ArrayList<>();
        Cursor cursor = super.query(ACHIEVEMENT_TABLE, ACHIEVEMENT_COLUMNS, null,null, ACHIEVEMENT_NAME);
        if(cursor != null && cursor.moveToFirst()){
            do {
                Achievement achievement = cursorToEntity(cursor);
                achievementList.add(achievement);
            } while(cursor.moveToNext());
        }
        return achievementList;
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

    public boolean addAchievement(Achievement achievement) {
        setContentValue(achievement);
        try {
            return (super.insert(ACHIEVEMENT_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    protected Achievement cursorToEntity(Cursor c) {
        Achievement a = new Achievement();
        if (c != null) {
            if (c.getColumnIndex(ACHIEVEMENT_ID) != -1)                   {a.setId(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_ID))); }
            if (c.getColumnIndex(ACHIEVEMENT_NAME) != -1)                 {a.setName(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_NAME))); }
            if (c.getColumnIndex(ACHIEVEMENT_CITY) != -1)                 {a.setCity(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_CITY))); }
            if (c.getColumnIndex(ACHIEVEMENT_STATE) != -1)                {a.setState(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_STATE))); }
            if (c.getColumnIndex(ACHIEVEMENT_COUNTRY) != -1)              {a.setCountry(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_COUNTRY))); }
            if (c.getColumnIndex(ACHIEVEMENT_STATUS) != -1)               {a.setStatus( c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_STATUS))); }
            if (c.getColumnIndex(ACHIEVEMENT_NOTE) != -1)                 {a.setNote(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_NOTE))); }
            if (c.getColumnIndex(ACHIEVEMENT_LATLNG_ACHIEVEMENT) != -1)   {a.setLatlng_achievement(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_LATLNG_ACHIEVEMENT))); }
        }
        return a;
    }

    private void setContentValue(Achievement a) {
        initialValues = new ContentValues();
        initialValues.put(ACHIEVEMENT_ID, a.id);
        initialValues.put(ACHIEVEMENT_NAME, a.name);
        initialValues.put(ACHIEVEMENT_CITY, a.city);
        initialValues.put(ACHIEVEMENT_STATE, a.state);
        initialValues.put(ACHIEVEMENT_COUNTRY, a.country);
        initialValues.put(ACHIEVEMENT_STATUS, a.status);
        initialValues.put(ACHIEVEMENT_NOTE, a.note);
        initialValues.put(ACHIEVEMENT_LATLNG_ACHIEVEMENT, a.latlng_achievement);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
