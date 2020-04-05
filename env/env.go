package env

import (
	"os"
	"strconv"
	"strings"

	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
)

var logger = logrus.WithField("context", "Env")

var GithubAPIKey, GithubUser string
var UpdateDelay int
var PluginsID []int

func Init() {
	logrus.Info("Loading .env file")
	err := godotenv.Load()
	if err != nil {
		logrus.Error("Error loading .env file")
	}

	GithubAPIKey = os.Getenv("GITHUB_API_KEY")
	if GithubAPIKey == "" {
		logger.Info("GITHUB_API_KEY is not defined!")
	}

	GithubUser = os.Getenv("GITHUB_USER")
	if GithubUser == "" {
		logger.Info("GITHUB_USER is not defined, using Heroslender as default!")
		GithubUser = "Heroslender"
	}

	delay := os.Getenv("UPDATE_DELAY")
	if delay == "" {
		logger.Info("UPDATE_DELAY is not defined, using 300 as default!")
		UpdateDelay = 300
	} else {
		if value, err := strconv.Atoi(delay); err != nil {
			logger.Errorf("The delay '%s' is not a valid number!", delay)
			logger.Error(err)
		} else {
			UpdateDelay = value
		}
	}

	plugins := os.Getenv("BSTATS_PLUGINS")
	for _, pluginID := range strings.Split(plugins, ",") {
		if id, err := strconv.Atoi(pluginID); err != nil {
			logger.Errorf("The id '%s' is not a valid number!", pluginID)
			logger.Error(err)
		} else {
			PluginsID = append(PluginsID, id)
		}
	}
}
