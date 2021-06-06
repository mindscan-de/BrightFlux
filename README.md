# BrightFlux -- Log File Viewing and Log File Analysis for Humans

LogFileViewer with yet unseen Features and written in Java. Why Java -- it is an uneducated guess
at this moment in time, I just want to see it work. The solution can be reworked whenever needed.

I'm not trying to recreate one of the many monitoring tools (e.g ELK-Stacks) -- if you are looking
for this, this is not the right place for you.

## Motivation

I'm sick of all the f..king log file viewers out there, because they lack the features and the usability from
different perspectives which serve the software developers, software-engineers and system-engineers point of
view. For some reason software developers have forgot to serve themselves and their future selves.

Since some time I'm often analyzing bugs and issues using log files (think of post mortem analysis). Think
of the kind of errors, where it must be determined what happened across many domains and across the system.
This data is usually acquired in the field and usually isn't reproducible on the developer system, because
these problems are those, where dozens or hundreds of components are integrated. This means there are tens
of thousands of software modules.

Logs for post mortem analysis are usually very large, because the issue might be so rare and hard to reproduce,
that all available logs and traces are written and even then, you might understand the issue only when you see
the other interfaces, because you couldn't log this particular thing in your part of the software, instead you
must correlate a particular log item from a external code base (or second grade log) to your own code as well.

Therefore reading multiple and different logs and correlate them (according to time and internal state) is an 
important part of the whole log analysis system.

Most of these analysis jobs are a one time scenario or will only be repeated until the underlying issue
is resolved -- this might also be the reason why such tools probably aren't developed. Every time there
will be a good enough solution, which gets the job somehow done. No need for a tool here. So everybody
develops similar solutions and all the things are reinvented -- all the time and built into other tools
as well. Text based logs have one important shortcomming -- they can be shown in a text editor. Editors
are good for editing - but they often aren't the best thing if you work basically readonly on the files.

## Planned Features

Some of the planned features are: 

* Build a row and column dataset from the Logmessages
* Treat logs like data frames and use transformations on columns
  * save the transformations and dataframe configuration to an analysis project file
* Create Sequence-Diagrams from Logmessages / log-dataframes
* Create Activity-Diagrams from Logmessages / log-dataframes
* Have Swimlanes for different Components
  * save swimplane configuration into an analysis project file
* Annonate Logmessages
  * save the annotations to an analysis project file
  * use annotations for the analysis writeup
* Filter Rows (Logmessage lines) and Columns in log-dataframes
* have multiple views / diagram options for the same logs to understand particular system behavior
* Export the analysis result in short form
* Help with writing up the analysis, eg. using templates
* Export datarames as h5-files (HDF5), so it can be used in multiple ways (e.g. some proof of concept works as well as machine learning)
* Using receipts for reproducing some analysis with a second log
  * using labelling techniques to mark suspicious occurences and make them available for machine learning training
  * like this is "ok", this is "suspicious", this indicates a "problem" in case we see a particular message when some value in the message is bigger than a threashold
  * NER for logmessages
* some dictionary or knowledge base articles for certain messages to provide context
* add links and options to use a code search engine and log search engine for the messages
  * did we see that message more often, but we never thought about it before
  * since what version of the system we see that message
  * where does this message come from in the code and annotate this message with a software component / swimlane

* Support log correlation and videos, e.g. identify timestamps in video and correlate them to logs
* identify same events for different log sources (e.g. in case they are connected) and correlate logs to each other using very different log formats.
* provide measuring tools (e.g. time difference between two or more messages) e.g. performance measurements

* indexing dataframes columnwise for future search operations
* Decoders for inner structures again into dataframes - e.g. Zooming into the dataframes / e.g. Level of detail and then study a single aspect across the whole log

* Ingest text files into table oriented dataframes
  * use json file based - format and parser description 
* ingest binary files into table oriented dataframes
  * use json file based - format and parser description 
* ingest inner encodings of data in certain columns of the database in case, using selectors (SQL-Like) and then a format/parser description
  
* use a client server approach for the dataframe processing vs presentation
