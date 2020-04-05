package main

import (
	formatter "github.com/antonfisher/nested-logrus-formatter"
	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/updater/github"
	"github.com/sirupsen/logrus"
)

func init() {
	logrus.Info("Main")
}

func main() {
	setupLogrus()

	env.Init()

	logrus.Info("Fetching github user " + env.GithubUser)
	user, err := github.GetUserInfo(env.GithubUser)
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
