package com.application.task.service;

import com.application.task.exception.InvalidInputDataException;
import com.application.task.exception.TaskDuplicateException;
import com.application.task.exception.TaskNotFoundException;
import com.application.task.dto.TaskResultDTO;
import com.application.task.dto.TaskResultDetailDTO;
import com.application.task.entity.Task;
import com.application.task.mapper.TaskToDtoMapper;
import com.application.task.processing.TaskProcessor;
import com.application.task.repository.InventoryTaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Value("${task.processing.threadSleep:1000}")
    long taskProcessingThreadSleep;

    @Autowired
    private final InventoryTaskRepository inventoryTaskRepository;

    @Autowired
    private final TaskToDtoMapper taskToDtoMapper;

    public TaskService(InventoryTaskRepository inventoryTaskRepository, TaskToDtoMapper taskToDtoMapper) {
        this.inventoryTaskRepository = inventoryTaskRepository;
        this.taskToDtoMapper = taskToDtoMapper;
    }

    @Async
    public CompletableFuture<TaskResultDTO> createTask(final String input, final String pattern){
        logger.info("createTask() start with input=" + input + " and pattern=" + pattern + " Thread name " + Thread.currentThread().getName());
        boolean invalid = validateInput(input, pattern);
        if(invalid){
            logger.warn("createTask() Input=" + input + " or pattern=" + pattern + " are invalid.");
            throw new CompletionException(new InvalidInputDataException("createTask() Input=" + input + " or pattern=" + pattern + " are invalid."));
        }
        Optional<Task> taskFromRepository = inventoryTaskRepository.getByInputAndPattern(input, pattern);

        if(taskFromRepository.isPresent()){
            logger.warn("createTask() Task with Input=" + input + " and pattern=" + pattern + " are already exist.");
            throw new CompletionException(new TaskDuplicateException("createTask() Task with Input=" + input + " and pattern=" + pattern + " are already exist."));
        }

        Task task = inventoryTaskRepository.save(input, pattern);

        TaskProcessor utils = new TaskProcessor(taskProcessingThreadSleep);
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.submit(() -> utils.processTask(task));


        TaskResultDTO result = taskToDtoMapper.taskToTaskResultDto(task);
        logger.info("createTask() finish with result=" + result);
        return CompletableFuture.completedFuture(result);
    }

    private boolean validateInput(final String input, final String pattern) {
        return StringUtils.isBlank(input) || StringUtils.isBlank(pattern);
    }

    @Async
    public CompletableFuture<List<TaskResultDTO>> checkStatusOfAllTasks() {
        logger.info("checkStatusOfAllTasks() start Thread name " + Thread.currentThread().getName());

        List<TaskResultDTO> result = inventoryTaskRepository.getAll()
                .stream()
                .map(taskToDtoMapper::taskToTaskResultDto)
                .collect(Collectors.toList());

        logger.info("checkStatusOfAllTasks() finish with result=" + result);
        return CompletableFuture.completedFuture(result);
    }

    @Async
    public CompletableFuture<TaskResultDetailDTO> checkStatus(final Integer taskId) {

        logger.info("checkStatus() start with taskId=" + taskId + " Thread name " + Thread.currentThread().getName());
        Optional<Task> optionalTask = inventoryTaskRepository.getById(taskId);
        if(optionalTask.isEmpty()){
            logger.warn("checkStatus() task with taskId=" + taskId + " was not found");
            throw new CompletionException(new TaskNotFoundException("checkStatus() task with taskId=" + taskId + " was not found"));
        }

        TaskResultDetailDTO result = taskToDtoMapper.taskToTaskResultDetailDto(optionalTask.get());
        logger.info("checkStatus() finish with result=" + result);
        return CompletableFuture.completedFuture(result);
    }
}
