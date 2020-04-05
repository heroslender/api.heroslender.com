package types

type Plugin struct {
	ID      int         `json:"id"`
	Name    string      `json:"name"`
	Owner   string      `json:"owner"`
	Servers PluginUsage `json:"servers"`
	Players PluginUsage `json:"players"`
}

type PluginUsage struct {
	Current int `json:"current"`
	Maximum int `json:"maximum"`
}
