package com.ylab.intensive.service.impl;

import com.ylab.intensive.model.entity.WorkoutInfo;
import com.ylab.intensive.repository.WorkoutInfoDao;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Workout Info Service Tests")
public class WorkoutInfoServiceImplTest {

    @Mock
    private WorkoutInfoDao workoutInfoDao;

    @InjectMocks
    private WorkoutInfoServiceImpl workoutInfoService;

    @Test
    @DisplayName("Save workout info - success")
    void testSaveWorkoutInfo_Success() {
        int workoutId = 1;
        String title = "Title";
        String info = "Info";

        workoutInfoService.saveWorkoutInfo(workoutId, title, info);

        verify(workoutInfoDao).saveWorkoutInfo(workoutId, title, info);
    }

    @Nested
    @DisplayName("Positive testing")
    class Positive {
        @Test
        @DisplayName("Update workout info - success")
        void testUpdateWorkoutInfo_Success() {
            int workoutId = 1;
            String title = "Title";
            String info = "Updated Info";

            workoutInfoService.updateWorkoutInfo(workoutId, title, info);

            verify(workoutInfoDao).updateWorkoutInfo(workoutId, title, info);
        }

        @Test
        @DisplayName("Get info by workout ID - success")
        void testGetInfoByWorkoutId_Success() {
            int workoutId = 1;
            WorkoutInfo expectedInfo = new WorkoutInfo(10, 2, new HashMap<>());
            expectedInfo.getWorkoutInfo().put("Title", "Info");

            when(workoutInfoDao.findByWorkoutId(workoutId)).thenReturn(Optional.of(expectedInfo));

            assertThat(workoutInfoService.getInfoByWorkoutId(workoutId))
                    .isPresent()
                    .hasValueSatisfying(actualInfo -> assertThat(actualInfo).isEqualTo(expectedInfo));
        }

        @Test
        @DisplayName("Delete workout info - success")
        void testDelete_Success() {
            int workoutId = 1;

            workoutInfoService.delete(workoutId);

            verify(workoutInfoDao).delete(workoutId);
        }
    }

    @Nested
    @DisplayName("Negative testing")
    class Negative {
        @Test
        @DisplayName("Update workout info - failure")
        void testUpdateWorkoutInfo_Failure() {
            int workoutId = 2;
            String title = "newTitle";
            String info = "Updated Info";

            doNothing().when(workoutInfoDao).updateWorkoutInfo(workoutId, title, info);
            when(workoutInfoDao.findByWorkoutId(workoutId))
                    .thenReturn(Optional.of(new WorkoutInfo(1, 1, new HashMap<>())));

            workoutInfoService.updateWorkoutInfo(workoutId, title, info);

            assertThat(workoutInfoService.getInfoByWorkoutId(workoutId))
                    .isPresent()
                    .hasValueSatisfying(found -> {
                        assertThat(found.getWorkoutInfo()).doesNotContainKey(title);
                    });
        }

        @Test
        @DisplayName("Get info by workout ID - not found")
        void testGetInfoByWorkoutId_NotFound() {
            int workoutId = -1;

            when(workoutInfoDao.findByWorkoutId(workoutId)).thenReturn(Optional.empty());

            assertThat(workoutInfoService.getInfoByWorkoutId(workoutId))
                    .isEmpty();
        }
    }
}
