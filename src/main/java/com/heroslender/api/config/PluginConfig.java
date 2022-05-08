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
                    .bstatsId(2088)
                    .panelBackground("background-image: url(/assets/images/herospawners_banner.png)")
                    .version("1.0.0")
                    .downloadCount(0)
                    .players(PluginMetricRecord.builder().current(0).record(0).build())
                    .servers(PluginMetricRecord.builder().current(0).record(0).build())
                    .build();

            Plugin heroStackDrops = Plugin
                    .builder()
                    .name("HeroStackDrops")
                    .bstatsId(5041)
                    .panelBackground("background-image: url(https://github.com/heroslender/HeroStackDrops/raw/master/assets/preview.png); background-position: 90% -10%;")
                    .version("1.0.0")
                    .downloadCount(0)
                    .players(PluginMetricRecord.builder().current(0).record(0).build())
                    .servers(PluginMetricRecord.builder().current(0).record(0).build())
                    .build();

            Plugin heroMagnata = Plugin
                    .builder()
                    .name("HeroMagnata")
                    .bstatsId(1621)
                    .panelBackground("background-image: url(https://github.com/heroslender/HeroMagnata/raw/master/assets/preview_npc.png); background-position: -50% 50%; background-size: 100%;")
                    .version("1.0.0")
                    .downloadCount(0)
                    .players(PluginMetricRecord.builder().current(0).record(0).build())
                    .servers(PluginMetricRecord.builder().current(0).record(0).build())
                    .build();

            Plugin heroVender = Plugin
                    .builder()
                    .name("HeroVender")
                    .bstatsId(3757)
                    .panelBackground("background-image: url(/assets/images/herospawners_banner.png)")
                    .version("1.0.0")
                    .downloadCount(0)
                    .players(PluginMetricRecord.builder().current(0).record(0).build())
                    .servers(PluginMetricRecord.builder().current(0).record(0).build())
                    .build();

            pluginRepository.save(heroSpawners);
            pluginRepository.save(heroStackDrops);
            pluginRepository.save(heroMagnata);
            pluginRepository.save(heroVender);
        };
    }
}
