package main

import (
	formatter "github.com/antonfisher/nested-logrus-formatter"
	"github.com/heroslender/api.heroslender.com/updater/github"
	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
)

func main() {
	setupLogrus()

	logrus.Info("Loading .env file")
	err := godotenv.Load()
	if err != nil {
		logrus.Error("Error loading .env file")
	}

	logrus.Info("Fetching github user Heroslender")
	user, err := github.GetUserInfo("Heroslender")
	if err != nil {
		logrus.Error(err)
	}
	logrus.Infof("%#v", user)
}

func setupLogrus() {
	logrus.SetFormatter(&formatter.Formatter{
		HideKeys: true,
	})

	logrus.SetLevel(logrus.TraceLevel)
}
