package env

import (
	"os"

	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
)

var logger = logrus.WithField("context", "Env")

var GithubAPIKey, GithubUser string

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
}
