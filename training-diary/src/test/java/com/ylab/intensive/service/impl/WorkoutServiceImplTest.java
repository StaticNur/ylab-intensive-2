package com.ylab.intensive.service.impl;

import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.dto.*;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.repository.WorkoutDao;
import com.ylab.intensive.service.WorkoutInfoService;
import com.ylab.intensive.service.WorkoutTypeService;
import com.ylab.intensive.tag.UnitTest;
import com.ylab.intensive.util.converter.Converter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@UnitTest
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

    @Mock
    private Converter converter;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
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
            String email = "email";
            WorkoutInfoDto workoutInfo = new WorkoutInfoDto();
            workoutInfo.setWorkoutInfo(Map.of("title", "info"));
            User user = new User();
            user.setId(1);
            Workout workoutMy = getWorkout();
            workoutMy.setType("1");
            workoutMy.setUserId(1);

            when(converter.convert(anyString(), any(), anyString())).thenReturn(UUID.fromString(uuid));
            when(userService.findByEmail(email)).thenReturn(Optional.of(user));
            when(workoutDao.findByUUID(UUID.fromString(uuid))).thenReturn(Optional.of(workoutMy));
            when(workoutTypeService.findByName(anyString())).thenReturn(new WorkoutType());
            doNothing().when(workoutInfoService).saveWorkoutInfo(anyInt(), anyString(), anyString());

            Workout workout = workoutService.addWorkoutInfo(email, uuid, workoutInfo);

            assertThat(workout.getWorkoutInfo())
                    .isEqualTo(workoutInfo.getWorkoutInfo());
        }

        @Test
        @DisplayName("Get all user workouts - success")
        void testGetAllUserWorkouts_Success() {
            Workout workout = getWorkout();
            workout.setId(1);
            workout.setType("type");
            List<Workout> mockWorkouts = List.of(workout);
            User user = new User();
            user.setId(1);

            when(workoutDao.findByUserId(1)).thenReturn(mockWorkouts);
            when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(Optional.of(new WorkoutInfo()));

            assertThat(workoutService.getAllUsersWorkouts(List.of(user)))
                    .hasSize(1);
        }

        @Test
        @DisplayName("Update type - success")
        void testUpdateType_Success() {
            WorkoutType workout = new WorkoutType();
            workout.setId(1);
            String newType = "NewType";
            workout.setType(newType);

            when(workoutTypeService.findTypeByUserId(anyInt(), anyString())).thenReturn(workout);

            workoutService.updateType(1, 1, newType);

            verify(workoutDao).updateType(1, newType);
        }

        @Test
        @DisplayName("Update duration - success")
        void testUpdateDuration_Success() {
            Duration duration = Duration.ofHours(1)
                    .plusMinutes(50)
                    .plusSeconds(1);
            doNothing().when(workoutDao).updateDuration(1, duration);

            workoutService.updateDuration(1, duration);

            verify(workoutDao).updateDuration(1, duration);
        }

        @Test
        @DisplayName("Update calories - success")
        void testUpdateCalories_Success() {
            doNothing().when(workoutDao).updateCalorie(anyInt(), anyFloat());

            workoutService.updateCalories(1, 1f);

            verify(workoutDao).updateCalorie(anyInt(), anyFloat());
        }

        @Test
        @DisplayName("Update additional info - success")
        void testUpdateAdditionalInfo_Success() {
            WorkoutInfo workoutInfo = new WorkoutInfo();
            HashMap<String, String> map = new HashMap<>();
            map.put("title", "info");
            workoutInfo.setWorkoutInfo(map);
            when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(Optional.of(workoutInfo));
            doNothing().when(workoutInfoService).updateWorkoutInfo(anyInt(), anyString(), anyString());

            Map<String, String> infoMap = workoutService.updateAdditionalInfo(1, Map.of("title", "newInfo"));

            assertThat(infoMap.size()).isEqualTo(1);
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
            when(converter.convert(anyString(), any(), anyString())).thenReturn(UUID.fromString(uuid));
            when(workoutDao.findByUUID(UUID.fromString(uuid))).thenReturn(Optional.of(workout));
            doNothing().when(workoutInfoService).delete(anyInt());
            doNothing().when(workoutDao).deleteWorkout(1, 1);

            workoutService.deleteWorkout(email, uuid);

            verify(workoutInfoService).delete(anyInt());
            verify(workoutDao).deleteWorkout(1, 1);
        }

        @Test
        @DisplayName("Get workout statistics - success")
        void testGetWorkoutStatistics_Success() {
            Workout workout = getWorkout();
            String email = "test@email.com";
            String date = "2022-01-01";
            User user = new User();
            user.setId(1);

            when(workoutDao.findByDuration(1, LocalDate.parse(date), LocalDate.parse(date)))
                    .thenReturn(Arrays.asList(workout, workout));
            when(converter.convert(anyString(), any(), anyString())).thenReturn(LocalDate.parse(date));
            when(userService.findByEmail(email)).thenReturn(Optional.of(user));

            StatisticsDto result = workoutService.getWorkoutStatistics(email, date, date);

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
            workoutInfo.setWorkoutInfo(Map.of("title", "key"));

            when(workoutDao.findByUserId(1)).thenReturn(mockWorkouts);
            when(workoutInfoService.getInfoByWorkoutId(1)).thenReturn(Optional.of(workoutInfo));

            List<User> result = workoutService.getAllUsersWorkouts(mockUsers);

            verify(workoutInfoService, times(4)).getInfoByWorkoutId(1);
            assertThat(result.size()).isEqualTo(mockWorkouts.size());
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен добавлять тип тренировки, если пользователь не существует")
        void shouldNotAddWorkoutTypeIfUserDoesNotExist() {
            String typeName = "Cardio";
            String email = "nonexistent@email.com";

            when(userService.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> workoutService.saveWorkoutType(email, typeName))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Не должен добавлять информацию о тренировке, если пользователь не существует")
        void shouldNotAddWorkoutInfoIfUserDoesNotExist() {
            String uuid = "123e4567-e89b-12d3-a456-426614174001";
            String email = "nonexistent@email.com";
            WorkoutInfoDto workoutInfo = new WorkoutInfoDto();

            when(userService.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> workoutService.addWorkoutInfo(email, uuid, workoutInfo))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("Не должен удалять тренировку, если пользователь не существует")
        void shouldNotDeleteWorkoutIfUserDoesNotExist() {
            String email = "nonexistent@email.com";
            String uuid = "123e4567-e89b-12d3-a456-426614174001";

            when(userService.findByEmail(email)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> workoutService.deleteWorkout(email, uuid))
                    .isInstanceOf(NotFoundException.class);
        }
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
