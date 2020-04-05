package github

import (
	"github.com/heroslender/api.heroslender.com/types"
)

func mapUserInfo(userInfo *GithubUser) *types.User {
	repos := make([]types.Repository, len(userInfo.ItemShowcase.Items.Nodes))

	for _, repo := range userInfo.ItemShowcase.Items.Nodes {
		repos = append(repos, types.Repository{
			Name:        repo.Name,
			Displayname: repo.Displayname,
			Description: repo.Description,
			Forks:       repo.Forks,
			Stars:       repo.Stargazers.Count,
			Owner:       repo.Owner.Name,
			Language: types.RepositoryLanguage{
				Name:  repo.Language.Name,
				Color: repo.Language.Color,
			},
		})
	}

	return &types.User{
		Name:         userInfo.Name,
		Displayname:  userInfo.Displayname,
		Bio:          userInfo.Bio,
		Location:     userInfo.Location,
		URL:          userInfo.URL,
		AvatarURL:    userInfo.AvatarURL,
		Repositories: repos,
	}
}
