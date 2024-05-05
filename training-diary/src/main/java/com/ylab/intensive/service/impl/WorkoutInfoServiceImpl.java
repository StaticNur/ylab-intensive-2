package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.repository.WorkoutInfoDao;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.service.WorkoutInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation class for {@link WorkoutInfoService}.
 */
@Service
@RequiredArgsConstructor
public class WorkoutInfoServiceImpl implements WorkoutInfoService {

    /**
     * Data access object for workout information operations.
     */
    private final WorkoutInfoDao workoutInfoDao;


    @Override
    @Loggable
    @Timed
    @Transactional
    public void saveWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.saveWorkoutInfo(workoutId, title, info);
    }

    @Override
    @Loggable
    @Timed
    @Transactional
    public void updateWorkoutInfo(int workoutId, String title, String info) {
        workoutInfoDao.updateWorkoutInfo(workoutId, title, info);
    }

    @Override
    @Loggable
    @Timed
    public Optional<WorkoutInfo> getInfoByWorkoutId(int workoutId) {
        return workoutInfoDao.findByWorkoutId(workoutId);
    }

    @Override
    @Loggable
    @Timed
    public void delete(int workoutId) {
        workoutInfoDao.delete(workoutId);
    }
}
