package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.service.WorkoutTypeService;

import java.util.Set;

/**
 * Implementation class for {@link WorkoutTypeService}.
 */
public class WorkoutTypeServiceImpl implements WorkoutTypeService {

    /**
     * Data access object for workout type operations.
     */
    @Inject
    private WorkoutTypeDao workoutTypeDao;

    @Override
    public void saveType(int workoutId, String typeName) {
        workoutTypeDao.saveType(workoutId, typeName);
    }

    @Override
    public Set<String> findByWorkoutId(int workoutId) {
        return workoutTypeDao.findByWorkoutId(workoutId);
    }

    @Override
    public void updateType(int workoutId, String oldType, String newType) {
        workoutTypeDao.updateType(workoutId, oldType, newType);
    }

    @Override
    public void delete(int workoutId) {
        workoutTypeDao.delete(workoutId);
    }
}
