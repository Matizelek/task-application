package com.application.task.controller;

import com.application.task.service.TaskService;
import com.application.task.dto.TaskResultDTO;
import com.application.task.dto.TaskResultDetailDTO;
import com.application.task.exception.TaskDuplicateException;
import com.application.task.exception.InvalidInputDataException;
import com.application.task.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResultDTO> createTask(@RequestParam final String input, @RequestParam final String pattern) throws Throwable {

        TaskResultDTO result;
        try {
            CompletableFuture<TaskResultDTO> futureResult = taskService.createTask(input, pattern);
            futureResult.isCompletedExceptionally();
            result = futureResult.join();
        } catch (CompletionException ex) {
            try {
                throw ex.getCause();
            } catch (final InvalidInputDataException | TaskDuplicateException e) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
        }

        return ResponseEntity.ok()
                .body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResultDTO>> checkStatusOfAllTasks() {

        List<TaskResultDTO> result;
        try {
            CompletableFuture<List<TaskResultDTO>> futureResult = taskService.checkStatusOfAllTasks();
            result = futureResult.join();
        } catch (CompletionException ex) {
            try {
                throw ex.getCause();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok()
                .body(result);

    }

    @GetMapping
    public ResponseEntity<TaskResultDetailDTO> checkStatusOfTask(@RequestParam final Integer id) throws Throwable {

        TaskResultDetailDTO result;
        try {
            CompletableFuture<TaskResultDetailDTO> futureResult = taskService.checkStatus(id);
            result = futureResult.join();
        } catch (CompletionException ex) {
            try {
                throw ex.getCause();
            } catch (TaskNotFoundException possible) {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok()
                .body(result);

    }
}
