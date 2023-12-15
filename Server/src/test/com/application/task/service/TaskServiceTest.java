package com.application.task.service;


import com.application.task.dto.TaskResultDTO;
import com.application.task.dto.TaskResultDetailDTO;
import com.application.task.entity.Task;
import com.application.task.exception.InvalidInputDataException;
import com.application.task.exception.TaskDuplicateException;
import com.application.task.exception.TaskNotFoundException;
import com.application.task.mapper.TaskToDtoMapper;
import com.application.task.repository.InventoryTaskRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;


import static com.application.task.enums.TaskResult.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class TaskServiceTest {


    public static final String INPUT = "input";
    public static final String PATTERN = "pattern";
    @Mock
    private static InventoryTaskRepository inventoryTaskRepository;

    @Mock
    private static TaskToDtoMapper taskToDtoMapper;

    @InjectMocks
    static TaskService taskService;

    @BeforeAll
    static void before(){
        inventoryTaskRepository = mock(InventoryTaskRepository.class);
        taskToDtoMapper = mock(TaskToDtoMapper.class);
        taskService = new TaskService(inventoryTaskRepository, taskToDtoMapper);
        taskService.setCorePoolSize(1);
    }

    @Test
    void createTask_Should_returnCorrectFuture_When_everythingOk() throws ExecutionException, InterruptedException {
        TaskResultDTO taskResultDTO = new TaskResultDTO(1, INPUT, PATTERN);
        var task = new Task(1, INPUT, PATTERN);
        when(inventoryTaskRepository.save(INPUT, PATTERN)).thenReturn(task);
        when(taskToDtoMapper.taskToTaskResultDto(task)).thenReturn(taskResultDTO);

        CompletableFuture<TaskResultDTO> result = taskService.createTask(INPUT, PATTERN);

        assertThat(result).isNotNull();
        assertThat(result.get()).isNotNull().isEqualTo(taskResultDTO);
    }

    @Test
    void createTask_Should_throwInvalidInputDataException_When_inputIsNull() {

        Exception result = assertThrows(CompletionException.class, () -> taskService.createTask(null, PATTERN));

        assertThat(result).isNotNull().satisfies(exception -> {
            assertThat(exception).isInstanceOf(CompletionException.class);
            assertThat(exception.getCause()).isNotNull().isInstanceOf(InvalidInputDataException.class);
            assertThat(exception.getMessage()).isNotNull();
        });
    }

    @Test
    void createTask_Should_throwInvalidInputDataException_When_patternIsNull() {

        Exception result = assertThrows(CompletionException.class, () -> taskService.createTask(INPUT, null));

        assertThat(result).isNotNull().satisfies(exception -> {
            assertThat(exception).isInstanceOf(CompletionException.class);
            assertThat(exception.getCause()).isNotNull().isInstanceOf(InvalidInputDataException.class);
            assertThat(exception.getMessage()).isNotNull();
        });
    }

    @Test
    void createTask_Should_throwTaskDuplicateException_When_taskAlreadyExist() {

        when(inventoryTaskRepository.getByInputAndPattern(INPUT, PATTERN))
                .thenReturn(Optional.of(new Task(1, "input", "pattern")));

        Exception result = assertThrows(CompletionException.class, () -> taskService.createTask(INPUT, PATTERN));

        assertThat(result).isNotNull().satisfies(exception -> {
            assertThat(exception).isInstanceOf(CompletionException.class);
            assertThat(exception.getCause()).isNotNull().isInstanceOf(TaskDuplicateException.class);
            assertThat(exception.getMessage()).isNotNull();
        });
    }

    @Test
    void checkStatusOfAllTasks_Should_returnCorrectFuture_When_everythingOk() throws ExecutionException, InterruptedException {
        TaskResultDTO taskResultDTO = new TaskResultDTO(1, INPUT, PATTERN);
        List<TaskResultDTO> taskResultDtoList = List.of(taskResultDTO);
        var task = new Task(1, INPUT, PATTERN);
        List<Task> taskList = List.of(task);
        when(inventoryTaskRepository.getAll()).thenReturn(taskList);
        when(taskToDtoMapper.taskToTaskResultDto(task)).thenReturn(taskResultDTO);

        CompletableFuture<List<TaskResultDTO>> result = taskService.checkStatusOfAllTasks();

        assertThat(result).isNotNull();
        assertThat(result.get()).isNotNull().isEqualTo(taskResultDtoList);
    }

    @Test
    void checkStatus_Should_returnCorrectFuture_When_everythingOk() throws ExecutionException, InterruptedException {
        TaskResultDetailDTO taskResultDetailDTO = new TaskResultDetailDTO(1, INPUT, PATTERN, 1, 1, "process", SUCCESS);
        var task = new Task(1, INPUT, PATTERN);
        when(inventoryTaskRepository.getById(1)).thenReturn(Optional.of(task));
        when(taskToDtoMapper.taskToTaskResultDetailDto(task)).thenReturn(taskResultDetailDTO);

        CompletableFuture<TaskResultDetailDTO> result = taskService.checkStatus(1);

        assertThat(result).isNotNull();
        assertThat(result.get()).isNotNull().isEqualTo(taskResultDetailDTO);
    }

    @Test
    void checkStatus_Should_throwTaskNotFoundException_When_taskIsNotExist() {

        when(inventoryTaskRepository.getById(1)).thenReturn(Optional.empty());

        Exception result = assertThrows(CompletionException.class, () -> taskService.checkStatus(2));

        assertThat(result).isNotNull().satisfies(exception -> {
            assertThat(exception).isInstanceOf(CompletionException.class);
            assertThat(exception.getCause()).isNotNull().isInstanceOf(TaskNotFoundException.class);
            assertThat(exception.getMessage()).isNotNull();
        });
    }

}