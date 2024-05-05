package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.repository.WorkoutTypeDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.WorkoutTypeException;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation class for {@link WorkoutTypeService}.
 */
@Service
@RequiredArgsConstructor
public class WorkoutTypeServiceImpl implements WorkoutTypeService {

    /**
     * Data access object for workout type operations.
     */
    private final WorkoutTypeDao workoutTypeDao;

    @Override
    @Auditable
    @Loggable
    @Timed
    @Transactional
    public WorkoutType saveType(int userId, String typeName) {
        Optional<WorkoutType> workoutType = workoutTypeDao.findTypeByUserId(userId, typeName);
        if (workoutType.isPresent()) {
            throw new WorkoutTypeException("Такой тип ранее был добавлен!");
        }
        return workoutTypeDao.saveType(userId, typeName);
    }

    @Override
    public List<WorkoutType> findByUserId(int userId) {
        return workoutTypeDao.findByUserId(userId);
    }

    @Override
    @Transactional
    public void updateType(int userId, String oldType, String newType) {
        workoutTypeDao.updateType(userId, oldType, newType);
    }

    @Override
    public WorkoutType findByName(String name) {
        return workoutTypeDao.findByName(name)
                .orElseThrow(() -> new NotFoundException("Такой тип тренировок не существует в базе данных."));
    }

    @Override
    public WorkoutType findTypeByUserId(int userId, String typeName) {
        Optional<WorkoutType> workoutType = workoutTypeDao.findTypeByUserId(userId, typeName);
        if (workoutType.isEmpty()) {
            throw new NotFoundException("Такого типа тренировки у данного спортсмена не существует в базе данных!");
        }
        return workoutType.get();
    }

    @Override
    @Transactional
    public void delete(int userId) {
        workoutTypeDao.delete(userId);
    }
}
