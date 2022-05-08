package com.heroslender.api.service;

import com.heroslender.api.entity.GithubProfile;
import com.heroslender.api.entity.GithubProfileRepo;
import com.heroslender.api.repository.GithubProfileRepository;
import lombok.Data;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class GithubProfileService {
    private final GitHub github;
    private final GithubProfileRepository githubProfileRepository;

    @Autowired
    public GithubProfileService(GithubProfileRepository githubProfileRepository, GitHub github) {
        this.githubProfileRepository = githubProfileRepository;
        this.github = github;

//        System.out.println(getLanguageColor("Java"));


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (GithubProfile profile : githubProfileRepository.findAll()) {
                    System.out.println("Updating profile: " + profile.getLogin());
                    try {
                        update(profile);

                        githubProfileRepository.save(profile);
                    } catch (IOException e) {
                        System.out.println("Error updating profile " + profile.getLogin() + ": " + e.getMessage());
                    }
                }
            }
        }, 1000L, 1000L * 60 * 15);
    }

    public GithubProfile getFirst() {
        GithubProfile profile = githubProfileRepository.findAll().iterator().next();
        profile.getRepos().sort(Comparator.comparing(GithubProfileRepo::getStars).reversed());
        return profile;
    }

    private void update(GithubProfile githubProfile) throws IOException {
        GHUser user = github.getUser(githubProfile.getLogin());
        githubProfile.setDisplayname(user.getName());
        githubProfile.setPublic_repos(user.getPublicRepoCount());
        githubProfile.setBio(user.getBio());
        githubProfile.setLocation(user.getLocation());
        githubProfile.setFollowers(user.getFollowersCount());
        githubProfile.setFollowing(user.getFollowingCount());

        int count = 0;
        for (GHRepository repository : user.listStarredRepositories()) {
            count++;
        }

        githubProfile.setStars(count);

        for (GithubProfileRepo repo : githubProfile.getRepos()) {
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
        System.out.println(Arrays.toString(colorsList.getColors().entrySet().toArray()));
        return colorsList.getColors().get(language);
    }

    @Data
    public static class ColorsList {
        public Map<String, String> colors;
    }
}
