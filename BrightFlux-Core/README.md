# BrightFlux-Core

This is project will host the core code for data ingestion, data processing, data annotation and data serving.

## Idea

Provide some performant access to the logdata to analyze, using filters and selections. Because this is meant 
to be working in a readonly mode on data, I want to at least support some kind of project file. That helps to 
access and process the data in a desired way. I also want to create an index, and reformat the data and store 
this reformatted data partitioned. 

So this code is going to contain most of the data processing. Higher order usage of the core components will
be found in different projects (e.g. Framework and System).

## What this project is not right now.

I won't claim this being the most performant code... and honestly I don't care enough for raw performance 
right now. Maybe I will consider that later, if the project is still interesting enough for me or others.
I regard everything here in this project to be a POC.

I will implement a limited DataFrame library for this project. I definitely shouldn't do it, but anyway 
it's interesting to do such things. This DataFrame library can either evolve into a relational database
or might be extended in such a way, that it works large logs, such as logs which are Gigabytes in size.

But as for now speaking, this is tightly coupled and part of the log analysis project. Maybe it will be 
rewritten with a higher performance target in mind in some future.

* Maybe use the HDF5 format to load and store the column data of the dataframe (Problem full HDF5 stack is completely out of reach. Probably I can implement some very simple write routines. But the documentation and the HDF5 feature set is quite large. I will keep that out of scope for now. 
* Idea is to reuse all the already developed features and also a high performance reader and writer is appreciated
* The idea is also to be able to load this data (my dataframes) into python or some other tool for future quantitative analysis and machine learning
** https://github.com/HDFGroup/hdf5


## Ingest Data

The data to be ingested is first being tokenized. The tokenizer is specific to the file format (it can be a binary
or any other interesting / important file format). The tokenizer will produce a stream of tokens. The goal is a well 
formatted stream of tokens, which is the first abstraction of the file-content. The tokens are expected to follow a 
specific token syntax / token grammar so that the tokens can be parsed independently of what the input file actually 
is. 

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

### DataFrames internal DSL and external DSL
 
We started with an internal DSL to select and filter and compose the data frames.

Besides that internal DSL I started to implement a tokenizer, parser and compiler for something like a
Dataframe Query Language. The idea is to provide transform operations using a scriptable language. 

Currently supported operations are like these: 

* "select * from df" 
  - create a new dataframe from dataframe 'df' but keep all  rows
* "select 'col1', 'col2' from df" 
  - create a new dataframe with a sub-selection of columns 'col1' and 'col2' from dataframe 'df' but keep all rows
* "select 'col1' from df where (df.'col2'>1)" 
  - create a new dataframe with a sub-selection of columns  'col1' from dataframe 'df' but keep all rows, where the value of the column 'col2' is greater than 1;

The selection and the test predicate are compiled into a use of the following API calls:
 
* df.select(ColumnSelector/ColumnPredicate).where(RowSelector/RowPredicate);

To implement the comparisons more easily we use the Java-Comparator contract. We can implement gt, ge, lt, le, neq, eq 
by comparisons with the zero value and abstract from here.

String columns can now be selected using StringEvaluationRowFilterPredicates like 'contains', 'endswith' and 'startswith'
the equals contract works on String based columns as well as the gt, ge, lt and le contract. (But without locale support)

We support logical combinations of row filter predicates 'and', 'or', 'eq', 'neq'(xor), also we can negate a rowFilterPredicate.

TODO: 
* The predicates for contains, endswith and startswith are currently not accessible through the external DSL 
* Also the unary not operator is not supported.

TODO:
* between
* in
* compare values of two columns
* string implementation ( matches, ignorecase-operations)
* nand, nor
  

TODO
* In case of exactly one predicate affecting exactly one column, a column-wise approach would be much more efficient to iterate over and to filter  

## Data Frames - I/O

We should be able to export the selected and filtered data, and put it into files which can be used by other
simple programs and decision-tree-programs. Since we have simplified the data input, we may now be able to 
formulate some simple rules for each analysis case.

## Stored Data-Frames Queries

The idea is, that if we have hundreds of Data Frame Queries, is that we can apply them all and evaluate the 
outcome and point to the best hypothesis.

## Machine Learning and/or Decision Trees

The filtered text should be process-able by a ML-transformer architecture, for analysis we can train on the
predictability and then find the outliers