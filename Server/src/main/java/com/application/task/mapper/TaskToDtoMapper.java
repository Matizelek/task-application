package com.application.task.mapper;

import com.application.task.service.TaskService;
import com.application.task.dto.TaskResultDTO;
import com.application.task.dto.TaskResultDetailDTO;
import com.application.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring", uses= TaskService.class)
public interface TaskToDtoMapper {

    @Mapping(target = "taskId", source = "source.id")
    TaskResultDTO taskToTaskResultDto(Task source);

    TaskResultDetailDTO taskToTaskResultDetailDto(Task source);
}
