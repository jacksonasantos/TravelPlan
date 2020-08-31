package com.jacksonasantos.travelplan.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.DAO.Interface.UserIDAO;
import com.jacksonasantos.travelplan.DAO.Interface.UserISchema;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;

public class UserDAO extends DbContentProvider implements UserISchema, UserIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public UserDAO(SQLiteDatabase db) {
        super(db);
    }

    public User fetchUserById(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ID + " = ?";
        User user = new User();
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection,
                selectionArgs, USER_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                user = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return user;
    }

    public List<User> fetchAllUsers() {
        List<User> userList = new ArrayList<>();
        cursor = super.query(USER_TABLE, USER_COLUMNS, null,
                null, USER_ID);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                User user = cursorToEntity(cursor);
                userList.add(user);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return userList;
    }

    public boolean deleteUser(int id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ID + " = ?";
        User user = new User();
        try {
            return super.delete(USER_TABLE, selection, selectionArgs) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    public boolean addUser(User user) {
        // set values
        setContentValue(user);
        try {
            return super.insert(USER_TABLE, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean addUsers(List<User> users) {
        return false;
    }

    @Override
    public boolean deleteAllUsers() {
        return false;
    }

    protected User cursorToEntity(Cursor cursor) {

        User user = new User();

        int idIndex;
        int userNameIndex;
        int emailIndex;
        int dateIndex;

        if (cursor != null) {
            if (cursor.getColumnIndex(USER_ID) != -1) {
                idIndex = cursor.getColumnIndexOrThrow(USER_ID);
                user.id = cursor.getInt(idIndex);
            }
            if (cursor.getColumnIndex(USER_NAME) != -1) {
                userNameIndex = cursor.getColumnIndexOrThrow(
                        USER_NAME);
                user.username = cursor.getString(userNameIndex);
            }
            if (cursor.getColumnIndex(USER_EMAIL) != -1) {
                emailIndex = cursor.getColumnIndexOrThrow(
                        USER_EMAIL);
                user.email = cursor.getString(emailIndex);
            }
            if (cursor.getColumnIndex(USER_DATE) != -1) {
                dateIndex = cursor.getColumnIndexOrThrow(USER_DATE);
                user.createdDate = new Date(cursor.getLong(dateIndex));
            }

        }
        return user;
    }

    private void setContentValue(User user) {
        initialValues = new ContentValues();
        initialValues.put(USER_ID, user.id);
        initialValues.put(USER_NAME, user.username);
        initialValues.put(USER_EMAIL, user.email);
        initialValues.put(USER_DATE, user.createdDate.getTime());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}