package com.heroslender.api.repository;

import com.heroslender.api.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PluginRepository extends JpaRepository<Plugin, Long> {
    Optional<Plugin> findByName(String name);

    Optional<Plugin> findByNameEqualsIgnoreCase(String name);
}
