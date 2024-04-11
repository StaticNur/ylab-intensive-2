package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.DateFormatException;
import com.ylab.intensive.exception.NotFoundWorkoutException;
import com.ylab.intensive.exception.TrainingTypeException;
import com.ylab.intensive.exception.WorkoutException;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.UserManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testing Workout Service Implementation")
class WorkoutServiceImplTest {

    @Mock
    private WorkoutDao workoutDao;

    @Mock
    private UserManagementService userManagementService;

    @Mock
    private Session authorizedUser;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    @DisplayName("Add training type - Type already exists")
    void testAddTrainingType_TypeExists() {
        String date = "11-04-2024";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String typeName = "Running";
        Workout workout = new Workout();
        workout.setType(Set.of(typeName));

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.of(workout));

        assertThrows(TrainingTypeException.class, () -> workoutService.addTrainingType(date, typeName));
    }

    @Test
    @DisplayName("Add training type - Type does not exist")
    void testAddTrainingType_TypeDoesNotExist() {
        String date = "11-04-2024";
        String typeName = "Running";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.empty());

        assertThrows(TrainingTypeException.class, () -> workoutService.addTrainingType(date, typeName));
    }

    @Test
    @DisplayName("Add workout - Workout already exists")
    void testAddWorkout_WorkoutExists() {
        String date = "11-04-2024";
        String typeName = "Running";
        String durationStr = "1:30:00";
        String calorie = "500";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.of(new Workout()));

        assertThrows(WorkoutException.class, () -> workoutService.addWorkout(date, typeName, durationStr, calorie));
    }

    @Test
    @DisplayName("Add workout - Workout does not exist")
    void testAddWorkout_WorkoutDoesNotExist() {
        String date = "11-04-2024";
        String typeName = "Running";
        String durationStr = "1:30:00";
        String calorie = "500";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> workoutService.addWorkout(date, typeName, durationStr, calorie));
    }

    @Test
    @DisplayName("Add workout info - Workout not found")
    void testAddWorkoutInfo_WorkoutNotFound() {
        String date = "11-04-2024";
        String title = "Title";
        String info = "Info";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.empty());

        assertThrows(NotFoundWorkoutException.class, () -> workoutService.addWorkoutInfo(date, title, info));
    }

    @Test
    @DisplayName("Add workout info - Workout found")
    void testAddWorkoutInfo_WorkoutFound() {
        String date = "11-04-2024";
        String title = "Title";
        String info = "Info";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Workout workout = new Workout();
        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.of(workout));

        assertDoesNotThrow(() -> workoutService.addWorkoutInfo(date, title, info));
    }

    @Test
    @DisplayName("Get all workouts")
    void testGetAllWorkouts() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Workout workout1 = new Workout();
        workout1.setDate(LocalDate.parse("01-02-2023", formatter));
        Workout workout2 = new Workout();
        workout2.setDate(LocalDate.parse("11-04-2024", formatter));
        List<Workout> workouts = Arrays.asList(workout1, workout2);

        when(workoutDao.findAll()).thenReturn(workouts);

        List<WorkoutDto> result = workoutService.getAllUserWorkouts();

        assertEquals(workouts.size(), result.size());
    }

    @Test
    @DisplayName("Get workout by date - Workout found")
    void testGetWorkoutByDate_WorkoutFound() {
        String date = "11-04-2024";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Workout workout = new Workout();

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.of(workout));

        Optional<WorkoutDto> result = workoutService.getWorkoutByDate(date);

        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Get workout by date - Workout not found")
    void testGetWorkoutByDate_WorkoutNotFound() {
        String date = "11-04-2024";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.empty());

        Optional<WorkoutDto> result = workoutService.getWorkoutByDate(date);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Update workout type")
    void testUpdateType() {
        WorkoutDto workoutDto = new WorkoutDto();
        String oldType = "Old Type";
        String newType = "New Type";

        assertDoesNotThrow(() -> workoutService.updateType(workoutDto, oldType, newType));
    }

    @Test
    @DisplayName("Update workout duration")
    void testUpdateDuration() {
        WorkoutDto workoutDto = new WorkoutDto();
        String durationStr = "1:30:00";

        assertDoesNotThrow(() -> workoutService.updateDuration(workoutDto, durationStr));
    }

    @Test
    @DisplayName("Update workout calories")
    void testUpdateCalories() {
        WorkoutDto workoutDto = new WorkoutDto();
        String calories = "500";

        assertDoesNotThrow(() -> workoutService.updateCalories(workoutDto, calories));
    }

    @Test
    @DisplayName("Update additional info")
    void testUpdateAdditionalInfo() {
        WorkoutDto workoutDto = new WorkoutDto();
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        String title = "Title";
        String info = "Info";
        objectObjectHashMap.put(title, info);
        workoutDto.setInfo(objectObjectHashMap);

        doNothing().when(workoutDao).updateWorkoutInfo(workoutDto, title, info);
        doNothing().when(userManagementService).saveAction(anyString());


        assertDoesNotThrow(() -> workoutService.updateAdditionalInfo(workoutDto, title, info));
    }

    @Test
    @DisplayName("Delete workout")
    void testDeleteWorkout() {
        String date = "11-04-2024";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        when(workoutDao.findByDate(LocalDate.parse(date, formatter))).thenReturn(Optional.of(new Workout()));

        assertDoesNotThrow(() -> workoutService.deleteWorkout(date));
    }

    @Test
    @DisplayName("Get workout statistics")
    void testGetWorkoutStatistics() {
        String beginStr = "01-02-2024";
        String endStr = "11-04-2024";

        assertDoesNotThrow(() -> workoutService.getWorkoutStatistics(beginStr, endStr));
    }

    @Test
    @DisplayName("Get workout statistics - Invalid date format")
    void testGetWorkoutStatistics_InvalidDateFormat() {
        String beginStr = "2024/04/01";
        String endStr = "2024/04/10";

        assertThrows(DateFormatException.class, () -> workoutService.getWorkoutStatistics(beginStr, endStr));
    }

}
