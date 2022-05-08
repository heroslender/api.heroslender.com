package com.heroslender.api.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubProfile {

    @Id
    @SequenceGenerator(name = "github_profile_seq", sequenceName = "github_profile_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "github_profile_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "display_name")
    private String displayname;

    @Column(name = "location")
    private String location;

    @Column(name = "bio")
    private String bio;

    @Column(name = "public_repos")
    private Integer public_repos;

    @Column(name = "stars")
    private Integer stars;

    @Column(name = "followers")
    private Integer followers;

    @Column(name = "following")
    private Integer following;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "github_profile_repos",
            joinColumns = @JoinColumn(name = "github_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "repo_id")
    )
    private List<GithubProfileRepo> repos;
}
