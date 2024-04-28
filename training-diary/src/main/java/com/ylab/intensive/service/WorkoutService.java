package com.ylab.intensive.service;

import com.ylab.intensive.model.dto.EditWorkout;
import com.ylab.intensive.model.dto.StatisticsDto;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.exception.DateFormatException;
import com.ylab.intensive.exception.WorkoutException;
import com.ylab.intensive.model.dto.WorkoutInfoDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutType;

import java.util.List;

/**
 * Service interface for managing workout-related operations.
 */
public interface WorkoutService {

    /**
     * Adds a new workout with the specified date, type name, duration, and calorie.
     *
     * @throws WorkoutException    if an error occurs during the workout addition process
     * @throws DateFormatException if the date format is invalid
     */
    WorkoutDto addWorkout(String email, WorkoutDto workoutDto);

    /**
     * Adds additional information to a workout with the specified date, title, and info.
     */
    Workout addWorkoutInfo(String email, String uuidStr, WorkoutInfoDto workoutInfoDto);

    /**
     * Retrieves all workouts as DTOs sorted by date.
     *
     * @return List of training DTOs.
     */
    List<WorkoutDto> getAllUserWorkouts(String login);

    /**
     * Updates the type of the workout with the specified old type to the new type.
     *
     * @param workoutDto The workout DTO to be updated
     * @param oldType    The old type of the workout
     * @param newType    The new type of the workout
     */
    //void updateType(WorkoutDto workoutDto, String oldType, String newType);

    /**
     * Updates the duration of the workout.
     *
     * @param workoutDto The workout DTO to be updated
     * @param duration   The new duration of the workout in the format hh:mm:ss
     */
    //void updateDuration(WorkoutDto workoutDto, String duration);

    /**
     * Updates the calorie burned during the workout.
     *
     * @param workoutDto The workout DTO to be updated
     * @param calorie    The new calorie burned during the workout
     */
    //void updateCalories(WorkoutDto workoutDto, String calorie);

    /**
     * Updates additional information of the workout with the specified title to the new info.
     *
     * @param workoutDto The workout DTO to be updated
     * @param title      The title of the additional information to be updated
     * @param info       The new information to be set for the specified title
     */
    //void updateAdditionalInfo(WorkoutDto workoutDto, String title, String info);

    /**
     * Deletes the workout with the specified date.
     *
     * @param uuid The date of the workout to be deleted
     */
    void deleteWorkout(String email, String uuid);

    /**
     * Retrieves the workout statistics within the specified date range and returns the total calorie burned.
     *
     * @param begin The start date of the date range
     * @param end   The end date of the date range
     * @return The total calorie burned within the specified date range
     */
    StatisticsDto getWorkoutStatistics(String email, String begin, String end);

    /**
     * Retrieves all workouts for the users in the provided list.
     *
     * @param userList the list of users
     * @return a list of all workouts for the users
     */
    List<User> getAllUsersWorkouts(List<User> userList);

    List<WorkoutType> getAllType(String login);

    WorkoutType saveWorkoutType(String login, String typeName);

    Workout updateWorkout(String email, String uuidStr, EditWorkout editWorkout);
}
