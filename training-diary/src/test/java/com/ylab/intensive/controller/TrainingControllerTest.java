package com.ylab.intensive.controller;

import com.ylab.intensive.in.InputData;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.model.dto.WorkoutDto;
import com.ylab.intensive.service.WorkoutService;
import com.ylab.intensive.ui.AnsiColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Training Controller Unit Tests")
class TrainingControllerTest {

    @Mock
    private InputData inputData;

    @Mock
    private OutputData outputData;

    @Mock
    private WorkoutService workoutService;
    @Mock
    private AnsiColor color;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    @DisplayName("Test addTrainingType method")
    void testAddTrainingType() {
        when(inputData.input()).thenReturn("01-04-2024", "Running");
        when(color.greenBackground(anyString())).thenReturn("Новый тип тренировок успешно добавлен!");
        when(color.yellowBackground(anyString())).thenReturn("text");
        trainingController.addTrainingType();

        verify(workoutService).addTrainingType("01-04-2024", "Running");
        verify(outputData).output("Новый тип тренировок успешно добавлен!");
    }

    @Test
    @DisplayName("Test addWorkout method")
    void testAddWorkout() {
        when(inputData.input()).thenReturn("01-04-2024", "Running", "2:30:00", "500");
        when(color.greenBackground(anyString())).thenReturn("Новая тренировка успешно добавлена!");
        when(color.yellowBackground(anyString())).thenReturn("text");

        trainingController.addWorkout();

        verify(workoutService).addWorkout("01-04-2024", "Running", "2:30:00", "500");
        verify(outputData).output("Новая тренировка успешно добавлена!");
    }

    @Test
    @DisplayName("Test addWorkoutInfo method")
    void testAddWorkoutInfo() {
        when(inputData.input()).thenReturn("01-04-2024", "Distance", "5km");
        when(color.greenBackground(anyString())).thenReturn("Дополнительная информация о тренировке успешно добавлена!");
        when(color.yellowBackground(anyString())).thenReturn("text");

        trainingController.addWorkoutInfo();

        verify(workoutService).addWorkoutInfo("01-04-2024", "Distance", "5km");
        verify(outputData).output("Дополнительная информация о тренировке успешно добавлена!");
    }

    @Test
    @DisplayName("Test showWorkoutHistory method when workouts list is empty")
    void testShowWorkoutHistoryEmptyList() {

        List<WorkoutDto> emptyList = new ArrayList<>();
        when(workoutService.getAllWorkouts()).thenReturn(emptyList);

        trainingController.showWorkoutHistory();

        verify(outputData).errOutput("Вы еще не добавили тренировки!");
    }

    @Test
    @DisplayName("Test showWorkoutHistory method when workouts list is not empty")
    void testShowWorkoutHistoryNotEmptyList() {
        List<WorkoutDto> workouts = new ArrayList<>();
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setDuration(Duration.parse("PT4H56M45S"));
        workouts.add(workoutDto);

        when(workoutService.getAllWorkouts()).thenReturn(workouts);
        when(color.greenBackground(anyString())).thenReturn(workouts.toString());

        trainingController.showWorkoutHistory();

        verify(outputData).output(color.greenBackground(anyString()));
    }

    @Test
    @DisplayName("Test editWorkout method")
    void testEditWorkout() {
        WorkoutDto workoutDto = new WorkoutDto();
        when(inputData.input()).thenReturn("01-04-2024", "2", "2:00:00");
        when(workoutService.getWorkoutByDate("01-04-2024")).thenReturn(Optional.of(workoutDto));
        when(color.greenBackground(anyString()))
                .thenReturn("workoutDto.get().toString()")
                .thenReturn("Длительность тренировки успешно изменена!");
        when(color.yellowBackground(anyString())).thenReturn("text");
        when(color.greyBackground(anyString())).thenReturn("text");

        trainingController.editWorkout();

        verify(workoutService).updateDuration(workoutDto, "2:00:00");
        verify(outputData).output("Длительность тренировки успешно изменена!");
    }

    @Test
    @DisplayName("Test deleteWorkout method")
    void testDeleteWorkout() {
        when(color.yellowBackground(anyString())).thenReturn("text");
        when(inputData.input()).thenReturn("01-04-2024");

        trainingController.deleteWorkout();

        verify(workoutService).deleteWorkout("01-04-2024");
    }

    @Test
    @DisplayName("Test showWorkoutStatistics method")
    void testShowWorkoutStatistics() {
        when(color.yellowText(anyString())).thenReturn("text");
        when(color.yellowBackground(anyString()))
                .thenReturn("text");
        when(color.greenBackground(anyString())).thenReturn("Количество потраченных калорий в разрезе времени: 1000");
        when(inputData.input()).thenReturn("01-04-2024", "02-04-2024");
        when(workoutService.getWorkoutStatistics("01-04-2024", "02-04-2024")).thenReturn(1000);

        trainingController.showWorkoutStatistics();

        verify(outputData).output("Количество потраченных калорий в разрезе времени: 1000");
    }

}
