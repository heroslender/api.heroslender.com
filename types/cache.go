package types

type CachedData struct {
	GitHub  User     `json:"github"`
	Plugins []Plugin `json:"plugins"`
}
