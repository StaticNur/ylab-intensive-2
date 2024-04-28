package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.dao.WorkoutTypeDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.WorkoutTypeException;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutTypeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Implementation class for {@link WorkoutTypeService}.
 */
@ApplicationScoped
@NoArgsConstructor
public class WorkoutTypeServiceImpl implements WorkoutTypeService {

    /**
     * Data access object for workout type operations.
     */
    private WorkoutTypeDao workoutTypeDao;

    @Inject
    public WorkoutTypeServiceImpl(WorkoutTypeDao workoutTypeDao) {
        this.workoutTypeDao = workoutTypeDao;
    }

    @Override
    @Timed
    @Loggable
    public WorkoutType saveType(int userId, String typeName) {
        Optional<WorkoutType> workoutType = workoutTypeDao.findByType(typeName);
        if (workoutType.isPresent()) {
            throw new WorkoutTypeException("Такой тип ранее был добавлен!");
        }
        return workoutTypeDao.saveType(userId, typeName);
    }

    @Override
    @Timed
    @Loggable
    public List<WorkoutType> findByUserId(int userId) {
        return workoutTypeDao.findByUserId(userId);
    }

    @Override
    @Timed
    @Loggable
    public void updateType(int workoutId, String oldType, String newType) {
        workoutTypeDao.updateType(workoutId, oldType, newType);
    }

    @Override
    @Timed
    @Loggable
    public WorkoutType findById(int id) {
        return workoutTypeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Такой тип тренировок не существует в базе данных."));
    }
}
