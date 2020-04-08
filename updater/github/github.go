package github

import (
	"bytes"
	"encoding/json"
	"net/http"
	"strings"
	"time"

	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/types"
	"github.com/heroslender/api.heroslender.com/updater/utils"
)

var client = http.Client{
	Timeout: time.Duration(5 * time.Second),
}

// GetPluginsInfo fetches the plugins releases info from the github graphql api
func GetPluginsInfo(user string, repos []string) (*[]GithubRepositoryReleases, error) {
	var reposStruct struct {
		Data map[string]struct {
			Name     string `json:"name"`
			Releases struct {
				Nodes []struct {
					TagName string `json:"tagName"`
					Assets  struct {
						Nodes []struct {
							Downloads int `json:"downloadCount"`
						}
					} `json:"releaseAssets"`
				} `json:"nodes"`
			} `json:"releases"`
		} `json:"data"`
	}

	var queryBuilder strings.Builder
	queryBuilder.WriteString("query {")
	for _, repo := range repos {
		queryBuilder.WriteString(repo)
		queryBuilder.WriteString(":repository(name:\"")
		queryBuilder.WriteString(repo)
		queryBuilder.WriteString("\", owner:\"")
		queryBuilder.WriteString(user)
		queryBuilder.WriteString(`") {
		  name,
		  releases(first:100) {
			nodes {
			  tagName
			  releaseAssets(first:1) {
				nodes {
				  downloadCount
				}
			  }
			}
		  }
		},`)
	}
	queryBuilder.WriteString("}")

	err := fetchGithub(queryBuilder.String(), &reposStruct)
	if err != nil {
		return nil, err
	}

	var toReturn = make([]GithubRepositoryReleases, len(reposStruct.Data))

	var i = 0
	for repo, releases := range reposStruct.Data {
		var count = 0
		var latest = ""
		for _, rel := range releases.Releases.Nodes {
			for _, asset := range rel.Assets.Nodes {
				count += asset.Downloads
			}

			latest = rel.TagName
		}

		toReturn[i] = GithubRepositoryReleases{
			Name:      repo,
			Version:   latest,
			Downloads: count,
		}

		i++
	}

	return &toReturn, nil
}

// GetUserInfo fetches the user info from the github graphql api
func GetUserInfo(user string) (*types.User, error) {
	var useInfo struct {
		Data struct {
			User GithubUser `json:"user"`
		} `json:"data"`
	}

	err := fetchGithub(`
query { 
  user(login:"`+user+`"){
    login,
    name,
    bio,
    location,
    url,
    avatarUrl,
    itemShowcase {
      items(first:3){
        nodes {
          ... on Repository {
            name,
            nameWithOwner
            description,
            forkCount,
            stargazers {
              totalCount
            }
            owner {
              login
            },
            primaryLanguage{
              name,
              color
            },
          }
        }
      }
    }
  }
}`, &useInfo)

	if err != nil {
		return nil, err
	}

	return mapUserInfo(&useInfo.Data.User), nil
}

func fetchGithub(query string, result interface{}) error {
	reqBody, err := json.Marshal(map[string]string{
		"query": query,
	})
	if err != nil {
		return err
	}

	req, err := http.NewRequest("POST", "https://api.github.com/graphql", bytes.NewBuffer(reqBody))
	if err != nil {
		return err
	}

	req.Header.Set("Authorization", "bearer "+env.GithubAPIKey)

	resp, err := client.Do(req)
	if err != nil {
		return err
	}

	defer resp.Body.Close()

	return utils.ParseResponse(resp, result)
}
