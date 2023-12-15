package com.application.task.processing;

import com.application.task.entity.Task;
import org.junit.jupiter.api.Test;

import static com.application.task.enums.TaskResult.FAILED;
import static com.application.task.enums.TaskResult.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

class TaskProcessorTest {

    TaskProcessor taskProcessor = new TaskProcessor(0L);

    @Test
    void processTask_Should_returnPosition1AndTypos0_When_inputABCDAndPatternBCS() {
        // Given
        Task task = new Task(1, "ABCD", "BCD");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
            assertThat(s.getPosition()).isEqualTo(1);
            assertThat(s.getTypos()).isEqualTo(0);
            assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
            assertThat(s.getTaskResult()).isNotNull().isEqualTo(SUCCESS);
                }
        );
    }

    @Test
    void processTask_Should_returnPosition1AndTypos1_When_inputABCDAndPatternBWD() {
        // Given
        Task task = new Task(1, "ABCD", "BWD");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
                    assertThat(s.getPosition()).isEqualTo(1);
                    assertThat(s.getTypos()).isEqualTo(1);
                    assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
                    assertThat(s.getTaskResult()).isNotNull().isEqualTo(SUCCESS);
                }
        );
    }

    @Test
    void processTask_Should_returnPosition4AndTypos1_When_inputABCDEFGAndPatternCFG() {
        // Given
        Task task = new Task(1, "ABCDEFG", "CFG");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
                    assertThat(s.getPosition()).isEqualTo(4);
                    assertThat(s.getTypos()).isEqualTo(1);
                    assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
                    assertThat(s.getTaskResult()).isNotNull().isEqualTo(SUCCESS);
                }
        );
    }

    @Test
    void processTask_Should_returnPosition0AndTypos0_When_inputABCABCAndPatternABC() {
        // Given
        Task task = new Task(1, "ABCABC,", "ABC");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
                    assertThat(s.getPosition()).isEqualTo(0);
                    assertThat(s.getTypos()).isEqualTo(0);
                    assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
                    assertThat(s.getTaskResult()).isNotNull().isEqualTo(SUCCESS);
                }
        );
    }

    @Test
    void processTask_Should_returnPosition1AndTypos2_When_inputABCDEFGAndPatternTDD() {
        // Given
        Task task = new Task(1, "ABCDEFG", "TDD");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
                    assertThat(s.getPosition()).isEqualTo(1);
                    assertThat(s.getTypos()).isEqualTo(2);
                    assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
                    assertThat(s.getTaskResult()).isNotNull().isEqualTo(SUCCESS);

                }
        );
    }

    @Test
    void processTask_Should_returnTaskResultFailed_When_pattenCantFindPosition() {
        // Given
        Task task = new Task(1, "ABCDEFG", "XYZ");

        // When
        taskProcessor.processTask(task);

        // Then
        assertThat(task).isNotNull().satisfies(s -> {
                    assertThat(s.getPosition()).isNull();
                    assertThat(s.getTypos()).isNull();
                    assertThat(s.getProgress()).isNotNull().isEqualTo("100%");
                    assertThat(s.getTaskResult()).isNotNull().isEqualTo(FAILED);
                }
        );
    }
}