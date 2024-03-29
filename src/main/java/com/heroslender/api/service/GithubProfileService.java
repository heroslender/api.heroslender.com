package com.heroslender.api.service;

import com.heroslender.api.cache.GithubProfileCache;
import com.heroslender.api.entity.GithubProfile;
import com.heroslender.api.entity.GithubProfileRepo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class GithubProfileService {
    private final GitHub github;
    private final GithubProfileCache githubProfileCache;

    @Autowired
    public GithubProfileService(GithubProfileCache githubProfileCache, GitHub github) {
        this.githubProfileCache = githubProfileCache;
        this.github = github;

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                GithubProfile profile = githubProfileCache.getProfile();

                try {
                    update(profile);

                    githubProfileCache.setProfile(profile);
                } catch (IOException e) {
                    log.info("Error updating profile {}: {}", profile.getLogin(), e.getMessage());
                }
            }
        }, 1000L * 60 * 15, 1000L * 60 * 15);
    }

    public GithubProfile getFirst() {
        GithubProfile profile = githubProfileCache.getProfile();
        profile.getRepos().sort(Comparator.comparing(GithubProfileRepo::getStars).reversed());
        return profile;
    }

    public void update() throws IOException {
        GithubProfile profile = githubProfileCache.getProfile();
        update(profile);
    }

    private void update(GithubProfile githubProfile) throws IOException {
        log.info("Updating profile: {}...", githubProfile.getLogin());

        GHUser user = github.getUser(githubProfile.getLogin());
        githubProfile.setDisplayname(user.getName());
        githubProfile.setPublic_repos(user.getPublicRepoCount());
        githubProfile.setBio(user.getBio());
        githubProfile.setLocation(user.getLocation());
        githubProfile.setFollowers(user.getFollowersCount());
        githubProfile.setFollowing(user.getFollowingCount());

        log.info("Updating stars...");
        int count = 0;
        for (GHRepository ignored : user.listStarredRepositories()) {
            count++;
        }

        githubProfile.setStars(count);

        log.info("Updating repos...");
        for (GithubProfileRepo repo : githubProfile.getRepos()) {
            log.info("Updating repo: {}...", repo.getFullName());
            GHRepository repository = github.getRepository(repo.getFullName());
            repo.setName(repository.getOwnerName().equals(user.getLogin()) ? repository.getName() : repository.getFullName());
            repo.setDescription(repository.getDescription());
            repo.setUrl(repository.getHtmlUrl().toString());
            repo.setLanguage(repository.getLanguage());
            repo.setLanguageColor("#b07219");
            repo.setStars(repository.getStargazersCount());
            repo.setForks(repository.getForksCount());
        }
    }

    public String getLanguageColor(String language) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ColorsList colorsList = restTemplate.getForObject(
                "https://raw.githubusercontent.com/ozh/github-colors/master/colors.json",
                ColorsList.class
        );

        if (colorsList == null) {
            return "#000000";
        }

        return colorsList.getColors().get(language);
    }

    @Data
    public static class ColorsList {
        public Map<String, String> colors;
    }
}
