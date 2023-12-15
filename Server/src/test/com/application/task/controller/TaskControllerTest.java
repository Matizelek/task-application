package com.application.task.controller;


import com.application.task.dto.TaskResultDTO;
import com.application.task.dto.TaskResultDetailDTO;
import com.application.task.exception.InvalidInputDataException;
import com.application.task.exception.TaskDuplicateException;
import com.application.task.exception.TaskNotFoundException;
import com.application.task.service.TaskService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.application.task.enums.TaskResult.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {


    public static final String INPUT = "input";
    public static final String PATTERN = "pattern";
    @Mock
    private static TaskService taskService;


    @InjectMocks
    private static TaskController taskController;

    @BeforeAll
    static void before(){
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    void createTask_Should_returnCorrectResponse_When_everythingOk() throws Throwable {
        TaskResultDTO taskResultDTO = new TaskResultDTO(1, INPUT, PATTERN);
        CompletableFuture<TaskResultDTO> future = CompletableFuture.completedFuture(taskResultDTO);

        when(taskService.createTask(INPUT, PATTERN)).thenReturn(future);

        ResponseEntity<TaskResultDTO> result = taskController.createTask(INPUT, PATTERN);

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isEqualTo(taskResultDTO);
        });
    }

    @Test
    void createTask_Should_returnResponseWithErrorCode406_When_serverReturnInvalidInputDataException() throws Throwable {
        CompletableFuture<TaskResultDTO> future = CompletableFuture.failedFuture(new InvalidInputDataException("Error message"));

        when(taskService.createTask(INPUT, PATTERN)).thenReturn(future);

        ResponseEntity<TaskResultDTO> result = taskController.createTask(INPUT, PATTERN);

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
            assertThat(response.getBody()).isNull();
        });
    }

    @Test
    void createTask_Should_returnResponseWithErrorCode406_When_serverReturnDuplicateException() throws Throwable {
        CompletableFuture<TaskResultDTO> future = CompletableFuture.failedFuture(new TaskDuplicateException("Error message"));

        when(taskService.createTask(INPUT, PATTERN)).thenReturn(future);

        ResponseEntity<TaskResultDTO> result = taskController.createTask(INPUT, PATTERN);

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
            assertThat(response.getBody()).isNull();
        });
    }

    @Test
    void checkStatusOfAllTasks_Should_returnCorrectResponse_When_everythingOk() {
        TaskResultDTO taskResultDTO = new TaskResultDTO(1, INPUT, PATTERN);
        List<TaskResultDTO> list = List.of(taskResultDTO);
        CompletableFuture<List<TaskResultDTO>> future = CompletableFuture.completedFuture(list);

        when(taskService.checkStatusOfAllTasks()).thenReturn(future);

        ResponseEntity<List<TaskResultDTO>> result = taskController.checkStatusOfAllTasks();

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isEqualTo(list);
        });
    }

    @Test
    void checkStatusOfTask_Should_returnCorrectResponse_When_everythingOk() throws Throwable {
        TaskResultDetailDTO taskResultDetailDTO = new TaskResultDetailDTO(1, INPUT, PATTERN, 2,3, "process", SUCCESS);
        CompletableFuture<TaskResultDetailDTO> future = CompletableFuture.completedFuture(taskResultDetailDTO);

        when(taskService.checkStatus(1)).thenReturn(future);

        ResponseEntity<TaskResultDetailDTO> result = taskController.checkStatusOfTask(1);

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull().isEqualTo(taskResultDetailDTO);
        });
    }

    @Test
    void checkStatusOfTask_Should_returnResponseWithErrorCode404_When_serverReturnTaskNotFoundException() throws Throwable {
        CompletableFuture<TaskResultDetailDTO> future = CompletableFuture.failedFuture(new TaskNotFoundException(""));

        when(taskService.checkStatus(1)).thenReturn(future);

        ResponseEntity<TaskResultDetailDTO> result = taskController.checkStatusOfTask(1);

        assertThat(result).isNotNull().satisfies(response -> {
            assertThat(response.getStatusCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isNull();
        });
    }
}