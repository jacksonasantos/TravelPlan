package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.PersonIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.PersonISchema;
import com.jacksonasantos.travelplan.ui.utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonDAO extends DbContentProvider implements PersonISchema, PersonIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public PersonDAO(SQLiteDatabase db) {
        super(db);
    }

    public Person fetchPersonById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = PERSON_ID + " = ?";
        Person person = new Person();
        cursor = super.query(PERSON_TABLE, PERSON_COLUMNS, selection, selectionArgs, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                person = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return person;
    }

    public List<Person> fetchAllPerson() {
        List<Person> personList = new ArrayList<>();

        cursor = super.query(PERSON_TABLE, PERSON_COLUMNS, null,null, PERSON_NAME);

        if (cursor.moveToFirst()) {
            do {
                Person person = cursorToEntity(cursor);
                personList.add(person);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return personList;
    }

    public ArrayList<Person> fetchArrayPerson(){
        ArrayList<Person> personList = new ArrayList<>();
        Cursor cursor = super.query(PERSON_TABLE, PERSON_COLUMNS, null,null, PERSON_NAME);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Person person = cursorToEntity(cursor);
                personList.add(person);
            }while(cursor.moveToNext());
        }
        return personList;
    }

    public Cursor fetchCursorPerson() {
        return super.rawQuery( "SELECT '' _id, '' text1 UNION " +
                "SELECT " + PERSON_ID + ", " +
                PERSON_NAME + " " +
                "FROM " + PERSON_TABLE + " " +
                "ORDER BY " + PERSON_NAME, null);
    }

    public void deletePerson(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = PERSON_ID + " = ?";
        super.delete(PERSON_TABLE, selection, selectionArgs);
    }

    public boolean updatePerson(Person Person) {
        setContentValue(Person);
        final String[] selectionArgs = { String.valueOf(Person.getId()) };
        final String selection = PERSON_ID + " = ?";
        try {
            return (super.update(PERSON_TABLE, getContentValue(), selection, selectionArgs) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Update Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    public boolean addPerson(Person Person) {
        setContentValue(Person);
        try {
            return (super.insert(PERSON_TABLE, getContentValue()) > 0);
        } catch (SQLiteConstraintException ex){
            Log.w("Insert Table", Objects.requireNonNull(ex.getMessage()));
            return false;
        }
    }

    protected Person cursorToEntity(Cursor c) {
        Person p = new Person();
        if (c != null) {
            if (c.getColumnIndex(PERSON_ID) != -1)                      {p.id = c.getInt(c.getColumnIndexOrThrow(PERSON_ID)); }
            if (c.getColumnIndex(PERSON_NAME) != -1)                    {p.name = c.getString(c.getColumnIndexOrThrow(PERSON_NAME)); }
            if (c.getColumnIndex(PERSON_SHORT_NAME) != -1)              {p.short_name = c.getString(c.getColumnIndexOrThrow(PERSON_SHORT_NAME)); }
            if (c.getColumnIndex(PERSON_GENDER) != -1)                  {p.gender = c.getInt(c.getColumnIndexOrThrow(PERSON_GENDER)); }
            if (c.getColumnIndex(PERSON_DATE_BIRTH) != -1)              {p.date_birth = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(PERSON_DATE_BIRTH))); }
            if (c.getColumnIndex(PERSON_DRIVING_RECORD) != -1)          {p.driving_record = c.getString(c.getColumnIndexOrThrow(PERSON_DRIVING_RECORD)); }
            if (c.getColumnIndex(PERSON_LICENSE_EXPIRATION_DATE) != -1) {p.license_expiration_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(PERSON_LICENSE_EXPIRATION_DATE))); }
            if (c.getColumnIndex(PERSON_FIRST_LICENSE_DATE) != -1)      {p.first_license_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(PERSON_FIRST_LICENSE_DATE))); }
            if (c.getColumnIndex(PERSON_LICENSE_ISSUE_DATE) != -1)      {p.license_issue_date = Utils.dateParse(c.getString(c.getColumnIndexOrThrow(PERSON_LICENSE_ISSUE_DATE))); }
            if (c.getColumnIndex(PERSON_LICENSE_CATEGORY) != -1)        {p.license_category = c.getString(c.getColumnIndexOrThrow(PERSON_LICENSE_CATEGORY));
            }
        }
        return p;
    }

    private void setContentValue(Person b) {
        initialValues = new ContentValues();
        initialValues.put(PERSON_ID, b.id);
        initialValues.put(PERSON_NAME, b.name);
        initialValues.put(PERSON_SHORT_NAME, b.short_name);
        initialValues.put(PERSON_GENDER, b.gender);
        initialValues.put(PERSON_DATE_BIRTH, Utils.dateFormat(b.date_birth));
        initialValues.put(PERSON_DRIVING_RECORD, b.driving_record);
        initialValues.put(PERSON_LICENSE_EXPIRATION_DATE, Utils.dateFormat(b.license_expiration_date));
        initialValues.put(PERSON_FIRST_LICENSE_DATE, Utils.dateFormat(b.first_license_date));
        initialValues.put(PERSON_LICENSE_ISSUE_DATE, Utils.dateFormat(b.license_issue_date));
        initialValues.put(PERSON_LICENSE_CATEGORY, b.license_category);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}