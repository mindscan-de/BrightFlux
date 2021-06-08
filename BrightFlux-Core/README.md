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


## Ingest Data

The data to be ingested will be tokenized first. The tokenizer is specific to the file format (it can be a binary or
any other interesting/important format. The tokenizer will produce a stream of tokens. The goal is a well formatted 
stream of tokens, which is the first abstraction of the file-content. The tokens are expected to follow a specific
token syntax so that they can be parsed independently what the input file actually was. 

This specific tokenizers help to keep the file format specific details out of our implementation and acts as an 
anti-corruption-layer.

These tokens will then be passed to a parser (DataFrameParser), which will convert the stream of tokens into a 
column-oriented table, still containing tokens, but rearranged, or transformed, or merged... Also some simple checks
in the tokens will be implemented, whether the data frames have the correct size, and such, whether the data is 
complete (e.g. missing tokens between two ColumnSeparators). A Stream of tokens can contain multiple dataframes
and can switch between them or create new data frames, whenever needed. e.g. different logfiles for same issue.
The responsibility of the parser is to keep track of where the tokens belong to, and to add them to the correct
positions in the data frame. 

After re-arranging and reordering the data frames the result is an AbstractDataFrame.

This will result in an AbstractDataFrame-Directory, which then is compiled to the fully typed DataFrame. A fully 
typed data frame can then be saved to disk or used for the Log-Data-Analysis process. A fully typed data frame can
only be reached by converting the values if the value tokens into the appropriate types of the column using some
data parsers, where the tokenizer or the parser, was unable to infer the data types for the columns. Maybe the
The target columntype was already be inferred by the parser, with some type annotations given to the parser.
 
Type interference is part of the compilation process from an abstract data frame into a fully typed data frame.
These steps can be implemented independently and can be tested on each edge of each data transformation process.
