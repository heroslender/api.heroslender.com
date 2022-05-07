package com.heroslender.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("id")
public class Plugin {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plugin_id_seq")
    @SequenceGenerator(name = "plugin_id_seq", sequenceName = "plugin_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Column(name = "download_count")
    private Integer downloadCount;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "players_metrics_id", unique = true)
    private PluginMetricRecord players;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "servers_metrics_id", unique = true)
    private PluginMetricRecord servers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Plugin plugin = (Plugin) o;
        return id != null && Objects.equals(id, plugin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "version = " + version + ", " +
                "downloadCount = " + downloadCount + ", " +
                "players = " + players + ", " +
                "servers = " + servers + ")";
    }
}
