package aoc

import (
    "net/http"
    "time"
)

const (
    BaseURL = "https://adventofcode.com/"
)

type Client struct {
    BaseURL    string
    apiKey     string
    HTTPClient *http.Client
}

func NewAPIClient(apiKey string) *APIClient {
	return &APIClient{
		BaseURL:    BaseURL,
		HTTPClient: &http.Client{Timeout: 10 * time.Second},
		APIKey:     apiKey,
	}
}

func (c *APIClient) GetStars(year, day string) (int, error) {
	c := config.C

	r, err := http.NewRequest("GET", c.BaseURL+year+"/", nil)
	if err != nil {
		return 0, err
	}
	r.AddCookie(&http.Cookie{Name: "session", Value: c.apiKey})

	resp, err := http.DefaultClient.Do(r)
	if err != nil {
		return 0, err
	}
	defer resp.Body.Close()

	data, err := io.ReadAll(resp.Body)
	if err != nil {
		return 0, err
	}

	if strings.Contains(string(data), "Day "+day+", two stars") {
		return 2, nil
	}

	if strings.Contains(string(data), "Day "+day+", one star") {
		return 1, nil
	}

	return 0, nil
}