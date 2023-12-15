package com.application.task.configuration;

import com.application.task.repository.InventoryTaskRepository;
import com.application.task.repository.MemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public InventoryTaskRepository inventoryTaskRepository(){
        return new MemoryRepository();
    }
}
