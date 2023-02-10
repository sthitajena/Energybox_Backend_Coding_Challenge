package com.energybox.backendcodingchallenge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.DatabaseSelection;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;

@Configuration
public class DatabaseConfig {
    @Bean
    DatabaseSelectionProvider databaseSelectionProvider(@Value("${spring.data.neo4j.database}") String database) {
        return () -> {

            return DatabaseSelection.undecided();

            //We can improve to connect specfic database, which is not on this scope
            //TO-DO
        };
    }
}