package com.heroslender.api.config;

import com.heroslender.api.entity.GithubProfile;
import com.heroslender.api.entity.GithubProfileRepo;
import com.heroslender.api.repository.GithubProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GithubProfileConfig {

    @Bean
    CommandLineRunner githubProfileConfigRunner(GithubProfileRepository repository) {
        return args -> {
            GithubProfile profile = GithubProfile
                    .builder()
                    .login("heroslender")
                    .repos(List.of(
                            GithubProfileRepo.builder().fullName("heroslender/menu-framework").build(),
                            GithubProfileRepo.builder().fullName("heroslender/HeroVender").build(),
                            GithubProfileRepo.builder().fullName("heroslender/HeroStackDrops").build()
                    ))
                    .build();

            repository.save(profile);
        };
    }
}
