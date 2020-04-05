package types

type User struct {
	Name         string       `json:"login"`
	Displayname  string       `json:"name"`
	Bio          string       `json:"bio"`
	Location     string       `json:"location"`
	URL          string       `json:"url"`
	AvatarURL    string       `json:"avatar_url"`
	Repositories []Repository `json:"item_showcase"`
}

type Repository struct {
	Name        string             `json:"name"`
	Displayname string             `json:"displayname"`
	Description string             `json:"description"`
	Forks       int16              `json:"forks"`
	Stars       int16              `json:"stars"`
	Owner       string             `json:"owner"`
	Language    RepositoryLanguage `json:"language"`
}

type RepositoryLanguage struct {
	Name  string `json:"name"`
	Color string `json:"color"`
}
