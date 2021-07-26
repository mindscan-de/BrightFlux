# BrightFlux-Viewer

This project will implement the Logfile-Viewer. Keep in mind that this is just a spare time
side project of mine.

## Idea

The Idea is basically to have a simple GUI, where the log files can be viewed and analyzed. I
didn't meant to start the GUI that early, but I really like the end-to-end experience. The big
advantage of the end-to-end experience is, that each day the application can do something that
it couldn't do the day before and the progress becomes visible. On the other side this also 
means, that the project will look very unsatisfying for a long time, because it is not polished. 

Anyway, because I want to see the content of the data frames and I don't want to stick to write 
them back to text files (what I obviously could do), I decided to start with the simplest application 
possible - to present a single data frame only and then work from here.

I will probably even delete it and restart from scratch if I'm not satisfied with the outcome,
but for now I prefer the end-to-end experience over the usability. The end-to-end approach also
helps to focus and develop the right and smallest feature for the next improvement. Things can
be refactored and improved as we go. Also because I don't intend to keep that GUI application 
forever, I hopefully will be able to decouple the functions from the presentation. 
  
Also I'm not a GUI-guy, therefore do not expect me to do something fancy right now. Maybe later.

## MVP

That all said, this "Viewer" should be able to load, ingest and present exactly one file 
"heart.csv". This stage is reached for now. We can load ingest and also present multiple
other files as well, as well as a non-public reader for some binary logfiles. Loading
and presenting such things as well is a proof-of-concept at this stage.  

## Post MVP

I will add the smallest increment each time, which provides me some benefit.