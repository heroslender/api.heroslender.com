package utils

import (
	"encoding/json"
	"io/ioutil"
	"net/http"
)

// ParseResponse parses the http response json body to the given interface
func ParseResponse(resp *http.Response, result interface{}) error {
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}

	json.Unmarshal(body, result)
	return nil
}
