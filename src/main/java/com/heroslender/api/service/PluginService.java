package com.heroslender.api.service;

import com.heroslender.api.entity.Plugin;
import com.heroslender.api.repository.PluginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PluginService {

    @Autowired
    private PluginRepository pluginRepository;

    public List<Plugin> getPlugins() {
        return pluginRepository.findAll();
    }

    public Plugin getPlugin(String name) {
        return pluginRepository
                .findByName(name)
                .orElseThrow(() -> new IllegalStateException("Plugin not found"));
    }
}
