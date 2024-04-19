package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutInfoDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.service.WorkoutInfoService;

import java.util.Map;

public class WorkoutInfoServiceImpl implements WorkoutInfoService {
    @Inject
    private WorkoutInfoDao workoutInfoDao;

    @Override
    public void saveWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.saveWorkoutInfo(workoutId, title, info);
    }

    @Override
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.updateWorkoutInfo(workoutId, title, info);
    }

    @Override
    public Map<String, String> getInfoByWorkoutId(int workoutId) {
        return workoutInfoDao.findByWorkoutId(workoutId);
    }

    @Override
    public void delete(int workoutId) {
        workoutInfoDao.delete(workoutId);
    }
}
