package com.heroslender.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("id")
public class PluginMetricRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metric_record_id_seq")
    @SequenceGenerator(name = "metric_record_id_seq", sequenceName = "metric_record_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "current")
    private Integer current;

    @Column(name = "max")
    private Integer record;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PluginMetricRecord that = (PluginMetricRecord) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "current = " + current + ", " +
                "record = " + record + ")";
    }
}
