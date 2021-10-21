# BrightFlux-Framework

The framework project includes all the support code, which can be used for different UI and non-UI projects as well.

# Command-Processing

# Event-Processing

# Registry

# Intent-Processing

I would like to implement an Intents mechanism to somehow replace Events and Commands. This event and command queue 
solution as of now has its limits. I want the components to provide their own intents and capabilities, instead of 
sending an event, and the component answering this event. I want to say, listen all, i want this job to be done, or
i have this request, and then this component answers this thread instead of answers via broadcast. This would make
many parts of the UI loosely coupled.    