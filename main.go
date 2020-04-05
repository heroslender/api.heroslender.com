package main

import (
	"time"

	formatter "github.com/antonfisher/nested-logrus-formatter"
	"github.com/heroslender/api.heroslender.com/env"
	"github.com/heroslender/api.heroslender.com/updater"
	"github.com/sirupsen/logrus"
)

func main() {
	setupLogrus()

	env.Init()

	updater.Schedule()
	
	time.Sleep(2000)
	logrus.Infof("%#v", updater.Cache)
}

func setupLogrus() {
	logrus.SetFormatter(&formatter.Formatter{
		HideKeys: true,
	})

	logrus.SetLevel(logrus.TraceLevel)
}
