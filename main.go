package main

import (
	formatter "github.com/antonfisher/nested-logrus-formatter"
	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/updater/github"
	"github.com/sirupsen/logrus"
)

func main() {
	setupLogrus()

	env.Init()

	// updater.Schedule()

	// time.Sleep(2000)

	// byteArray, err := json.MarshalIndent(updater.Cache, "", "  ")
	// if err != nil {
	// 	logrus.Error(err)
	// }

	// logrus.Info(string(byteArray))

	plugins, err := github.GetPluginsInfo("Heroslender", []string{"HeroVender", "HeroSpawners"})
	if err != nil {
		logrus.Error(err)
		return
	}

	logrus.Infof("%#v", plugins)
}

func setupLogrus() {
	logrus.SetFormatter(&formatter.Formatter{
		HideKeys: true,
	})

	logrus.SetLevel(logrus.TraceLevel)
}
