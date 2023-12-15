package com.application.task.repository;

import com.application.task.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryTaskRepository {

    Task save(String input, String pattern);

    List<Task> getAll();

    Optional<Task> getById(Integer id);

    Optional<Task> getByInputAndPattern(String input, String pattern);
}
