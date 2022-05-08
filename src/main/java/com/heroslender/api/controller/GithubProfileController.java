package com.heroslender.api.controller;

import com.heroslender.api.entity.GithubProfile;
import com.heroslender.api.service.GithubProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GithubProfileController {

    @Autowired
    private GithubProfileService githubProfileService;

    @GetMapping
    public GithubProfile getGithubProfile() {
        return githubProfileService.getFirst();
    }

}
