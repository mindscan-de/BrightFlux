# BrightFlux -- Log File Viewing and Log File Analysis for Humans

LogFileViewer with yet unseen Features and written in Java. Why Java -- it is an uneducated guess
at this moment in time, I just want to see it work. The solution can be reworked whenever needed.

I'm not trying to recreate one of the many monitoring tools (e.g ELK-Stacks) -- if you are looking
for this, this is not the right place for you.

Also keep in mind, that this is a spare time side project for me, where I challenge myself to build
tools from scratch. I like to work on green field projects and to figure stuff out. Also the progress
will be on a need-by-need basis. I won't develop it, if I don't need it. I also won't fix things if it
ain't broken.

## Motivation

I'm sick of all the f..king "log file viewers" out there, because they lack the features and the usability from
different perspectives which serve the software developers, software-engineers and system-engineers point of
view. For some reason software developers have forgotten to serve themselves and their future selves.

Since some time I'm often analyzing bugs and issues using log files (think of post-mortem analysis). Think
of the kind of errors, where it must be determined what happened across many domains and across the system.
This data is usually acquired in the field and usually isn't reproducible on the developer system, because
these problems are those, where dozens or hundreds of components are integrated. This means there are tens
of thousands of software modules.

Logs for post-mortem analysis are usually very large, because the issue might be so rare and hard to reproduce,
that all available logs and traces are written and even then, you might understand the issue only when you see
the other interfaces, because you couldn't log this particular thing in your part of the software, instead you
must correlate a particular log item from an external code base (or second grade log) to your own code as well.

Therefore reading multiple and different logs and correlating them (according to time and internal state) is an 
important part of a unified log analysis system. Some of these "logs" can be videos, text files or binary logs.

Most of these analysis jobs are one time scenarios, or will only be repeated until the underlying issue
is resolved -- this might also be the reason why such tools probably aren't developed. Every time there
will be a good enough solution, which gets the job somehow done. No need for a tool here. So everybody
develops similar solutions and all the things are reinvented -- all the time and built into other tools
as well. Text based logs have one important shortcoming -- they can be shown in a text editor. Editors
are good for editing - but they often aren't the best thing if you work basically read-only on the files
and you want to take notes.

Because of the fact that most of the scenarios are one time jobs, means that such a system must be highly
flexible and must have low (initial) adaptation costs, because you can't develop a new tool or a completely
new strategy each time a new analysis is requested.

## Not fit for anything (YET)

I'm currently in the process figuring out, what can be implemented as soon as possible for some kind of
MVP. I think a very simple working pipeline from a simple '.csv' file to an annotated log analysis project 
file, would be nice to have.

Currently you would be totally underwhelmed by forking this project. There will also be no stable APIs
and anything and everything is subject to changes. The first solution will be a simple API to support
the ingestion and processing of log data, with the most simple viewer for this data. A desired solution
would be to have this as a client-server solution, where the heavy lifting of data processing is done
on some kind of server and a "thin" client to request data processing. But as for now the local machine
will do the data processing and also the presentation. What might put a hard limit on how much log data
can be viewed at a time.

In a client-server scenario, the amount of data which can and should be transferred might be limited as
well. Therefore we must think of very different strategies for accessing the data. A client-server scenario 
might be technically interesting, but should not be the driver for developing the core components. That 
would introduce much of the limitations of the UI and communication and then transfer these "solutions"
into the system and software design of the core components. Those things should be handled in a different
layer of abstraction. Doing such things too early will cause more harm then good, as of my understanding.

## So what's implemented right now?

* Proof of Concept to build a row and column dataset from csv (and some proprietary log) files
* Load a file (tokenize, parse and compile the data) - ingest a file as a data frame
* Filter Rows (log message lines) and Select Columns (parts of a log message line)
* Support recipes
  * save recipes - A data frame tracks the applied transformations, the applied transformations can be saved
  * apply recipes - data frame can be modified by selecting and applying a recipe
* A limited DataFrame Query Language is implemented
  * contains, startswith, endswith
  * eq, neq, gt, ge. lt, le
  * &&, ||
  * ROWCALLBACK -> do operation for each row matching the WHERE-CLAUSE
  * SELECT
  * NOT 
    - very simple mode only the top node can contain a single NOT statement yet
	- will be extended if i require it.
