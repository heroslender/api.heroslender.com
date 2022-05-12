package com.heroslender.api.cache;

import com.heroslender.api.entity.GithubProfile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class GithubProfileCache {

    private GithubProfile profile;

    public GithubProfileCache() {
        System.out.println("GithubProfileCache created");
    }
}
