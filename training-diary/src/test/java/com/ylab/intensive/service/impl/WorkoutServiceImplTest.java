package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.exception.TrainingTypeException;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.mapper.WorkoutMapper;
import com.ylab.intensive.model.security.Session;
import com.ylab.intensive.service.AuditService;
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
    private AuditService auditService;

    @Mock
    private WorkoutMapper workoutMapper;

    @Mock
    private Session authorizedUser;

    @Mock
    private UserManagementServiceImpl userManagementService;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @Test
    @DisplayName("Add training type - success")
    void testAddTrainingType_Success() {
        String date = "01-01-2022";
        String typeName = "Cardio";
        Workout workout = new Workout();
        workout.setId(1);
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(workoutDao.findByDate(any())).thenReturn(Optional.of(workout));
        when(workoutTypeService.findByWorkoutId(anyInt())).thenReturn(Set.of());
        doNothing().when(workoutTypeService).saveType(anyInt(), anyString());
        doNothing().when(auditService).saveAction(anyInt(), anyString());
        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));

        workoutService.addTrainingType(date, typeName);

        verify(auditService).saveAction(anyInt(), anyString());
    }

    @Test
    @DisplayName("Add training type - training already exists")
    void testAddTrainingType_TypeExists() {
        String date = "01-01-2022";
        String typeName = "Cardio";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(new Workout()));
        when(workoutTypeService.findByWorkoutId(anyInt())).thenReturn(Set.of(typeName));

        assertThatThrownBy(() -> workoutService.addTrainingType(date, typeName))
                .isInstanceOf(TrainingTypeException.class)
                .hasMessageContaining("Такой тип тренировки уже существует в " + date);
    }

    @Test
    @DisplayName("Add workout info - success")
    void testAddWorkoutInfo_Success() {
        String date = "01-01-2022";
        String title = "Title";
        String info = "Info";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(new Workout()));
        doNothing().when(workoutInfoService).saveWorkoutInfo(anyInt(), anyString(), anyString());
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        workoutService.addWorkoutInfo(date, title, info);

        verify(auditService).saveAction(anyInt(), anyString());
    }

    @Test
    @DisplayName("Add workout info - workout not found")
    void testAddWorkoutInfo_WorkoutNotFound() {
        String date = "01-01-2022";
        String title = "Title";
        String info = "Info";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.empty());
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        assertThatThrownBy(() -> workoutService.addWorkoutInfo(date, title, info))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Тренировка в " + date + " не проводилось! Сначала добавьте ее.");
    }

    @Test
    @DisplayName("Get all user workouts - success")
    void testGetAllUserWorkouts_Success() {
        List<Workout> mockWorkouts = Arrays.asList(Workout.builder().id(1).date(LocalDate.now()).build(),
                Workout.builder().id(2).date(LocalDate.now()).build());
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByUserId(1)).thenReturn(mockWorkouts);
        when(workoutTypeService.findByWorkoutId(anyInt())).thenReturn(Set.of());
        when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(Collections.emptyMap());
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        List<WorkoutDto> result = workoutService.getAllUserWorkouts();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Get workout by date - success")
    void testGetWorkoutByDate_Success() {
        String date = "01-01-2022";
        String email = "test@email.com";
        Workout workout = Workout.builder().id(1).date(LocalDate.now()).build();
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(workout));
        when(workoutTypeService.findByWorkoutId(anyInt())).thenReturn(Set.of());
        when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(Collections.emptyMap());
        when(workoutMapper.entityToDto(workout)).thenReturn(new WorkoutDto());

        WorkoutDto result = workoutService.getWorkoutByDate(date);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Update type - success")
    void testUpdateType_Success() {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(LocalDate.now());
        Workout workout = Workout.builder().id(1).date(LocalDate.now()).build();
        String oldType = "OldType";
        String newType = "NewType";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(workoutDto.getDate())).thenReturn(Optional.of(workout));

        workoutService.updateType(workoutDto, oldType, newType);

        verify(workoutTypeService).updateType(1, oldType, newType);
        verify(auditService).saveAction(1, "Пользователь редактировал тип тренировки с OldType на NewType");
    }

    @Test
    @DisplayName("Update duration - success")
    void testUpdateDuration_Success() {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(LocalDate.now());
        String durationStr = "1:5:24";
        Workout workout = Workout.builder().id(1).date(LocalDate.now()).build();
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(workout));

        workoutService.updateDuration(workoutDto, durationStr);

        verify(auditService).saveAction(1, "Пользователь редактировал длительность тренировки, теперь " + durationStr);
    }

    @Test
    @DisplayName("Update calories - success")
    void testUpdateCalories_Success() {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(LocalDate.now());
        String calories = "300";
        Workout workout = Workout.builder().id(1).date(LocalDate.now()).build();
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(workout));
        doNothing().when(workoutDao).updateCalorie(anyInt(), anyFloat());
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        workoutService.updateCalories(workoutDto, calories);

        verify(auditService).saveAction(anyInt(), anyString());
    }

    @Test
    @DisplayName("Update additional info - success")
    void testUpdateAdditionalInfo_Success() {
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(LocalDate.now());
        workoutDto.setInfo(new HashMap<>());
        String title = "Title";
        String info = "Info";
        workoutDto.getInfo().put(title, info);
        Workout workout = Workout.builder().id(1).date(LocalDate.now()).build();
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(workout));
        doNothing().when(workoutInfoService).updateWorkoutInfo(anyInt(), anyString(), anyString());
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        workoutService.updateAdditionalInfo(workoutDto, title, info);

        verify(auditService).saveAction(anyInt(), anyString());
    }

    @Test
    @DisplayName("Delete workout - success")
    void testDeleteWorkout_Success() {
        String date = "01-01-2022";
        String email = "test@email.com";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));
        when(workoutDao.findByDate(any())).thenReturn(Optional.of(new Workout()));
        doNothing().when(workoutTypeService).delete(anyInt());
        doNothing().when(workoutInfoService).delete(anyInt());
        doNothing().when(workoutDao).deleteWorkout(any(LocalDate.class));
        doNothing().when(auditService).saveAction(anyInt(), anyString());

        workoutService.deleteWorkout(date);

        verify(auditService).saveAction(anyInt(), anyString());
    }

    @Test
    @DisplayName("Get workout statistics - success")
    void testGetWorkoutStatistics_Success() {
        String email = "test@email.com";
        String begin = "01-01-2022";
        String end = "02-01-2022";
        UserDto userDto = new UserDto();
        userDto.setEmail(email);
        User user = new User();
        user.setId(1);
        user.setEmail(email);

        when(workoutDao.findByDuration(any(), any()))
                .thenReturn(Arrays.asList(Workout.builder().id(1).date(LocalDate.now()).calorie(123f).build(),
                        Workout.builder().id(2).date(LocalDate.now()).calorie(123f).build()));
        doNothing().when(auditService).saveAction(1, "Пользователь просмотрел статистику по тренировкам за период " + begin + " -- " + end);
        when(authorizedUser.getAttribute("authorizedUser")).thenReturn(userDto);
        when(userManagementService.findByEmail(email)).thenReturn(Optional.of(user));

        int result = workoutService.getWorkoutStatistics(begin, end);

        assertThat(result).isEqualTo(246);
    }

    @Test
    @DisplayName("Get all users workouts - success")
    void testGetAllUsersWorkouts_Success() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        List<Workout> mockWorkouts = Arrays.asList(new Workout(), new Workout());

        when(workoutDao.findByUserId(anyInt())).thenReturn(mockWorkouts);
        when(workoutTypeService.findByWorkoutId(anyInt())).thenReturn(Set.of());
        when(workoutInfoService.getInfoByWorkoutId(anyInt())).thenReturn(Collections.emptyMap());

        List<User> result = workoutService.getAllUsersWorkouts(mockUsers);

        assertThat(result).hasSize(2);
    }

}
