package com.jacksonasantos.travelplan.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jacksonasantos.travelplan.dao.general.Database;
import com.jacksonasantos.travelplan.dao.general.DbContentProvider;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementResumeIDAO;
import com.jacksonasantos.travelplan.dao.interfaces.AchievementResumeISchema;

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
        "SELECT d.country " +
             ", d.state " +
             ", MAX(d.va) vl_noType " +
             ", MAX(d.vb) vl_mountainRange " +
             ", MAX(d.vc) vl_road " +
             ", MAX(d.vd) vl_cave " +
             ", MAX(d.ve) vl_touristSpot " +
          "FROM ( SELECT c.country " +
                      ", c.state " +
                      ", CASE c.types_achievement WHEN 0 then (c.Conquistados) ELSE ' ' END va " +
                      ", CASE c.types_achievement WHEN 1 then (c.Conquistados) ELSE ' ' END vb " +
                      ", CASE c.types_achievement WHEN 2 then (c.Conquistados) ELSE ' ' END vc " +
                      ", CASE c.types_achievement WHEN 3 then (c.Conquistados) ELSE ' ' END vd " +
                      ", CASE c.types_achievement WHEN 4 then (c.Conquistados) ELSE ' ' END ve " +
                   "FROM ( SELECT b.country " +
                               ", b.state " +
                               ", b.types_achievement " +
                               ", SUM(b.conquistado)||"+"'/'"+"||SUM(b.total) Conquistados " +
                            "FROM ( SELECT a.country " +
                                        ", IIF((a.state!=a.state_end and a.state_end==''),a.state,IIF(a.state!=a.state_end,'--',a.state)) state " +
                                        ", a.types_achievement " +
                                        ", CASE a.status_achievement WHEN 0 THEN COUNT(*) ELSE 0 END pendente " +
                                        ", CASE a.status_achievement WHEN 1 THEN COUNT(*) ELSE 0 END conquistado " +
                                        ", COUNT(*) total " +
                                     "FROM achievement a " +
                                    "GROUP BY a.country " +
                                           ", IIF((a.state!=a.state_end and a.state_end==''),a.state,IIF(a.state!=a.state_end,'--',a.state)) " +
                                           ", a.types_achievement " +
                                           ", a.status_achievement ) b " +
                           "GROUP BY b.country, b.state, b.types_achievement " +
                           "ORDER BY b.country,b.state, b.types_achievement) c " +
                  "GROUP BY c.country, c.state, c.types_achievement) d " +
         "GROUP BY d.country, d.state", null);
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
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_NOTYPE) != -1)          {aR.setVl_noType(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_NOTYPE))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_MOUNTAINRANGE) != -1)   {aR.setVl_mountainRange(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_MOUNTAINRANGE))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_ROAD) != -1)            {aR.setVl_road(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_ROAD))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_CAVE) != -1)            {aR.setVl_cave(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_CAVE))); }
            if (c.getColumnIndex(ACHIEVEMENT_RESUME_VL_TOURISTSPOT) != -1)     {aR.setVl_touristSpot(c.getString(c.getColumnIndexOrThrow(ACHIEVEMENT_RESUME_VL_TOURISTSPOT))); }
        }
        return aR;
    }
}