* A limited GUI is implemented
* Export the data frame as a CSV file
* Support for the original row index (in a derived frame)
  * "select *" will copy the original index
* Annotate log messages
  * annotate line of a data frame or one of its child data frames
  * annotations are global over different (related) data frames
  * create a simple report from the annotations
  * use report snippets for annotations
  * save current annotations to annotation file - this feature is just temporary
  * load current annotations from annotation file - this feature is just temporary
* Highlight log messages
  * colorize line of a data frame or one of its child/parent data frames 
  * save highlight file - this feature is just temporary and will be embedded into the log analysis project file
  * load highlight file - this feature is just temporary and will be embedded into the log analysis project file
  * colorization of a line of a data frame via DFQL
* Show data frame hierarchy for the current selected data frame
* Provide favorite recipes directly into context menu
* Copy current selected row to clipboard
* Annotate video Objects
  * "open a video" and annotate each second of a video using a slider to navigate in video time
  * generate a simple (hard-coded) report for all available video objects
  * use default texts, which can be used to annotate timestamps
  * save video annotations to disk
  * load video annotations from disk
  * sync video position from video slider to log position and locate nearest following time stamp in data frame  
* Annotate video Timestamps
  * link video time stamps to data frame time stamps / create a sync with video, by selecting a log entry  
  * save sync positions to video annotation files as extra reference data
  * load sync positions from video annotation files with reference data (sync also works after load)  
  

Also one note, just because the features are implemented somehow, it doesn't mean, that they are easy to use 
right now. Making things easy to use, comes at a cost, some parts must be reimplemented once or twice before
they become good enough. I decided to skip these re-writes and re-implementations until becoming absolutely 
necessary. And also, sometimes, if you give it some more time, you will come up with a better idea or even a
better solution. Therefore giving it some time to mature, saves a lot of effort in the long run.

This whole thing is going through a permanent dogfooding phase.


## So what's implemented but not included?

Some of the code is kept proprietary for multiple reasons: One is - I develop this as a very personal tool.
This tool doesn't have to be any better than this. Second is - Things are still not yet good enough for a 
proper abstraction.

* Non published - proof of concept - tokenize binary log file
* Non published - proof of concept - tokenize from data frame
  * DataSource tokenizer based on a data frame and a hard-coded predefined/selected column which is further tokenized
  * This can also copy content (column values) of the parent data frame to the new tokenized frame.
  * also correctly log DataFrameActions that a data frame was created by tokenization (track full Recipe)


## Planned Features

The "planned features" are not planned actually to be implemented, but they are more of a kind of ideas and put
into some personally reasonable order. Some of these ideas will be dropped some reordered, delayed or sometimes
also implemented. The most active topics will be the ones mentioned in the "Next" sub section, these are topics 
I want to address rather sooner than later.

### Unclassified

* This list is currently empty


### Next

DataFrame-Core
* [DFCORE] Support original Index
  * [DFCORE] copy the original index in a derived data frame 
* [DFCORE] Improve support of "__org_idx" and "__idx", when doing select statements (added to the default copied columns)
* [DFCORE] reindex "__idx" row on data frame filtering and data frame column selection


Startup and EarlyPersistence
* Implement an Early Persistence module, which can then be used to start the other components (used for initial configurations)


Startup
* Do a real application startup, such that the components are properly initialized - instead of "enabling" features.


UI/UX
* Configuration System
  * provide PreferenceStore
  * provide at least all the load operations for the preferences (Load is much more important than UI based editing / editing can still done by hand if important)


### Later

Log-Analysis-Case-File
* Introduce Project Files / "Case"
* Treat logs like data frames and use transformations on columns
  * [LACF] save the transformations and data frame configuration to an analysis case file
* [LACF] log message Annotations
  * [LACF] save annotations to an analysis case file
  * [LACF] load annotations from analysis case file for a file


Annotation and Highlights Improvements
* Have different annotation frames / highlight frames for each base document and combine via strategy...
* Support log correlation for videos, e.g. identify time stamps in video and correlate them to logs


Application-Startup (est. 6 kLOC)
* Provide a script or configuration for the application startup.


Reporting
* Help with writing up the analysis, eg. using special report templates (e.g. different formats JIRA, or else))
  * use of templates for creating a report
  * also detect different DataFrame types and use different report templates for different DataFrame types


