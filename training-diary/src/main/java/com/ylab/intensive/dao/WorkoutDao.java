package com.ylab.intensive.dao;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.Workout;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The WorkoutDao interface provides methods to interact with workout data in the database.
 */
public interface WorkoutDao {
    /**
     * Initializes the workout database with the provided list of workouts.
     *
     * @param workouts The list of workouts to initialize the database with
     */
    void init(List<Workout> workouts);

    /**
     * Gets the size of the workout database.
     *
     * @return The size of the workout database
     */
    int getSize();

    /**
     * Finds a workout by its date.
     *
     * @param date The date of the workout to find
     * @return An Optional containing the workout if found, otherwise empty
     */

    Optional<Workout> findByDate(LocalDate date);

    /**
     * Saves the type of a workout.
     *
     * @param workout The workout to save the type for
     * @param type    The type of the workout to save
     */

    void saveType(Workout workout, String type);

    /**
     * Saves a workout to the database.
     *
     * @param workout The workout to save
     */

    void saveWorkout(Workout workout);

    /**
     * Saves additional information for a workout.
     *
     * @param workout The workout to save the information for
     * @param title   The title of the information
     * @param info    The information to save
     */

    void saveWorkoutInfo(Workout workout, String title, String info);

    /**
     * Finds all workouts in the database.
     *
     * @return A list of all workouts in the database
     */
    List<Workout> findAll();

    /**
     * Deletes a workout from the database by its date.
     *
     * @param date The date of the workout to delete
     */
    void deleteWorkout(LocalDate date);

    /**
     * Updates the information of a workout.
     *
     * @param workoutDto The workout DTO containing the information to update
     * @param title      The title of the information to update
     * @param info       The new information
     */
    void updateWorkoutInfo(WorkoutDto workoutDto, String title, String info);

    /**
     * Updates the calorie of a workout.
     *
     * @param workoutDto The workout DTO containing the calorie to update
     * @param calorie    The new calorie value
     */
    void updateCalorie(WorkoutDto workoutDto, Float calorie);

    /**
     * Updates the duration of a workout.
     *
     * @param workoutDto The workout DTO containing the duration to update
     * @param duration   The new duration value
     */
    void updateDuration(WorkoutDto workoutDto, Duration duration);

    /**
     * Updates the type of a workout.
     *
     * @param workoutDto The workout DTO containing the type to update
     * @param oldType    The old type of the workout
     * @param newType    The new type of the workout
     */
    void updateType(WorkoutDto workoutDto, String oldType, String newType);

    /**
     * Finds workouts within a specified date range.
     *
     * @param begin The start date of the range
     * @param end   The end date of the range
     * @return A list of workouts within the specified date range
     */
    List<Workout> findByDuration(LocalDate begin, LocalDate end);
}
