package github

// GithubUser represents a user returned from the
// github graphql api
type GithubUser struct {
	Name         string `json:"login"`
	Displayname  string `json:"name"`
	Bio          string `json:"bio"`
	Location     string `json:"location"`
	URL          string `json:"url"`
	AvatarURL    string `json:"avatarUrl"`
	ItemShowcase struct {
		Items struct {
			Nodes []GithubRepository `json:"nodes"`
		} `json:"items"`
	} `json:"itemShowcase"`
}

// GithubRepository represents a repository returned from the
// github graphql api
type GithubRepository struct {
	Name        string `json:"name"`
	Displayname string `json:"nameWithOwner"`
	Description string `json:"description"`
	Forks       int16  `json:"forkCount"`
	Stargazers  struct {
		Count int16 `json:"totalCount"`
	} `json:"stargazers"`
	Owner struct {
		Name string `json:"login"`
	} `json:"owner"`
	Language struct {
		Name  string `json:"name"`
		Color string `json:"color"`
	} `json:"primaryLanguage"`
}

type GithubRepositoryReleases struct {
	Name      string
	Version   string
	Downloads int
}
