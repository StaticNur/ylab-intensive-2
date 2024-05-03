package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.dao.WorkoutInfoDao;
import jakarta.inject.Inject;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.service.WorkoutInfoService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

/**
 * Implementation class for {@link WorkoutInfoService}.
 */
@ApplicationScoped
@NoArgsConstructor
public class WorkoutInfoServiceImpl implements WorkoutInfoService {

    /**
     * Data access object for workout information operations.
     */
    private WorkoutInfoDao workoutInfoDao;

    @Inject
    public WorkoutInfoServiceImpl(WorkoutInfoDao workoutInfoDao) {
        this.workoutInfoDao = workoutInfoDao;
    }

    @Override
    @Auditable
    @Loggable
    @Timed
    public void saveWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.saveWorkoutInfo(workoutId, title, info);
    }

    @Override
    @Loggable
    @Timed
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.updateWorkoutInfo(workoutId, title, info);
    }

    @Override
    @Loggable
    @Timed
    public WorkoutInfo getInfoByWorkoutId(int workoutId) {
        return workoutInfoDao.findByWorkoutId(workoutId);
    }

    @Override
    @Loggable
    @Timed
    public void delete(int workoutId) {
        workoutInfoDao.delete(workoutId);
    }
}
