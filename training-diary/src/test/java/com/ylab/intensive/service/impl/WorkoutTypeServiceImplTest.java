/*
package com.ylab.intensive.service.impl;

import com.ylab.intensive.dao.WorkoutTypeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Type Service Tests")
public class WorkoutTypeServiceImplTest {

    @Mock
    private WorkoutTypeDao workoutTypeDao;

    @InjectMocks
    private WorkoutTypeServiceImpl workoutTypeService;

    @Test
    @DisplayName("Save workout type - success")
    void testSaveType_Success() {
        int workoutId = 1;
        String typeName = "Cardio";

        workoutTypeService.saveType(workoutId, typeName);

        verify(workoutTypeDao).saveType(workoutId, typeName);
    }

    @Test
    @DisplayName("Find types by workout ID - success")
    void testFindByWorkoutId_Success() {
        int workoutId = 1;
        Set<String> expectedTypes = new HashSet<>();
        expectedTypes.add("Cardio");
        expectedTypes.add("Strength");

        when(workoutTypeDao.findByUserId(workoutId)).thenReturn(expectedTypes);

        Set<String> result = workoutTypeService.findByUserId(workoutId);

        assertThat(result).isEqualTo(expectedTypes);
    }

    @Test
    @DisplayName("Update workout type - success")
    void testUpdateType_Success() {
        int workoutId = 1;
        String oldType = "Cardio";
        String newType = "Yoga";

        workoutTypeService.updateType(workoutId, oldType, newType);

        verify(workoutTypeDao).updateType(workoutId, oldType, newType);
    }

    @Test
    @DisplayName("Delete workout types - success")
    void testDelete_Success() {
        int workoutId = 1;

        workoutTypeService.delete(workoutId);

        verify(workoutTypeDao).delete(workoutId);
    }
}
*/
