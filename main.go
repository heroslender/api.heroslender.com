package main

import (
	formatter "github.com/antonfisher/nested-logrus-formatter"
	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/updater/bstats"
	"github.com/sirupsen/logrus"
)

func main() {
	setupLogrus()

	env.Init()

	pl, err := bstats.GetPluginInfo(5041)
	if err != nil {
		logrus.Error(err)
	}
	logrus.Infof("%#v", pl)
}

func setupLogrus() {
	logrus.SetFormatter(&formatter.Formatter{
		HideKeys: true,
	})

	logrus.SetLevel(logrus.TraceLevel)
}
