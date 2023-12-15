package com.application.task.dto;

import com.application.task.enums.TaskResult;

public class TaskResultDetailDTO {

    private final Integer id;
    private final String input;
    private final String pattern;
    private final Integer position;
    private final Integer typos;
    private final String progress;
    private final TaskResult taskResult;


    public TaskResultDetailDTO(Integer id, String input, String pattern, Integer position, Integer typos, String progress, TaskResult taskResult) {
        this.id = id;
        this.input = input;
        this.pattern = pattern;
        this.position = position;
        this.typos = typos;
        this.progress = progress;
        this.taskResult = taskResult;
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
        return "TaskResultDetailDTO{" +
                "id=" + id +
                ", input='" + input + '\'' +
                ", pattern='" + pattern + '\'' +
                ", position=" + position +
                ", typos=" + typos +
                ", progress='" + progress + '\'' +
                ", taskResult=" + taskResult +
                '}';
    }
}
