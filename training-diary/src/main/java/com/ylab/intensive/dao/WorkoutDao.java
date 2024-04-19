package com.ylab.intensive.dao;

import com.ylab.intensive.model.entity.Workout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The WorkoutDao interface provides methods to interact with workout data in the database.
 */
public interface WorkoutDao {

    /**
     * Finds a workout by its date.
     *
     * @param date The date of the workout to find
     * @return An Optional containing the workout if found, otherwise empty
     */

    Optional<Workout> findByDate(LocalDate date);

    /**
     * Saves a workout to the database.
     *
     * @param workout The workout to save
     */

    Workout saveWorkout(Workout workout);

    /**
     * Finds all workouts in the database.
     *
     * @return A list of all workouts in the database
     */
    List<Workout> findByUserId(int userId);

    /**
     * Deletes a workout from the database by its date.
     *
     * @param date The date of the workout to delete
     */
    void deleteWorkout(LocalDate date);

    /**
     * Updates the calorie of a workout.
     *
     * @param id The workout id containing the calorie to update
     * @param calorie    The new calorie value
     */
    void updateCalorie(int id, Float calorie);

    /**
     * Updates the duration of a workout.
     *
     * @param id  The workout id containing the duration to update
     * @param duration The new duration value
     */
    void updateDuration(int id, Duration duration);

    /**
     * Finds workouts within a specified date range.
     *
     * @param begin The start date of the range
     * @param end   The end date of the range
     * @return A list of workouts within the specified date range
     */
    List<Workout> findByDuration(LocalDate begin, LocalDate end);
}
