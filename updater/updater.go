package updater

import (
	"time"

	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/types"
	"github.com/heroslender/api.heroslender.com/updater/bstats"
	"github.com/heroslender/api.heroslender.com/updater/github"
	"github.com/sirupsen/logrus"
)

var Cache types.CachedData = types.CachedData{}
var logger = logrus.WithField("context", "Updater")

// Schedule a timer to update te data every x time
func Schedule() {
	Update()
	
	ticker := time.NewTicker(time.Duration(env.UpdateDelay) * time.Second)
	go func() {
		for {
			<-ticker.C
			Update()
		}
	}()
}

func Update() {
	logrus.Debug("Running update")
	github, err := github.GetUserInfo(env.GithubUser)
	if err != nil {
		logger.Error("Failed to fetch the github user data for " + env.GithubUser)
		logger.Error(err)
	}
	Cache.GitHub = *github

	UpdatePlugins()
}

func UpdatePlugins() {
	if len(Cache.Plugins) != len(env.PluginsID) {
		// First time running, fill the plugins
		Cache.Plugins = make([]types.Plugin, 0)

		for _, pluginID := range env.PluginsID {
			plugin, err := bstats.GetPluginInfo(pluginID)
			if err != nil {
				logger.Error("Failed to fetch github user data for " + env.GithubUser)
				logger.Error(err)
			} else {
				Cache.Plugins = append(Cache.Plugins, *plugin)
			}
		}

		return
	}

	for _, plugin := range Cache.Plugins {
		if server, err := bstats.GetUsage(plugin.ID, "servers"); err != nil {
			logger.Error("Failed to fetch bstats plugin servers for " + plugin.Name)
			logger.Error(err)
		} else {
			plugin.Servers = *server
		}

		if server, err := bstats.GetUsage(plugin.ID, "players"); err != nil {
			logger.Error("Failed to fetch bstats plugin players for " + plugin.Name)
			logger.Error(err)
		} else {
			plugin.Players = *server
		}
	}
}
