package com.ylab.intensive.service.impl;

import com.ylab.intensive.aspects.annotation.Loggable;
import com.ylab.intensive.aspects.annotation.Timed;
import com.ylab.intensive.repository.WorkoutTypeDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.WorkoutTypeException;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation class for {@link WorkoutTypeService}.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class WorkoutTypeServiceImpl implements WorkoutTypeService {

    /**
     * Data access object for workout type operations.
     */
    private final WorkoutTypeDao workoutTypeDao;

    @Override
    @Loggable
    @Timed
    @Transactional
    public WorkoutType saveType(int userId, String typeName) {
        Optional<WorkoutType> workoutType = workoutTypeDao.findTypeByUserId(userId, typeName);
        if (workoutType.isPresent()) {
            String message = "Такой тип ранее был добавлен!";
            log.info(message);
            throw new WorkoutTypeException(message);
        }
        return workoutTypeDao.saveType(userId, typeName);
    }

    @Override
    @Loggable
    @Timed
    public List<WorkoutType> findByUserId(int userId) {
        return workoutTypeDao.findByUserId(userId);
    }

    @Override
    @Loggable
    @Timed
    @Transactional
    public void updateType(int userId, String oldType, String newType) {
        workoutTypeDao.updateType(userId, oldType, newType);
    }

    @Override
    @Loggable
    @Timed
    public WorkoutType findByName(String name) {
        return workoutTypeDao.findByName(name)
                .orElseThrow(() -> {
                    String message = "Такой тип тренировок не существует в базе данных.";
                    log.info(message);
                    return new NotFoundException(message);
                });
    }

    @Override
    @Loggable
    @Timed
    public WorkoutType findTypeByUserId(int userId, String typeName) {
        Optional<WorkoutType> workoutType = workoutTypeDao.findTypeByUserId(userId, typeName);
        if (workoutType.isEmpty()) {
            String message = "Такого типа тренировки у данного спортсмена не существует в базе данных!";
            log.info(message);
            throw new NotFoundException(message);
        }
        return workoutType.get();
    }

    @Override
    @Loggable
    @Timed
    @Transactional
    public void delete(int userId) {
        workoutTypeDao.delete(userId);
    }
}
