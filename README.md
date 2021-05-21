# BrightFlux -- Log File Viewing and Log File Analysis for Humans

LogFileViewer with yet unseen Features and written in Java. Why Java -- it is an uneducated guess
at this moment in time, I just want to see it work. The solution can be reworked whenever needed.

I'm not trying to recreate one of the many monitoring tools (e.g ELK-Stacks) -- if you are looking
for this, this is not the right place for you.

## Motivation

I'm sick of all the f..king log file viewers out there, because they lack the features and the usability from
different perspectives which serve the software developers, software-engineers and system-engineers point of
view. For some reason software developers forgot to serve themselves. 

Since some time I'm often analyzing bugs and issues using log files (think of post mortem analysis). Think
of the kind of errors where it must be determined what happened across many domains and across the system.
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
