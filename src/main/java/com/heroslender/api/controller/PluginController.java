package com.heroslender.api.controller;

import com.heroslender.api.entity.Plugin;
import com.heroslender.api.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plugins")
public class PluginController {

    @Autowired
    private PluginService pluginService;

    @GetMapping
    public List<Plugin> getPlugins() {
        return pluginService.getPlugins();
    }

    @GetMapping(path = "/{plugin}")
    public Plugin getPlugin(@PathVariable("plugin") String plugin) {
        return pluginService.getPlugin(plugin);
    }
}
