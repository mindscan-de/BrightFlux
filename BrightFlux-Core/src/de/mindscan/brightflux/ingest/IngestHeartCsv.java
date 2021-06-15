/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.brightflux.ingest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompiler;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.parser.DataFrameParser;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.pipeline.IngestPipelineConfiguration;
import de.mindscan.brightflux.ingest.tokenizers.CSVTokenizerImpl;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizer;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;

/**
 * Use one of these datasets, your column names may vary...
 * 
 * https://www.kaggle.com/ronitf/heart-disease-uci
 * https://www.kaggle.com/zhaoyingzhu/heartcsv
 */
public class IngestHeartCsv {

    public DataFrameImpl loadCsvAsDataFrame( Path path ) {

//        // age, sex, cp, trtbps, chol, fbs, restecg, thalachh, exng, oldpeak (float), slp, caa, thall, output
        DataFrameBuilder dfBuilder = new DataFrameBuilder().addName( path.getFileName().toString() );

        // basically we have a line parser
        // basically we have a columnparser (will go to next column)
        // basically we have a data parser (dependent on the current column)

        String head[] = { "age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,output", //
                        "63,1,3,145,233,1,0,150,0,2.3,0,0,1,1", "37,1,2,130,250,0,1,187,0,3.5,0,0,2,1" };
        // read some lines of the dataset
        String columnSeparator = IngestUtils.calculateColumnSeparator( head );

        // that value will also determine, how many columns we have per line + 1 (so last element per line collects complex texts?
        int numberOfColumns = IngestUtils.calculateNumberOfColumns( head, columnSeparator.charAt( 0 ) );

        // calculate the column names.
        // This is not the best way to handle this, because we can have string values containing the separator
        String[] columnNames = head[0].split( columnSeparator, numberOfColumns );

        // TODO: input format (currently 'int' and 'float' only)
        // * we want to say something like int32b (int 32 binary) , int32t (int 32 text)
        // * column format should be negotiable (currently 'int' and 'float' only)
        // * we want to describe the input format and the input format parsing.
        dfBuilder.addColumns( columnNames,
                        // I currently have no idea how to determine this in a fast way
                        // I will let that sink in for a while
                        // that is the target type of the column - but we also need the input type of the columns as well
                        new String[] { "int", "int", "int", "int", "int", "int", "int", "int", "int", "float", "int", "int", "int", "int" } );

        // This is part of the Ingest configuration.
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();
        tokenizer.setColumnSeparator( columnSeparator );
        tokenizer.setLineSeparator( "\n" );
        tokenizer.tokenize( "" );

        // then we init the dataparser with the stop symbol until we see a columnseparator
        // then we init the columndataparser with the columnseparator,
        // then we init the columndataparser with the lineseparator as a stop symbol
        // then we init the last columndataparser with the stopymbol for the line
        // then we init the line separator with either "\n\r", "\r\n", "\n"

//        int intElement = 0;
//        float floatElement = 0;

        return dfBuilder.build();
    }

    public DataFrameImpl loadCsvAsDataFrameV2( Path path ) {

        List<DataFrameColumn<DataToken>> parsedDataFrameColumns = null;
        List<DataFrameColumn<?>> compiledDataFrameColumns = null;

        // path and such should be part of the Ingest pipeline configuration
        String inputString = readAllLinesFromFile( path );

        // building the configuration for the data to ingest
        IngestPipelineConfiguration data = new IngestPipelineConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );
        data.setTokenizerConfiguration( "CSVTokenizer" );

        // prepare pipeline
        DataTokenizer tokenizer = data.tokenizerFactoryInstance.buildTokenizerInstance( data.getTokenizerConfiguration() );
        DataFrameParser dfParser = data.parserFactoryInstance.buildDataFrameParserInstance();
        DataFrameCompiler dfCompiler = data.compilerFactoryInstance.buildDataFrameCompilerInstance();

        // run pipeline
        List<DataToken> tokens = tokenizer.tokenize( inputString );
        parsedDataFrameColumns = dfParser.parse( tokens );
        compiledDataFrameColumns = dfCompiler.compileDataFrameColumns( parsedDataFrameColumns );

        // build dataframe
        DataFrameBuilder dfBuilder = new DataFrameBuilder().addName( path.getFileName().toString() );

        // compiled dataframe columns
        if (compiledDataFrameColumns != null) {
            dfBuilder.addColumns( compiledDataFrameColumns );
        }

        // abstract dataframe columns
//        else if (parsedDataFrameColumns != null) {
//            for (DataFrameColumn<?> dataFrameColumn : parsedDataFrameColumns) {
//                dfBuilder.addColumn( dataFrameColumn );
//            }
//        }

        return dfBuilder.build();
    }

    private String readAllLinesFromFile( Path path ) {
        try {
            List<String> allLines = Files.readAllLines( path );
            return String.join( "\n", allLines );
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

}
