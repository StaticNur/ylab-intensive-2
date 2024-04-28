package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.service.WorkoutInfoService;
import com.ylab.intensive.service.WorkoutTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testing Workout Service Implementation")
class WorkoutServiceImplTest {

    @Mock
    private WorkoutDao workoutDao;

    @Mock
    private WorkoutTypeService workoutTypeService;

    @Mock
    private WorkoutInfoService workoutInfoService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    @DisplayName("Add training type - success")
    void testAddTrainingType_Success() {
        String typeName = "Cardio";
        String email = "test@email.com";
        User user = new User();
        user.setId(1);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutTypeService.saveType(1, typeName)).thenReturn(new WorkoutType());

        workoutService.saveWorkoutType(email, typeName);

        verify(workoutTypeService).saveType(1, typeName);
    }

    @Test
    @DisplayName("Add workout info - success")
    void testAddWorkoutInfo_Success() {
        String uuid = "123e4567-e89b-12d3-a456-426614174001";
        WorkoutInfoDto workoutInfo = new WorkoutInfoDto();
        workoutInfo.setWorkoutInfo(Map.of("title","info"));
        User user = new User();
        user.setId(1);
        Workout workoutMy = getWorkout();
        workoutMy.setType("1");

        when(workoutDao.findByUUID(UUID.fromString(uuid))).thenReturn(Optional.of(workoutMy));
        when(workoutTypeService.findById(1)).thenReturn(new WorkoutType());
        doNothing().when(workoutInfoService).saveWorkoutInfo(anyInt(), anyString(), anyString());

        Workout workout = workoutService.addWorkoutInfo(uuid, workoutInfo);

        assertThat(workout.getWorkoutInfo())
                .isEqualTo(workoutInfo.getWorkoutInfo());
    }

    @Test
    @DisplayName("Add workout info - workout not found")
    void testAddWorkoutInfo_WorkoutNotFound() {
        String uuid = "123e4567-e89b-12d3-a456-426614174001";

        when(workoutDao.findByUUID(UUID.randomUUID())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutService.addWorkoutInfo(uuid, new WorkoutInfoDto()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Тренировка с uuid = "+uuid+" нет в базе данных! Сначала добавьте ее.");
    }

    @Test
    @DisplayName("Get all user workouts - success")
    void testGetAllUserWorkouts_Success() {
        Workout workout = getWorkout();
        workout.setId(1);
        workout.setType("1");
        Workout workout2 = getWorkout();
        workout2.setId(2);
        workout2.setType("1");
        List<Workout> mockWorkouts = List.of(workout,workout2);
        String email = "test@email.com";
        User user = new User();
        user.setId(1);
        WorkoutType workoutType = new WorkoutType();
        workoutType.setType("1");

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByUserId(1)).thenReturn(mockWorkouts);
        when(workoutTypeService.findById(anyInt())).thenReturn(workoutType);
        when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(new WorkoutInfo());

        List<WorkoutDto> result = workoutService.getAllUserWorkouts(email);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Update type - success")
    void testUpdateType_Success() {
        WorkoutType workout = new WorkoutType();
        workout.setId(2);
        workout.setType("cardio");
        WorkoutType workout2 = new WorkoutType();
        workout2.setId(1);
        String newType = "NewType";
        workout2.setType(newType);

        when(workoutTypeService.findByUserId(1)).thenReturn(List.of(workout,workout2));

        workoutService.updateType(1, 1, newType);

        verify(workoutDao).updateType(1, 1);
    }

    @Test
    @DisplayName("Update duration - success")
    void testUpdateDuration_Success() {
        Duration duration = Duration.ofHours(1)
                .plusMinutes(50)
                .plusSeconds(1);
        doNothing().when(workoutDao).updateDuration(1,duration);

        workoutService.updateDuration(1,1, duration);

        verify(workoutDao).updateDuration(1,duration);
    }

    @Test
    @DisplayName("Update calories - success")
    void testUpdateCalories_Success() {
        doNothing().when(workoutDao).updateCalorie(anyInt(), anyFloat());

        workoutService.updateCalories(1,1,1f);

        verify(workoutDao).updateCalorie(anyInt(), anyFloat());
    }

    @Test
    @DisplayName("Update additional info - success")
    void testUpdateAdditionalInfo_Success() {
        doNothing().when(workoutInfoService).updateWorkoutInfo(anyInt(), anyString(), anyString());

        workoutService.updateAdditionalInfo(1, 1, Map.of("title","info"));

        verify(workoutInfoService).updateWorkoutInfo(anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("Delete workout - success")
    void testDeleteWorkout_Success() {
        Workout workout = getWorkout();
        String uuid = "123e4567-e89b-12d3-a456-426614174001";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByUUID(UUID.fromString(uuid))).thenReturn(Optional.of(workout));
        doNothing().when(workoutInfoService).delete(anyInt());
        doNothing().when(workoutDao).deleteWorkout(1, 1);

        workoutService.deleteWorkout(email, uuid);

        verify(workoutInfoService).delete(anyInt());
        verify(workoutDao).deleteWorkout(1,1);
    }

    @Test
    @DisplayName("Get workout statistics - success")
    void testGetWorkoutStatistics_Success() {
        Workout workout = getWorkout();
        String email = "test@email.com";
        String begin = "01-01-2022";
        String end = "02-01-2022";
        User user = new User();
        user.setId(1);

        when(workoutDao.findByDuration(1, LocalDate.parse("2022-01-01"), LocalDate.parse("2022-01-02")))
                .thenReturn(Arrays.asList(workout, workout));
        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        StatisticsDto result = workoutService.getWorkoutStatistics(email, begin, end);

        assertThat(result.getSumCalorie()).isEqualTo(246);
    }

    @Test
    @DisplayName("Get all users workouts - success")
    void testGetAllUsersWorkouts_Success() {
        User user = new User();
        user.setId(1);
        List<User> mockUsers = List.of(user, user);
        Workout workout = getWorkout();
        workout.setId(1);
        workout.setType("1");
        List<Workout> mockWorkouts = List.of(workout, workout);
        WorkoutType workoutType = new WorkoutType();
        workoutType.setType("1");
        WorkoutInfo workoutInfo = new WorkoutInfo();
        workoutInfo.setWorkoutInfo(Map.of("title","key"));

        when(workoutDao.findByUserId(1)).thenReturn(mockWorkouts);
        when(workoutTypeService.findById(1)).thenReturn(workoutType);
        when(workoutInfoService.getInfoByWorkoutId(1)).thenReturn(workoutInfo);

        List<User> result = workoutService.getAllUsersWorkouts(mockUsers);

        verify(workoutTypeService,times(4)).findById(1);
        verify(workoutInfoService,times(4)).getInfoByWorkoutId(1);
    }

    private Workout getWorkout() {
        Workout workout = new Workout();
        workout.setId(1);
        workout.setType("cardio");
        workout.setWorkoutInfo(new HashMap<>());
        workout.setDate(LocalDate.parse("2022-01-01"));
        workout.setCalorie(123.0f);
        return workout;
    }

}
