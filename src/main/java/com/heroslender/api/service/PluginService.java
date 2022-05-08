package com.heroslender.api.service;

import com.heroslender.api.entity.Plugin;
import com.heroslender.api.entity.PluginMetricRecord;
import com.heroslender.api.repository.PluginRepository;
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

@Service
public class PluginService {
    GitHub github;
    private final PluginRepository pluginRepository;

    @Autowired
    public PluginService(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;

        try {
            this.github = new GitHubBuilder().withOAuthToken("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").build();
        } catch (IOException e) {
            System.out.println("Failed to connect to GitHub");
            throw new RuntimeException(e);
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePlugins();
            }
        }, 1000L, 1000L * 60 * 15);
    }

    public List<Plugin> getPlugins() {
        return pluginRepository.findAll();
    }

    public Plugin getPlugin(String name) {
        return pluginRepository
                .findByNameEqualsIgnoreCase(name)
                .orElseThrow(() -> new IllegalStateException("Plugin not found"));
    }

    public void updatePlugins() {
        System.out.println("Updating plugins...");
        List<Plugin> plugins = getPlugins();

        for (Plugin plugin : plugins) {
            System.out.println("https://bstats.org/api/v1/plugins/" + plugin.getBstatsId() + "/charts/" + "servers" + "/data/?maxElements=" + (2 * 24 * 30 * 3));
            try {
                int count = fetchDownloadCount(plugin.getName());

                plugin.setDownloadCount(count);

            } catch (IOException e) {
                System.out.println("Failed to update plugin " + plugin.getName() + ": " + e.getMessage());
            }

            PluginMetricRecord servers = fetchBstatsData(plugin.getBstatsId(), "servers");
            if (servers != null) {
                plugin.setServers(servers);
            }
            PluginMetricRecord players = fetchBstatsData(plugin.getBstatsId(), "players");
            if (players != null) {
                plugin.setPlayers(players);
            }

            pluginRepository.save(plugin);
        }
    }

    public int fetchDownloadCount(String name) throws IOException {
        if (github == null) {
            return 0;
        }

        PagedIterable<GHRelease> releases = github.getRepository("Heroslender/" + name).listReleases();
        int count = 0;
        for (GHRelease release : releases) {
            PagedIterable<GHAsset> assets = release.listAssets();
            for (GHAsset asset : assets) {
                if (asset.getName().endsWith(".jar")) {
                    count += asset.getDownloadCount();
                }
            }
        }

        return count;
    }

    public PluginMetricRecord fetchBstatsData(int bstatsId, String metric) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<long[][]> metricList = restTemplate.getForEntity("https://bstats.org/api/v1/plugins/" + bstatsId + "/charts/" + metric + "/data/?maxElements=" + (2 * 24 * 30 * 3), long[][].class);
        long[][] data = metricList.getBody();
        if (data == null || data.length == 0) {
            return null;
        }

        int current = (int) data[data.length - 1][1];
        int record = 0;
        for (long[] row : data) {
            record = Math.max(record, (int) row[1]);
        }

        return PluginMetricRecord.builder().record(record).current(current).build();
    }
}
