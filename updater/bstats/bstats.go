package bstats

import (
	"net/http"
	"strconv"
	"time"

	"github.com/heroslender/api.heroslender.com/types"
	"github.com/heroslender/api.heroslender.com/updater/utils"
	"github.com/sirupsen/logrus"
)

var client = http.Client{
	Timeout: time.Duration(5 * time.Second),
}

// GetPluginInfo fetches the whole plugin data, including
// servers & player usage from the bstats api
func GetPluginInfo(pluginID int) (*types.Plugin, error) {
	var plugin Plugin

	err := fetchPlugin(pluginID, &plugin)
	if err != nil {
		return nil, err
	}

	servers, err := GetUsage(pluginID, "servers")
	if err != nil {
		return nil, err
	}
	players, err := GetUsage(pluginID, "players")
	if err != nil {
		return nil, err
	}

	return &types.Plugin{
		ID:      plugin.ID,
		Name:    plugin.Name,
		Owner:   plugin.Owner.Name,
		Players: *players,
		Servers: *servers,
	}, nil
}

// GetUsage fetchs the plugin usage for the given chart
// from the bstats api
func GetUsage(pluginID int, chart string) (*types.PluginUsage, error) {
	var data Graph

	err := fetchBstats(pluginID, chart, &data)
	if err != nil {
		logrus.Error(err)
		return nil, err
	}

	var highestValue = 0
	for _, dataValue := range data {
		var value = dataValue[1]
		if value > highestValue {
			highestValue = value
		}
	}

	return &types.PluginUsage{
		Current: data[len(data)-1][1],
		Maximum: highestValue,
	}, nil
}

func fetchPlugin(pluginID int, result interface{}) error {
	req, err := http.NewRequest("GET", "https://bstats.org/api/v1/plugins/"+strconv.Itoa(pluginID), nil)
	if err != nil {
		return err
	}

	resp, err := client.Do(req)
	if err != nil {
		return err
	}

	defer resp.Body.Close()

	return utils.ParseResponse(resp, result)
}

func fetchBstats(pluginID int, chart string, result interface{}) error {
	req, err := http.NewRequest("GET", "https://bstats.org/api/v1/plugins/"+strconv.Itoa(pluginID)+"/charts/"+chart+"/data/?maxElements=4320", nil)
	if err != nil {
		return err
	}

	resp, err := client.Do(req)
	if err != nil {
		return err
	}

	defer resp.Body.Close()

	return utils.ParseResponse(resp, result)
}
