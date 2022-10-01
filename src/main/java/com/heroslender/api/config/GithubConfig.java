package com.heroslender.api.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.io.IOException;

@Configuration
public class GithubConfig {

    @Bean
    @Nullable
    GitHub getGithub() {
        try {
            return GitHubBuilder.fromEnvironment().build();
        } catch (IOException e) {
            System.err.println("Failed to connect to GitHub");
        }

        return null;
    }
}
