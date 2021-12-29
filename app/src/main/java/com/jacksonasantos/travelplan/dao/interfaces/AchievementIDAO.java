package com.jacksonasantos.travelplan.dao.interfaces;

import com.jacksonasantos.travelplan.dao.Achievement;

import java.util.ArrayList;
import java.util.List;

public interface AchievementIDAO {
    Achievement fetchAchievementById(Integer id);
    List<Achievement> fetchAllAchievement();
    ArrayList<Achievement> fetchArrayAchievement();
    boolean addAchievement(Achievement achievement);
    void deleteAchievement(Integer id);
    boolean updateAchievement(Achievement achievement);
}