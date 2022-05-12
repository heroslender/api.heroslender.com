package com.heroslender.api.repository;

import com.heroslender.api.entity.GithubProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GithubProfileRepository extends JpaRepository<GithubProfile, Long> {
    Optional<GithubProfile> findByLogin(String name);

    Optional<GithubProfile> findByLoginEqualsIgnoreCase(String name);
}
