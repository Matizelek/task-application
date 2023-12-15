package com.application.task.entity;

import com.application.task.enums.TaskResult;

import java.util.Objects;

import static com.application.task.enums.TaskResult.*;

public class Task {

    private final Integer id;
    private final String input;
    private final String pattern;
    private Integer position;
    private Integer typos;
    private String progress;
    private TaskResult taskResult;

    public Task(Integer id, String input, String pattern) {
        this.id = id;
        this.input = input;
        this.pattern = pattern;
        this.progress = "0%";
        this.taskResult = NEW;
    }

    public Integer getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getPattern() {
        return pattern;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getTypos() {
        return typos;
    }

    public String getProgress() {
        return progress;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", input='" + input + '\'' +
                ", pattern='" + pattern + '\'' +
                ", position=" + position +
                ", typos=" + typos +
                ", progress='" + progress + '\'' +
                ", taskResult=" + taskResult +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(input, task.input) && Objects.equals(pattern, task.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, pattern);
    }

    public void finishSuccessfully(Integer position, int typos) {
        this.position = position;
        this.typos = typos;
        this.taskResult = SUCCESS;
        this.progress = "100%";
    }

    public void finishFailed() {
        this.taskResult = FAILED;
        this.progress = "100%";
    }

    public void updateProgress(long percentage) {
        this.progress = percentage + "%";
    }
}
