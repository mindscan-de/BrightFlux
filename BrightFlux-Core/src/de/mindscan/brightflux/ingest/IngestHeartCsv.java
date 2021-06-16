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

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;

/**
 * Use one of these datasets, your column names may vary...
 * 
 * https://www.kaggle.com/ronitf/heart-disease-uci
 * https://www.kaggle.com/zhaoyingzhu/heartcsv
 */
public class IngestHeartCsv {

    public DataFrameImpl loadCsvAsDataFrameV2( Path path ) {
        // building the configuration for the data to ingest
        JobConfiguration config = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );
        config.setTokenizerConfiguration( "CSVTokenizer" );
        config.setDataFrameName( path.getFileName().toString() );
        config.setIngestInputFilePath( path );

        return IngestEngine.execute( config );
    }

    // This was basically the starting point of the Dataframe compilation process, 
    // but some ideas are still there, which i want to keep and to move them into an appropriate place
    // actually this is deprecated code, but for now i will keep it until the ideas are moved to their new place
    DataFrameImpl loadCsvAsDataFrame( Path path ) {

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

        return dfBuilder.build();
    }

}
