# BrightFlux-Core

This is project will host the code for data ingestion, data processing, data annotation and data serving.

## Idea

Provide some performant? access to the logdata to analyze, using filters and pipes. Since this is meant 
to be working in readonly mode on the data I might want to at least support some kind of project file, 
which helps to access the data in a desired way. I also want to create an index, and reformat the data 
and store this reformatted data partitioned. So this code contains the data process 

## What this project is not right now.

I won't claim this to be the most performant code... and i don't care enough for raw performance right
now. This particular project will probably not host the code for viewing the data, because presenting 
data is secondary for me at the moment.

I will implement a limited DataFrame library for this project. I definitely shouldn't do it but anyway 
it's interesting to do such things. This DataFrame library can either evolve into a relational database
or maybe will extended in such a way to work with very large logs, such as logs which are Gigabytes in
size. But as for now speaking, this is tightly coupled and part of the log analysis project. Maybe it 
will be rewritten with a higher performance in mind.    

* Maybe use the HDF5 format to load and store the column data of the dataframe (Problem full HDF5 stack is completely out of reach. Probably I can implement some very simple write routines. But the documentation and the HDF5 feature set is quite large. I will keep that out of scope for now. 
* Idea is to reuse all the already developed features and also a high performance reader and writer is appreciated
* The idea is also to be able to load this data (my dataframes) into python or some other tool for future quantitative analysis and machine learning
** https://github.com/HDFGroup/hdf5


## Ingest Data

The data to be ingested will first be tokenized. The tokenizer is specific to the file format (it can be a binary
or any other interesting / important file format. The tokenizer will produce a stream of tokens. The goal is a well 
formatted stream of tokens, which is the first abstraction of the file-content. The tokens are expected to follow a 
specific token syntax so that the tokens can be parsed independently of what the input file actually was. 

This specialized tokenizers help to keep the file format specific details out of the implementation and act as an 
anti-corruption-layer.

These tokens will be passed to a parser (DataFrameParser), that will convert the stream of tokens into a column-
oriented table - still containing tokens, but rearranged, or transformed, or merged... Also some simple checks
in the tokens will be implemented, whether the data frames have the correct size, and such, whether the data is 
complete (e.g. missing tokens between two ColumnSeparators). A Stream of tokens can contain multiple dataframes
and can switch between them or create new data frames, whenever needed. e.g. different logfiles for same issue.
The responsibility of the DataFrameParser is, to keep track of where the tokens belong and to add them to the 
correct positions in the data frame. 

After re-arranging and reordering the data frames the result is an AbstractDataFrame or merely a list of DataFrames.

This results in an AbstractDataFrame-Directory, which then is compiled to the fully typed DataFrame. A fully typed 
data frame can then be saved to disk or used for a Log-Data-Analysis. A fully typed data frame can only be reached 
by converting the values of the value tokens into the appropriate types of the column using some data parsers, where 
the tokenizer or the parser, was unable to infer the data types for the columns. Maybe the The target columntype was 
already be inferred by the parser, with some type annotations given to the parser.
 
Type interference is part of the compilation process from an abstract data frame into a fully typed data frame.
These steps can be implemented independently and can be tested on each edge of each data transformation process.

An option is to have different Parsers, which create an abstract data frame, with different capabilities. But in 
the next step we might have a single compiler which compiles the abstract data frame to a fully typed dataframe. 

## Data Frames

The ability to successful perform log analysis lies in the ability to select and filter log data e.g. by a DSL.

### DataFrames internal DSL vs. external DSL
 
We will start with an internal DSL to select and filter and compose the data frames.

We want to use something like this, what we know from operations on data (like SQL-Queries / QD (DataQuery))
* "select * from df;" 
  - create a new dataframe from dataframe 'df' but keep all  rows
* "select 'col1', 'col2' from df;" 
  - create a new dataframe with a sub-selection of columns 'col1' and 'col2' from dataframe 'df' but keep all rows
* "select 'col1' from df where df.col2>1;" 
  - create a new dataframe with a sub-selection of columns  'col1' from dataframe 'df' but keep all rows, where the value of the column 'col2' is greater than 1;
 
In the implementation as an internal DSL it could look like this:
* df.select(ColumnSelector/ColumnPredicate).where(RowSelector/RowPredicate);
* in reality the row selection comes first and after the row selection we select according to the columns, so each "where"-operation triggers the column selection and create a new dataframe

According to the Java-Comparator contract we can implement gt, ge, lt, le, neq, eq by comparisons with the zero 
value and abstract from here.

* TODO:
  - between
  - in
  - compare values of two columns
  - string implementation (contains, startswith, endswith, matches, ignorecase)
  - string also needs (eq, neq)
  - string may need (gt, ge, lt, le) ?

* TODO:
  - logical operations between two row filter predicates
  - and, or, nand, nor, eq, neq("xor"),
  - unary logical operation (not)
  
* In case of exactly one Predicate a column wise approach would be much more efficient.  


## Data Frames - I/O

We should be able to export the selected and filtered data, and put it into files which can be used by other
simple programs and decision-tree-programs. Since we have simplified the data input, we may now be able to 
formulate some simple rules for each analysis case.

## Stored Data-Frames Queries

The idea is, that if we have hundreds of Data Frame Queries, is that we can apply them all and evaluate the 
outcome and point to the best hypothesis.

## Machine Learning and/or Decision Trees

The filtered text should be process-able by a ML-transformer architecture, for analysis we can train 