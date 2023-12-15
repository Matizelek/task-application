package com.application.task.repository;

import com.application.task.entity.Task;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryRepository implements InventoryTaskRepository{

    private static final Map<Integer, Task> repository = new ConcurrentHashMap<>();

    private static final AtomicInteger idCounter = new AtomicInteger();

    @Override
    public Task save(final String input, final String pattern) {
        Integer id = idCounter.incrementAndGet();
        Task task = new Task(id, input, pattern);
        repository.put(id, task);
        return task;
    }

    @Override
    public List<Task> getAll() {
//        Map<Integer, Task> readOnlyMap = Collections.unmodifiableMap(repository);
        return new ArrayList<>(repository.values());
    }

    @Override
    public Optional<Task> getById(final Integer id) {
        try{
//            Map<Integer, Task> readOnlyMap = Collections.unmodifiableMap(repository);
            return Optional.of(repository.get(id));
        } catch (final NullPointerException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Task> getByInputAndPattern(final String input, final String pattern) {
        Map<Integer, Task> readOnlyMap = Collections.unmodifiableMap(repository);
        return readOnlyMap.values()
                .stream()
                .filter(task -> Objects.equals(task.getInput(), input) && Objects.equals(task.getPattern(), pattern))
                .findFirst();
    }
}
