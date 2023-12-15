package com.application.task.controller;

import com.application.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        this.mockMvc = standaloneSetup(new TaskController(taskService)).build();
    }


    @Test
    public void createTask_Should_returnResponseWithNewTask_When_EverythingIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCD")
                        .queryParam("pattern", "BCD")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"taskId\"")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCD\",\"pattern\":\"BCD\"}")));
    }

    @Test
    public void createTask_Should_returnResponseWithErrorCode406_When_inputIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "")
                        .queryParam("pattern", "BCD")
                )
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createTask_Should_returnResponseWithErrorCode406_When_patternIsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCD")
                        .queryParam("pattern", "")
                )
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createTask_Should_returnResponseWithErrorCode406_When_taskAlreadyExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCD")
                        .queryParam("pattern", "BWD")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"taskId\"")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCD\",\"pattern\":\"BWD\"}")));

        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCD")
                        .queryParam("pattern", "BWD")
                )
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void checkStatusOfAllTasks_Should_returnResponseWithAllTask_When_EverythingOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCDEFG")
                        .queryParam("pattern", "CFG")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"taskId\"")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCDEFG\",\"pattern\":\"CFG\"}")));

        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCABC")
                        .queryParam("pattern", "ABC")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"taskId\"")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCABC\",\"pattern\":\"ABC\"}")));

        mockMvc.perform(MockMvcRequestBuilders.get("/task/all"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCDEFG\",\"pattern\":\"CFG\"}")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCABC\",\"pattern\":\"ABC\"}")));
    }

    @Test
    public void checkStatusOfTask_Should_returnResponseWithTask_When_EverythingOk() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/task")
                        .queryParam("input", "ABCDEFG")
                        .queryParam("pattern", "TDD")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"taskId\"")))
                .andExpect(MockMvcResultMatchers
                        .content().string(containsString("\"input\":\"ABCDEFG\",\"pattern\":\"TDD\"}")));

        mockMvc.perform(MockMvcRequestBuilders.get("/task")
                        .queryParam("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(startsWith("{\"id\":1")));
    }
}
