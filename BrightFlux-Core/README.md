# BrightFlux-Core

This is project will host the code for data ingestion, data processing, data annotation and data serving.

## Idea

Provide some performant? access to the logdata to analyze, using filters and pipes. Since This is meant 
to be working in readonly mode on the data I might want to at least support some kind of project file, 
which helps to access the data in a desired way. I also want to create an index, and reformat the data 
and store this reformatted data partitioned. So this code contains the data process 

## What this project is not right now.

I won't claim this to be the most performant code... and i don't care enough for raw
It will probably not host the code for viewing the data, as this is secondary for me at the moment.


* Maybe use an existing DataFrame library so far, until we have reason to use our own?
** https://github.com/nRo/DataFrame

* Maybe use the HDF5 format to load and store the column data of the dataframe
* Idea is to reuse all the alredy developed features and also a high performance reader and writer is appreciated
* The idea is also to be able to load this data (my dataframes) into python or some other tool for future quantitative analysis and machine learning
** https://github.com/HDFGroup/hdf5