package bstats

type Plugin struct {
	ID    int    `json:"id"`
	Name  string `json:"name"`
	Owner struct {
		Name string `json:"name"`
	} `json:"owner"`
}

type Graph [][]int
