package github

import (
	"bytes"
	"encoding/json"
	"net/http"
	"os"
	"time"

	"github.com/heroslender/api.heroslender.com/types"
	"github.com/heroslender/api.heroslender.com/updater/utils"
)

var client = http.Client{
	Timeout: time.Duration(5 * time.Second),
}

var githubAPIKey = os.Getenv("GITHUB_API_KEY")

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

	req.Header.Set("Authorization", "bearer "+os.Getenv("GITHUB_API_KEY"))

	resp, err := client.Do(req)
	if err != nil {
		return err
	}

	defer resp.Body.Close()

	return utils.ParseResponse(resp, result)
}
