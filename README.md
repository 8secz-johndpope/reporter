# reporter

[powered by NewsAPI.org](https://newsapi.org/)

## What is Reporter?
Reporter is an Android application used to browse news articles fetched from NewsAPI.org

## Features

**Note:** for testing I used Samsung Galaxy S9+ API 28 as phyisical device, Pixel 2 API 28 and Nexus 4 API 23 as emulators, API level 23 is minimum and API level 28 is target

### Defaults
- you can browse 'Top headlines' and 'Everything' endpoints 
- top headlines are all in English 
- default keyword for everything is 'tech'

### Search
- search bar searches through 'Everything'
  - when a search query is submitted, new articles are fetched using the search query as keyword
  - until the query is submitted, the entered text is dynamically used to search through previously fetched articles

### Displaying
- single click on an article opens a dialog displaying article title and article description
- long click on an article opens the full article in a browser

### Scroll gestures
- when an article list is pulled down from the top, the first page for default search values is fetched and merged with the existing list
- when the bottom of an article list is reached, next page with the default search values is fetched and added to the list

## Setting up
- once you have cloned the project from GitHub, you need to set up your API key
- in order to acquire your API key, go to [newsapi.org](https://newsapi.org/) and follow instructions
- once you have acquired your API key, create a file at `hr/codable/reporter/APIKey.kt` with following contents: 
```
package hr.codable.reporter

object APIKey {

    const val apiKey = "YOUR API KEY"
}
```
- run
