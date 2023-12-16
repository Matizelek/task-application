package com.application.task.repository;

import com.application.task.entity.Task;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryRepository implements InventoryTaskRepository{

    private static final Map<Integer, Task> repository = new ConcurrentHashMap<>();

    private static final AtomicInteger idCounter = new AtomicInteger();

    @Override
    @CacheEvict(value="tasks", allEntries = true)
    public Task save(final String input, final String pattern) {
        Integer id = idCounter.incrementAndGet();
        Task task = new Task(id, input, pattern);
        repository.put(id, task);
        return task;
    }

    @Override
    @Cacheable("tasks")
    public List<Task> getAll() {
        return List.copyOf(repository.values());
    }

    @Override
    @Cacheable("tasks")
    public Optional<Task> getById(final Integer id) {
        try{
            return Optional.of(Collections.unmodifiableMap(repository).get(id));
        } catch (final NullPointerException e){
            return Optional.empty();
        }
    }

    @Override
    @Cacheable("tasks")
    public Optional<Task> getByInputAndPattern(final String input, final String pattern) {
        return Collections.unmodifiableMap(repository).values()
                .stream()
                .filter(task -> Objects.equals(task.getInput(), input) && Objects.equals(task.getPattern(), pattern))
                .findFirst();
    }
}
