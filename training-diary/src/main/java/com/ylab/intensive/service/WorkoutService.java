package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.exception.DateFormatException;
import com.ylab.intensive.exception.WorkoutException;
import com.ylab.intensive.model.Workout;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing workout-related operations.
 */
public interface WorkoutService {
    /**
     * Sets up the authorized user's workout database with the specified list of workouts.
     *
     * @param workouts The list of workouts to be set as the authorized workout database
     */
    void setAuthorizedWorkoutDB(List<Workout> workouts);

    /**
     * Adds a new training type on the specified date.
     *
     * @param date     The date of the training type.
     * @param typeName The name of the training type.
     */
    void addTrainingType(String date, String typeName);

    /**
     * Adds a new workout with the specified date, type name, duration, and calorie.
     *
     * @param date     The date of the workout
     * @param typeName The name of the workout type
     * @param duration The duration of the workout in the format hh:mm:ss
     * @param calorie  The calorie burned during the workout
     * @throws WorkoutException    if an error occurs during the workout addition process
     * @throws DateFormatException if the date format is invalid
     */
    void addWorkout(String date, String typeName, String duration, String calorie) throws WorkoutException, DateFormatException;

    /**
     * Adds additional information to a workout with the specified date, title, and info.
     *
     * @param date  The date of the workout
     * @param title The title of the additional information
     * @param info  The additional information to be added
     */
    void addWorkoutInfo(String date, String title, String info);

    /**
     * Retrieves all workouts as DTOs sorted by date.
     *
     * @return List of training DTOs.
     */
    List<WorkoutDto> getAllWorkouts();

    /**
     * Retrieves the workout DTO by date.
     *
     * @param date The date of the workout
     * @return An optional containing the workout DTO if found, empty otherwise
     */
    Optional<WorkoutDto> getWorkoutByDate(String date);

    /**
     * Updates the type of the workout with the specified old type to the new type.
     *
     * @param workoutDto The workout DTO to be updated
     * @param oldType    The old type of the workout
     * @param newType    The new type of the workout
     */
    void updateType(WorkoutDto workoutDto, String oldType, String newType);

    /**
     * Updates the duration of the workout.
     *
     * @param workoutDto The workout DTO to be updated
     * @param duration   The new duration of the workout in the format hh:mm:ss
     */
    void updateDuration(WorkoutDto workoutDto, String duration);

    /**
     * Updates the calorie burned during the workout.
     *
     * @param workoutDto The workout DTO to be updated
     * @param calorie    The new calorie burned during the workout
     */
    void updateCalories(WorkoutDto workoutDto, String calorie);

    /**
     * Updates additional information of the workout with the specified title to the new info.
     *
     * @param workoutDto The workout DTO to be updated
     * @param title      The title of the additional information to be updated
     * @param info       The new information to be set for the specified title
     */
    void updateAdditionalInfo(WorkoutDto workoutDto, String title, String info);

    /**
     * Deletes the workout with the specified date.
     *
     * @param date The date of the workout to be deleted
     */
    void deleteWorkout(String date);

    /**
     * Retrieves the workout statistics within the specified date range and returns the total calorie burned.
     *
     * @param begin The start date of the date range
     * @param end   The end date of the date range
     * @return The total calorie burned within the specified date range
     */
    int getWorkoutStatistics(String begin, String end);
}
