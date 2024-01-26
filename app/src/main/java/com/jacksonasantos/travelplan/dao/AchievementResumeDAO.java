package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementResumeIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementResumeISchema;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementISchema;

import java.util.ArrayList;
import java.util.List;

public class AchievementResumeDAO extends DbContentProvider implements AchievementResumeISchema, AchievementResumeIDAO {

    private Cursor cursor;

    public AchievementResumeDAO(SQLiteDatabase db) {
        super(db);
    }

    public List<AchievementResume> fetchAchievementResume( ) {

        ArrayList<AchievementResume> achievementResumeList = new ArrayList<>();
        cursor = super.rawQuery(
        " SELECT b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_COUNTRY +
                  ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_STATE +
                  ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT +
                  ", SUM(b.conquered) " + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_CONQUERED +
                  ", SUM(b.total) " + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_TOTAL +
              " FROM ( SELECT a." + AchievementISchema.ACHIEVEMENT_COUNTRY +
                           ", IIF( (a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END +
                              " AND a." + AchievementISchema.ACHIEVEMENT_STATE_END + "== '' ), " +
                                   "a." + AchievementISchema.ACHIEVEMENT_STATE + ","+
                                   "IIF(a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END + ", "+
                                        "'--' , "+
                                        "a." + AchievementISchema.ACHIEVEMENT_STATE + ")) " + AchievementISchema.ACHIEVEMENT_STATE +
                           ", a." + AchievementISchema.ACHIEVEMENT_TYPE_ACHIEVEMENT + " type_achievement " +
                           ", CASE a." + AchievementISchema.ACHIEVEMENT_STATUS_ACHIEVEMENT + " WHEN 0 THEN count(*) ELSE 0 END pending " +
                           ", CASE a." + AchievementISchema.ACHIEVEMENT_STATUS_ACHIEVEMENT + " WHEN 1 THEN count(*) ELSE 0 END conquered " +
                           ", COUNT(*) total " +
                       " FROM " + AchievementISchema.ACHIEVEMENT_TABLE + " a " +
                      " GROUP BY a." + AchievementISchema.ACHIEVEMENT_COUNTRY +
                              ", IIF(a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END + ", " +
                                     "'--' , "+
                                     "a." + AchievementISchema.ACHIEVEMENT_STATE + ") " +
                              ", a." + AchievementISchema.ACHIEVEMENT_TYPE_ACHIEVEMENT +
                              ", a." + AchievementISchema.ACHIEVEMENT_STATUS_ACHIEVEMENT + " ) b " +
             " GROUP BY b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_COUNTRY +
                     ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_STATE +
                     ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT +
             " ORDER BY b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_COUNTRY +
                 ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_STATE +
                 ", b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT , null);
        if (cursor.moveToFirst()) {
            do {
                AchievementResume achievementResume = Database.mAchievementResumeDao.cursorToEntity(cursor);
                achievementResumeList.add(achievementResume);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return achievementResumeList;
    }

    public List<AchievementResume> fetchAchievementResumeSummary( ) {

        ArrayList<AchievementResume> achievementResumeList = new ArrayList<>();
        cursor = super.rawQuery("SELECT c." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT +
                                        " , c." + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_CONQUERED +
                                        " , c." + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_TOTAL +
                                        " , ROUND(((CAST(c."+ AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_CONQUERED + " AS DECIMAL)*100)/(CAST(c." + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_TOTAL + " AS DECIMAL))),2) perc_conquered " +
                                     " FROM (SELECT b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT +
                                                " , SUM(b.conquered) " + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_CONQUERED +
                                                " , SUM(b.total) " + AchievementResumeISchema.ACHIEVEMENT_RESUME_VL_TOTAL +
                                             " FROM ( SELECT a." + AchievementISchema.ACHIEVEMENT_COUNTRY +
                                                          ", IIF( (a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END +
                                                             " AND a." + AchievementISchema.ACHIEVEMENT_STATE_END + "== '' ), " +
                                                                  "a." + AchievementISchema.ACHIEVEMENT_STATE + ","+
                                                                  "IIF(a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END + ", "+
                                                                      "'--' , "+
                                                                      "a." + AchievementISchema.ACHIEVEMENT_STATE + ")) " + AchievementISchema.ACHIEVEMENT_STATE +
                                                         ", a." + AchievementISchema.ACHIEVEMENT_TYPE_ACHIEVEMENT + " type_achievement " +
                                                         " , CASE a.status_achievement WHEN 0 THEN count(*) ELSE 0 END pending " +
                                                         " , CASE a.status_achievement WHEN 1 THEN count(*) ELSE 0 END conquered  " +
                                                         " , COUNT(*) total " +
                                                      " FROM " + AchievementISchema.ACHIEVEMENT_TABLE + " a " +
                                                     " GROUP BY a." + AchievementISchema.ACHIEVEMENT_COUNTRY +
                                                             ", IIF(a." + AchievementISchema.ACHIEVEMENT_STATE + "!=a." + AchievementISchema.ACHIEVEMENT_STATE_END + ", " +
                                                                   "'--' , "+
                                                                   "a." + AchievementISchema.ACHIEVEMENT_STATE + ") " +
                                                             ", a." + AchievementISchema.ACHIEVEMENT_TYPE_ACHIEVEMENT +
                                                             ", a." + AchievementISchema.ACHIEVEMENT_STATUS_ACHIEVEMENT + " ) b " +
                                       " GROUP BY b." + AchievementResumeISchema.ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT + ") c "
                , null);
        if (cursor.moveToFirst()) {
            do {
                AchievementResume achievementResume = Database.mAchievementResumeDao.cursorToEntity(cursor);
                achievementResumeList.add(achievementResume);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return achievementResumeList;
    }

    protected AchievementResume cursorToEntity(Cursor c) {
        AchievementResume aR = new AchievementResume();
        if (c != null) {
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_COUNTRY) != -1)            {aR.setCountry(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_COUNTRY))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_STATE) != -1)              {aR.setState(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_STATE))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT) != -1)   {aR.setType_achievement(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_TYPE_ACHIEVEMENT))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_CONQUERED) != -1)       {aR.setVl_conquered(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_CONQUERED))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_TOTAL) != -1)           {aR.setVl_total(c.getInt(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_TOTAL))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_PERC_CONQUERED) != -1)     {aR.setPerc_conquered(c.getFloat(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_PERC_CONQUERED))); }
        }
        return aR;
    }
}