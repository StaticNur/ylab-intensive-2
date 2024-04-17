package com.ylab.intensive.dao.impl;

import com.ylab.intensive.model.entity.Workout;
import com.ylab.intensive.model.dto.WorkoutDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Database Operations Testing")
class WorkoutDaoImplTest {

    @InjectMocks
    private WorkoutDaoImpl workoutDao;

    @Test
    @DisplayName("Get size of workout DB")
    void testGetSize() {
        List<Workout> workoutDB = new ArrayList<>();
        workoutDao.init(workoutDB);

        int result = workoutDao.getSize();

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Find workout by date - Workout exists")
    void testFindByDate_WorkoutExists() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        workoutDB.add(new Workout(1, date, Collections.emptySet(), Duration.ofHours(1), 500.0f, Collections.emptyMap()));
        workoutDao.init(workoutDB);

        Optional<Workout> result = workoutDao.findByDate(date);

        assertTrue(result.isPresent());
        assertEquals(date, result.get().getDate());
    }

    @Test
    @DisplayName("Find workout by date - Workout does not exist")
    void testFindByDate_WorkoutDoesNotExist() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        workoutDao.init(workoutDB);

        Optional<Workout> result = workoutDao.findByDate(date);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Save workout type")
    void testSaveType() {
        Workout workout = new Workout();
        workout.setType(new HashSet<>());
        String type = "Running";

        workoutDao.saveType(workout, type);

        assertTrue(workout.getType().contains(type));
    }

    @Test
    @DisplayName("Save workout")
    void testSaveWorkout() {
        List<Workout> workoutDB = new ArrayList<>();
        workoutDao.init(workoutDB);
        Workout workout = new Workout();

        workoutDao.saveWorkout(workout);

        assertEquals(1, workoutDB.size());
        assertTrue(workoutDB.contains(workout));
    }

    @Test
    @DisplayName("Save workout info")
    void testSaveWorkoutInfo() {
        Workout workout = new Workout();
        workout.setInfo(new HashMap<>());
        String title = "Title";
        String info = "Info";

        workoutDao.saveWorkoutInfo(workout, title, info);

        assertEquals(info, workout.getInfo().get(title));
    }

    @Test
    @DisplayName("Find all workouts")
    void testFindAll() {
        List<Workout> workoutDB = new ArrayList<>();
        workoutDB.add(new Workout());
        workoutDB.add(new Workout());
        workoutDao.init(workoutDB);

        List<Workout> result = workoutDao.findAll();

        assertEquals(workoutDB.size(), result.size());
        assertTrue(workoutDB.containsAll(result));
    }

    @Test
    @DisplayName("Delete workout")
    void testDeleteWorkout() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        Workout workout = new Workout(1, date, Collections.emptySet(), Duration.ofHours(1), 500.0f, Collections.emptyMap());
        workoutDao.init(workoutDB);

        workoutDao.deleteWorkout(date);

        assertTrue(workoutDB.isEmpty());
    }

    @Test
    @DisplayName("Update workout info")
    void testUpdateWorkoutInfo() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        Workout workout = new Workout(1, date, new HashSet<>(), Duration.ofHours(1), 500.0f, new HashMap<>());
        workoutDB.add(workout);
        workoutDao.init(workoutDB);
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(date);
        String title = "Title";
        String info = "Info";

        workoutDao.updateWorkoutInfo(workoutDto, title, info);

        assertEquals(info, workout.getInfo().get(title));
    }

    @Test
    @DisplayName("Update workout calorie")
    void testUpdateCalorie() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        Workout workout = new Workout(1, date, Collections.emptySet(), Duration.ofHours(1), 500.0f, Collections.emptyMap());
        workoutDB.add(workout);
        workoutDao.init(workoutDB);
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(date);
        Float calorie = 600.0f;

        workoutDao.updateCalorie(workoutDto, calorie);

        assertEquals(calorie, workout.getCalorie());
    }

    @Test
    @DisplayName("Update workout duration")
    void testUpdateDuration() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        Workout workout = new Workout(1, date, Collections.emptySet(), Duration.ofHours(1), 500.0f, Collections.emptyMap());
        workoutDB.add(workout);
        workoutDao.init(workoutDB);
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(date);
        Duration duration = Duration.ofHours(2);

        workoutDao.updateDuration(workoutDto, duration);

        assertEquals(duration, workout.getDuration());
    }

    @Test
    @DisplayName("Update workout type")
    void testUpdateType() {
        LocalDate date = LocalDate.of(2024, 4, 11);
        List<Workout> workoutDB = new ArrayList<>();
        Workout workout = new Workout(1, date, new HashSet<>(), Duration.ofHours(1), 500.0f, new HashMap<>());
        workoutDB.add(workout);
        workoutDao.init(workoutDB);
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDate(date);
        String oldType = "OldType";
        String newType = "NewType";
        workout.getType().add(oldType);

        workoutDao.updateType(workoutDto, oldType, newType);

        assertFalse(workout.getType().contains(oldType));
        assertTrue(workout.getType().contains(newType));
    }

    @Test
    @DisplayName("Find workouts by duration")
    void testFindByDuration() {
        LocalDate begin = LocalDate.of(2024, 4, 1);
        LocalDate end = LocalDate.of(2024, 4, 10);
        List<Workout> workoutDB = new ArrayList<>();
        workoutDB.add(new Workout(1, LocalDate.of(2024, 4, 5), Collections.emptySet(), Duration.ofHours(1), 500.0f, Collections.emptyMap()));
        workoutDB.add(new Workout(2, LocalDate.of(2024, 4, 8), Collections.emptySet(), Duration.ofHours(2), 700.0f, Collections.emptyMap()));
        workoutDB.add(new Workout(3, LocalDate.of(2024, 4, 15),Collections.emptySet(),  Duration.ofHours(1), 500.0f,Collections.emptyMap()));
        workoutDao.init(workoutDB);

        List<Workout> result = workoutDao.findByDuration(begin, end);

        assertEquals(2, result.size());
    }
}
