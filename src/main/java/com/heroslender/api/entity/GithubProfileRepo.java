package com.heroslender.api.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubProfileRepo {

    @Id
    @SequenceGenerator(name = "github_profile_repo_seq", sequenceName = "github_profile_repo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "github_profile_repo_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "description")
    private String description;

    @Column(name = "language")
    private String language;

    @Column(name = "language_color")
    private String languageColor = "#b07219";

    @Column(name = "url")
    private String url;

    @Column(name = "stars")
    private int stars;

    @Column(name = "forks")
    private Integer forks;
}
