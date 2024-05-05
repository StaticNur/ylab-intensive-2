package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.EditWorkout;
import com.ylab.intensive.model.dto.StatisticsDto;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutType;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing workouts and related operations.
 * <p>
 * This interface defines methods to perform various operations related to workouts, including adding, updating, and deleting workouts,
 * retrieving workout statistics, and managing workout types.
 * </p>
 *
 * @since 1.0
 */
public interface WorkoutService {

    /**
     * Adds a new workout for the specified user.
     *
     * @param email      The email of the user for whom the workout is added.
     * @param workoutDto The DTO containing information about the workout to be added.
     * @return The DTO representing the newly added workout.
     */
    WorkoutDto addWorkout(String email, WorkoutDto workoutDto);

    /**
     * Adds additional information to an existing workout.
     *
     * @param uuidStr         The UUID of the workout to which the information is added.
     * @param workoutInfoDto  The DTO containing additional information to be added.
     * @return The updated workout entity with additional information.
     */
    Workout addWorkoutInfo(String email, String uuidStr, WorkoutInfoDto workoutInfoDto);

    /**
     * Retrieves all workouts associated with a specific user.
     *
     * @param login The email (email) of the user whose workouts are to be retrieved.
     * @return A list of DTOs representing the user's workouts.
     */
    List<Workout> getAllUserWorkouts(String login);

    /**
     * Updates the type of a workout.
     *
     * @param userId    The ID of the user who owns the workout.
     * @param workoutId The ID of the workout to update.
     * @param type      The new type of the workout.
     */
    void updateType(int userId, int workoutId, String type);

    /**
     * Updates the duration of a workout.
     *
     * @param workoutId The ID of the workout to update.
     * @param duration  The new duration of the workout.
     */
    void updateDuration(int workoutId, Duration duration);

    /**
     * Updates the calorie count of a workout.
     *
     * @param workoutId The ID of the workout to update.
     * @param calories  The new calorie count of the workout.
     */
    void updateCalories(int workoutId, Float calories);

    /**
     * Updates additional information of a workout.
     *
     * @param workoutId   The ID of the workout to update.
     * @param workoutInfo A map containing additional information to update.
     * @return
     */
    Map<String, String> updateAdditionalInfo(int workoutId, Map<String, String> workoutInfo);

    /**
     * Deletes a workout for the specified user.
     *
     * @param email The email of the user who owns the workout.
     * @param uuid  The UUID of the workout to delete.
     */
    void deleteWorkout(String email, String uuid);

    /**
     * Retrieves statistics for workouts within the specified time range for a user.
     *
     * @param email The email of the user for whom to retrieve statistics.
     * @param begin The start date of the time range.
     * @param end   The end date of the time range.
     * @return The DTO containing workout statistics.
     */
    StatisticsDto getWorkoutStatistics(String email, String begin, String end);

    /**
     * Retrieves all users' workouts.
     *
     * @param userList The list of users for whom to retrieve workouts.
     * @return A list of users with their respective workouts.
     */
    List<User> getAllUsersWorkouts(List<User> userList);

    /**
     * Retrieves all workout types for a specific user.
     *
     * @param login The email (email) of the user for whom to retrieve workout types.
     * @return A list of workout types associated with the user.
     */
    List<WorkoutType> getAllType(String login);

    /**
     * Saves a new workout type for the specified user.
     *
     * @param login    The email (email) of the user for whom to save the workout type.
     * @param typeName The name of the workout type to save.
     * @return The saved workout type entity.
     */
    WorkoutType saveWorkoutType(String login, String typeName);

    /**
     * Updates an existing workout.
     *
     * @param email     The email of the user who owns the workout.
     * @param uuidStr   The UUID of the workout to update.
     * @param editWorkout The DTO containing the updated workout information.
     * @return The updated workout entity.
     */
    Workout updateWorkout(String email, String uuidStr, EditWorkout editWorkout);
}
