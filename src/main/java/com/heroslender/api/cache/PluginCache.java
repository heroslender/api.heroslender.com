package com.heroslender.api.cache;

import com.heroslender.api.entity.Plugin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Component
public class PluginCache {
    private final Map<String, Plugin> plugins = new HashMap<>();

    public void save(Plugin plugin) {
        plugins.put(plugin.getName().toLowerCase(Locale.ROOT), plugin);
    }

    public void remove(Plugin plugin) {
        plugins.remove(plugin.getName().toLowerCase(Locale.ROOT));
    }

    public void clear() {
        plugins.clear();
    }

    public Optional<Plugin> findByNameEqualsIgnoreCase(String name) {
        return Optional.ofNullable(plugins.get(name.toLowerCase(Locale.ROOT)));
    }

    public List<Plugin> findAll() {
        return new ArrayList<>(plugins.values());
    }
}
