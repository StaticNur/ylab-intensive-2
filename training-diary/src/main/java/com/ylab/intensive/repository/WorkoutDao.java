package com.ylab.intensive.repository;

import com.ylab.intensive.model.entity.Workout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The WorkoutDao interface provides methods to interact with workout data in the database.
 */
public interface WorkoutDao {

    /**
     * Retrieves a workout by date and user ID.
     *
     * @param date   the date of the workout.
     * @param userId the ID of the user associated with the workout.
     * @return an optional containing the workout if found, or empty if not found.
     */
    Optional<Workout> findByDate(LocalDate date, int userId);

    /**
     * Saves a workout.
     *
     * @param workout the workout to be saved.
     * @return the saved workout object.
     */
    Workout saveWorkout(Workout workout);

    /**
     * Retrieves all workouts associated with a user.
     *
     * @param userId the ID of the user.
     * @return a list of workouts associated with the user.
     */
    List<Workout> findByUserId(int userId);

    /**
     * Deletes a workout by user ID and workout ID.
     *
     * @param userId the ID of the user.
     * @param id     the ID of the workout to delete.
     */
    void deleteWorkout(int userId, int id);

    /**
     * Updates the calorie count of a workout.
     *
     * @param id      the ID of the workout.
     * @param calorie the new calorie count.
     */
    void updateCalorie(int id, Float calorie);

    /**
     * Updates the duration of a workout.
     *
     * @param id       the ID of the workout.
     * @param duration the new duration.
     */
    void updateDuration(int id, Duration duration);

    /**
     * Retrieves workouts by user ID and date range.
     *
     * @param userId the ID of the user.
     * @param begin  the start date of the date range.
     * @param end    the end date of the date range.
     * @return a list of workouts within the specified date range.
     */
    List<Workout> findByDuration(int userId, LocalDate begin, LocalDate end);

    /**
     * Retrieves a workout by its UUID.
     *
     * @param uuid the UUID of the workout.
     * @return an optional containing the workout if found, or empty if not found.
     */
    Optional<Workout> findByUUID(UUID uuid);

    /**
     * Updates the type of a workout.
     *
     * @param workoutId the ID of the workout.
     * @param newType   the new type ID.
     */
    void updateType(int workoutId, String newType);
}
