package com.heroslender.api.config;

import com.heroslender.api.cache.GithubProfileCache;
import com.heroslender.api.entity.GithubProfile;
import com.heroslender.api.entity.GithubProfileRepo;
import com.heroslender.api.service.GithubProfileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GithubProfileConfig {

    @Bean
    CommandLineRunner githubProfileConfigRunner(GithubProfileCache cache, GithubProfileService githubProfileService) {
        return args -> {
            GithubProfile profile = GithubProfile
                    .builder()
                    .login("heroslender")
                    .repos(new ArrayList<>(List.of(
                            GithubProfileRepo.builder().fullName("heroslender/menu-framework").build(),
                            GithubProfileRepo.builder().fullName("heroslender/HeroVender").build(),
                            GithubProfileRepo.builder().fullName("heroslender/HeroStackDrops").build()
                    )))
                    .build();

            cache.setProfile(profile);

            githubProfileService.update();
        };
    }
}
