# BrightFlux-Viewer

This project implements a simple Logfile-Viewer. Keep in mind that this is just a spare time
side project of mine.

## Idea

The idea is basically to have a simple GUI, where the log files can be viewed and analyzed.
I wasn't eager to start the GUI this early. On one side, I really like the end-to-end
experience. A big advantage of it is, that each day the application can do something that it 
couldn't do the day before and the progress becomes more and more visible. On the other side 
this also means, that the project will look very unsatisfying for a long time, because it is 
not polished. 

Anyway, because I want to see the content of the data frames and I don't want to stick to write 
them back to text files (what I obviously could do), I decided to start with the simplest application 
possible - to present a single data frame only and then work from there.

I will probably even delete this project and restart from scratch if I'm not satisfied with the 
outcome, but for now, I prefer the end-to-end experience over usability. An end-to-end approach 
also helps to focus and develop the right and smallest feature for the next improvement. Things 
can be refactored and improved as we go. Also, because I don't intend to keep that GUI application 
forever, I made the decision to not put any complex stuff related to the data processing into 
the GUI. That means hopefully most of the functionality will be decoupled enough to not cause
issues, when I will delete this GUI.  
  
Also I'm not a GUI-guy, therefore do not expect me to do something fancy right now. Maybe later.

## MVP

That all said, this "Viewer" should be able to load, ingest and present exactly one file 
"heart.csv". This particular stage is reached at the moment; we can load/ingest and also 
present multiple other files as well, by using a non-public reader for some binary logfiles. 
Loading and presenting such things is just a proof-of-concept at this stage.  

## Post MVP

Nothing special, just make it slightly better every time a feature is added, removed or changed.
Yes, sometimes it is better to remove features, e.g. saving files from data frame view. This is 
currently important, but will not stay here forever. 