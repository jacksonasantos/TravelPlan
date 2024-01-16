package com.jacksonasantos.travelplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.AccountIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AccountISchema;

import java.util.ArrayList;
import java.util.List;

public class AccountDAO extends DbContentProvider implements AccountISchema, AccountIDAO {

    private Cursor cursor;
    private ContentValues initialValues;

    public AccountDAO(SQLiteDatabase db) {
        super(db);
    }

    public Account fetchAccountById(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACCOUNT_ID + " = ?";
        Account account = new Account();
        cursor = super.query(ACCOUNT_TABLE, ACCOUNT_COLUMNS, selection, selectionArgs, ACCOUNT_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                account = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return account;
    }

    public List<Account> fetchAllAccount() {
        List<Account> accountList = new ArrayList<>();

        cursor = super.query(ACCOUNT_TABLE, ACCOUNT_COLUMNS, null,null, ACCOUNT_ID);

        if (cursor.moveToFirst()) {
            do {
                Account Account = cursorToEntity(cursor);
                accountList.add(Account);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return accountList;
    }

    public void deleteAccount(Integer id) {
        final String[] selectionArgs = { String.valueOf(id) };
        final String selection = ACCOUNT_ID + " = ?";
        super.delete(ACCOUNT_TABLE, selection, selectionArgs);
    }

    public boolean updateAccount(Account account) {
        setContentValue(account);
        final String[] selectionArgs = { String.valueOf(account.getId()) };
        final String selection = ACCOUNT_ID + " = ?";
        return (super.update(ACCOUNT_TABLE, getContentValue(), selection, selectionArgs) > 0);
    }

    public boolean addAccount(Account account) {
        setContentValue(account);
        return (super.insert(ACCOUNT_TABLE, getContentValue()) > 0);
    }

    protected Account cursorToEntity(Cursor c) {
        Account tc = new Account();
        if (c != null) {
            if (c.getColumnIndex(ACCOUNT_ID) != -1)                  {tc.id = c.getInt(c.getColumnIndexOrThrow(ACCOUNT_ID)); }
            if (c.getColumnIndex(ACCOUNT_ACCOUNT_TYPE) != -1)        {tc.account_type = c.getInt(c.getColumnIndexOrThrow(ACCOUNT_ACCOUNT_TYPE)); }
            if (c.getColumnIndex(ACCOUNT_DESCRIPTION) != -1)         {tc.description = c.getString(c.getColumnIndexOrThrow(ACCOUNT_DESCRIPTION)); }
        }
        return tc;
    }

    private void setContentValue(Account tc) {
        initialValues = new ContentValues();
        initialValues.put(ACCOUNT_ID, tc.id);
        initialValues.put(ACCOUNT_ACCOUNT_TYPE, tc.account_type);
        initialValues.put(ACCOUNT_DESCRIPTION, tc.description);
    }

    private ContentValues getContentValue() {
        return initialValues;
    }
}
