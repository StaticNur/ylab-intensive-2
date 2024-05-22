package com.ylab.intensive.service.impl;

import com.ylab.intensive.exception.WorkoutTypeException;
import com.ylab.intensive.model.entity.WorkoutType;
import com.ylab.intensive.repository.WorkoutTypeDao;
import com.ylab.intensive.tag.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Type Service Tests")
public class WorkoutTypeServiceImplTest {

    @Mock
    private WorkoutTypeDao workoutTypeDao;

    @InjectMocks
    private WorkoutTypeServiceImpl workoutTypeService;

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Save workout type - success")
        void testSaveType_Success() {
            int workoutId = 1;
            String typeName = "Cardio";

            workoutTypeService.saveType(workoutId, typeName);

            verify(workoutTypeDao).saveType(workoutId, typeName);
        }

        @Test
        @DisplayName("Find types by user ID - success")
        void testFindByUserId_Success() {
            int userId = 1;
            List<WorkoutType> expectedTypes = new ArrayList<>();
            expectedTypes.add(new WorkoutType());
            expectedTypes.add(new WorkoutType());

            when(workoutTypeDao.findByUserId(userId)).thenReturn(expectedTypes);

            assertThat(workoutTypeService.findByUserId(userId))
                    .isEqualTo(expectedTypes);
        }

        @Test
        @DisplayName("Update workout type - success")
        void testUpdateType_Success() {
            int userId = 1;
            String oldType = "Cardio";
            String newType = "Yoga";

            workoutTypeService.updateType(userId, oldType, newType);

            verify(workoutTypeDao).updateType(userId, oldType, newType);
        }

        @Test
        @DisplayName("Find types by name - success")
        void testFindByName_Success() {
            WorkoutType workoutType = new WorkoutType();
            workoutType.setType("type");
            when(workoutTypeDao.findByName(anyString())).thenReturn(Optional.of(workoutType));

            WorkoutType result = workoutTypeService.findByName("type");

            assertThat(result.getType()).isEqualTo("type");
        }

        @Test
        @DisplayName("Find types by user ID and name - success")
        void testFindTypeByUserId_Success() {
            WorkoutType workoutType = new WorkoutType();
            workoutType.setType("type");
            when(workoutTypeDao.findTypeByUserId(anyInt(), anyString())).thenReturn(Optional.of(workoutType));

            WorkoutType result = workoutTypeService.findTypeByUserId(1, "type");

            assertThat(result.getType()).isEqualTo("type");
        }

        @Test
        @DisplayName("Delete workout types - success")
        void testDelete_Success() {
            int userId = 1;

            workoutTypeService.delete(userId);

            verify(workoutTypeDao).delete(userId);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Не должен сохранять тип тренировки с недопустимым идентификатором пользователя")
        void shouldNotSaveWorkoutTypeWithInvalidUserId() {
            int invalidUserId = -1;
            String typeName = "Cardio";

            when(workoutTypeDao.findTypeByUserId(anyInt(), anyString())).thenReturn(Optional.of(new WorkoutType()));

            assertThatThrownBy(() -> workoutTypeService.saveType(invalidUserId, typeName))
                    .isInstanceOf(WorkoutTypeException.class);
        }
    }
}
