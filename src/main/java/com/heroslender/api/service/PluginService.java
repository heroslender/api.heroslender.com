package com.heroslender.api.service;

import com.heroslender.api.cache.PluginCache;
import com.heroslender.api.entity.Plugin;
import com.heroslender.api.entity.PluginMetricRecord;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PluginService {
    private final GitHub github;
    private final PluginCache pluginCache;

    @Autowired
    public PluginService(PluginCache pluginCache, GitHub github) {
        this.pluginCache = pluginCache;
        this.github = github;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePlugins();
            }
        }, 1000L * 60 * 15, 1000L * 60 * 15);
    }

    public List<Plugin> getPlugins() {
        return pluginCache.findAll();
    }

    public Plugin getPlugin(String name) {
        return pluginCache
                .findByNameEqualsIgnoreCase(name)
                .orElseThrow(() -> new IllegalStateException("Plugin not found"));
    }

    public void updatePlugins() {
        log.info("Updating plugins...");
        for (Plugin plugin : getPlugins()) {
            updatePlugin(plugin);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public CompletableFuture<Void> updatePlugin(Plugin plugin) {
        return CompletableFuture.runAsync(() -> {
            log.info("Updating plugin {}...", plugin.getName());
            try {
                updateFromGitHub(plugin);
            } catch (IOException e) {
                log.info("Failed to update plugin {}: {}", plugin.getName(), e.getMessage());
            }

            plugin.setServers(fetchBstatsData(plugin.getBstatsId(), "servers"));
            plugin.setPlayers(fetchBstatsData(plugin.getBstatsId(), "players"));

            log.info("Updated plugin {}.", plugin.getName());
        });
    }

    public void updateFromGitHub(Plugin plugin) throws IOException {
        if (github == null) {
            return;
        }

        GHRepository repository = github.getRepository("Heroslender/" + plugin.getName());
        PagedIterable<GHRelease> releases = repository.listReleases();
        int count = 0;
        for (GHRelease release : releases) {
            PagedIterable<GHAsset> assets = release.listAssets();
            for (GHAsset asset : assets) {
                if (asset.getName().endsWith(".jar")) {
                    count += asset.getDownloadCount();
                }
            }
        }

        plugin.setDownloadCount(count);
        plugin.setVersion(repository.getLatestRelease().getTagName());
    }

    @NotNull
    public PluginMetricRecord fetchBstatsData(int bstatsId, String metric) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<long[][]> metricList = restTemplate.getForEntity("https://bstats.org/api/v1/plugins/" + bstatsId + "/charts/" + metric + "/data/?maxElements=" + (2 * 24 * 30 * 3), long[][].class);
        long[][] data = metricList.getBody();
        if (data == null || data.length == 0) {
            return new PluginMetricRecord(null, 0, 0);
        }

        int current = (int) data[data.length - 1][1];
        int record = 0;
        for (long[] row : data) {
            record = Math.max(record, (int) row[1]);
        }

        return PluginMetricRecord.builder().record(record).current(current).build();
    }
}
