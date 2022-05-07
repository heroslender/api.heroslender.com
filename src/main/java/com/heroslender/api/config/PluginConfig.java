package com.heroslender.api.config;

import com.heroslender.api.entity.Plugin;
import com.heroslender.api.entity.PluginMetricRecord;
import com.heroslender.api.repository.PluginRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfig {

    @Bean
    CommandLineRunner runner(PluginRepository pluginRepository) {
        return args -> {
            Plugin heroSpawners = Plugin
                    .builder()
                    .name("HeroSpawners")
                    .version("1.0.0")
                    .downloadCount(312)
                    .players(PluginMetricRecord.builder().current(12).record(32).build())
                    .servers(PluginMetricRecord.builder().current(2).record(21).build())
                    .build();

            pluginRepository.save(heroSpawners);
        };
    }
}