UI/UX
* Configuration System
  * provide PreferencePages and Editors for different components (e.g. DFQL default queries, Report templates, Last Sizes, Last Files, Last Directories)  (later)
  

### Even Later

Swim lanes
* swim lanes simply are actually a select statement
* have swim lanes for different Components
  * save swim lane configuration into an analysis project file
  
  
DFQL
* Support tokenizing via DFQL
* Support recipes which contain tokenized data frames, such that recipes can be replayed and are creating a tokenized data frame


Decision Tree Analysis
* combine that system with the CheapLithium system (decision gag/decision tree) for automated decisions based on the content of log files for automated log file analysis


Search Engine Support
* combine that system with the FuriousIron code search system


Generic text File Readers
* The file format should be described in some configuration format and a generic file reader should tokenize/parse it according to the description in the file-description
  * Ingest text files into table oriented data frames
    * use .json file based (configured) - format and parser description 


Generic Binary File Readers
* The file format should be described in some configuration format and a generic file reader should tokenize/parse it according to the description in the file-description
  * ingest binary files into table oriented data frames
    * use json file based - format and parser description 


UML Image Generator
* Create Sequence-Diagrams from Log messages / log data frames
* Create Activity-Diagrams from Log messages / log data frames
* have multiple views / diagram options for the same logs to understand particular system behavior


Machine Learning and Pattern recognition and anomaly Detection on Log-Data 
* We need Column-Ingest processing to clean up the data / separate the data from meta information e.g. Filename, method name, line of implementation
* "Fraud detection" - Predict next line and evaluate, score how much the next line was/is expected... If not expected -> Found an anomaly
* Extract / Train / Predict / Classification / Model Update / ML Model Server
  * using labeling techniques to mark suspicious occurrences and make them available for machine learning training
  * like this is "ok", this is "suspicious", this indicates a "problem" in case we see a particular message when some value in the message is bigger than a threshold
* NER for log messages


Support Calculations between Rows or Columns
* provide measuring tools (e.g. time difference between two or more messages) e.g. performance measurements


Export the analysis result in short form
* Document / document type / technical vs manager form
* Project file


UI/UX (est. 1.5 kLOC)
* save width information by (column name list)-hash such that the width is preserved for future uses or derived frames.


UI/UX (est. 8-10 kLOC)
* provide left(show icons and boxes for annotated lines, direct synchronized windowed ruler ) and right ruler 
  (overview ruler which is showing the color bars and click-able fullscale positions)
 
  
Report Generator
* improve report generator with
  * templates
  * variables
  * conversion functions
  * blocks


Rebuild the Event-Pattern and Command-Pattern
* I think o be able to decouple all the code from each other (compile-wise) i want this system to transition to an Intent-based-System (such that menu entries are provided by visualization intents, requests, broadcast-requests, broadcast events, 1:1 communication using intents and futures, subscriptions and promises). And each plugin can state which intents it subscribes to. So that you can have the command factory and the intent to create a command in the plugin which provides the functionality. And the Buttons and such only fire intents and show menu items providing intents.


Video time stamp Annotations
* add Markers to annotation data frame (ATTN: requires rulers to make it more usable)
* load video also loads parallel video annotation file (UX? Later)
* show video frame content of the video at exactly a selected time stamp (FFMPEG / extract nearest Frame?)
* sync from data frame to current video and locate position. (MAYBE) 
* provide more accurate time stamps e.g. 0.1s 0.25s granularity (maybe on a frame number basis, i mean we know the number of frames, we know the frames per seconds, but the video player must be more precise then and show the frame numbers)


### Never?


Exporters
* Export data frames as h5-files (HDF5), so it can be used in multiple ways (e.g. some proof of concept works as well as machine learning)


Client Server / Windowed Data-Retrieval
* use a client server approach for the data frame processing vs presentation and maybe for performance


Internal Search Engine to search in data frame columns
* indexing data frames column wise for future search operations


* transform logs from one system (source) to the other - e.g. recovery mode if one of the systems stopped logging but the other system was receiving the logs as well and logged them
  * reconstruct logs
* identify same events for different log sources (e.g. in case they are connected) and correlate logs to each other using very different log formats.


* some dictionary or knowledge base articles for certain messages to provide context


* add links and options to use a code search engine and log search engine for the messages
  * did we see that message more often, but we never thought about it before
  * since what version of the system we see that message
  * where does this message come from in the code and annotate this message with a software component / swim-lane
