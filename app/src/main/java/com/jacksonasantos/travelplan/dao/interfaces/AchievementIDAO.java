package com.jacksonasantos.travelplan.dao.interfaces;

import android.database.Cursor;

import com.jacksonasantos.travelplan.dao.Achievement;

import java.util.List;

public interface AchievementIDAO {
    Achievement fetchAchievementById(Integer id);
    List<Achievement> fetchAllAchievement();
    List<Achievement> fetchAllAchievementByStatusAchievement( Integer status );
    Cursor fetchArrayAchievement();
    Integer addAchievement(Achievement achievement);
    void deleteAchievement(Integer id);
    boolean updateAchievement(Achievement achievement);
}
