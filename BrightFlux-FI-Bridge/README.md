# BrightFlux-FuriousIron-Bridge

The main idea for this project is to provide bridge code to access the FuriousIron code search engine.

The idea behind accessing a code search engine from a log analysis tool is, to be able to inspect the 
source code near to a given log message. Thus providing direct access to the source code at the time 
of analysis. This search should be available in the UI (BrightFlux-Viewer) as a configurable plugin.

Some possible future feature of the code search engine is to provide the shortest path between two log 
messages and create a sequence diagram from it. But this would require to rework the code search engine 
in a way to also 'understand' and index the AST structure of the indexed code.

## The current state

This project is currently not yet under active development, but a preparation in case I want this
search feature complete in a short time span, in that cas this project should be prepared. Maybe I 
will just implement the most obvious code over time. This code is also a proof-of-concept code to 
access my source code search engine using a java client.

I think for this i will reuse some of my previous code from the Fluent-Genesis-Plugins backend code
to access the search engine.