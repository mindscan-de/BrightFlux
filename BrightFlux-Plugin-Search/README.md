# BrightFlux-Plugin-Search

## Basic Idea

Provide multiple ways to search for the log messages. The search results are delivered as a search result
dataframe. Which provide links which can be followed to view the source.

## MVP

Provide access to search results of project FuriousIron-SearchBackend. 

* I want to select a row in the log and then state to search in FuriousIron project
* I want to use multiple configurable profiles, which will append the search automatically when a certain search profile is chosen/selected and a search is performed e.g. Profile "JavaImplementation" may add "language:java -unittest:true -fileext:txt"
* The search result is a list of documents matching the search
* a double click on a row in the search will retrieve the cached file and present it to the user