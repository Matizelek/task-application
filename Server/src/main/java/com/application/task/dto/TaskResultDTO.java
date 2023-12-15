package com.application.task.dto;

public class TaskResultDTO {

    private final Integer taskId;
    private final String input;
    private final String pattern;


    public TaskResultDTO(Integer taskId, String input, String pattern) {
        this.taskId = taskId;
        this.input = input;
        this.pattern = pattern;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getInput() {
        return input;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "TaskResultDTO{" +
                "taskId=" + taskId +
                ", input='" + input + '\'' +
                ", pattern='" + pattern + '\'' +
                '}';
    }
}
