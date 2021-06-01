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

        // TODO: input format (currently 'int' and 'float' only)
        // * we want to say something like int32b (int 32 binary) , int32t (int 32 text)
        // * column format should be negotiable (currently 'int' and 'float' only)
        // * we want to describe the input format and the input format parsing.
        dfBuilder.addColumns(
                        new String[] { "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh", "exng", "oldpeak", "slp", "cas", "thall", "output" },
                        new String[] { "int", "int", "int", "int", "int", "int", "int", "int", "int", "float", "int", "int", "int", "int" } );

        // basically we have a line parser
        // basically we have a columnparser (will go to next column)
        // basically we have a data parser (dependent on the current column)

        String head[] = { "age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,output", //
                        "63,1,3,145,233,1,0,150,0,2.3,0,0,1,1", "37,1,2,130,250,0,1,187,0,3.5,0,0,2,1" };
        // read some lines of the dataset
        String columnSeparator = IngestUtils.calculateColumnSeparator( head );

        // that value will also determine, how many columns we have per line + 1 (so last element per line collects complex texts?

        // then we init the dataparser with the stop symbol until we see a columnseparator
        // then we init the columndataparser with the columnseparator,
        // then we init the columndataparser with the lineseparator as a stop symbol
        // then we init the last columndataparser with the stopymbol for the line
        // then we init the line separator with either "\n\r", "\r\n", "\n"

//        int intElement = 0;
//        float floatElement = 0;

        // add values to the columns
        {

//            ageColumn.append( intElement );
//            sexColumn.append( intElement );
//            cpColumn.append( intElement );
//            trtbpsColumn.append( intElement );
//            cholColumn.append( intElement );
//            fbsColumn.append( intElement );
//            restecgColumn.append( intElement );
//            thalachhColumn.append( intElement );
//            exngColumn.append( intElement );
//            oldpeakColumn.append( floatElement );
//            slpColumn.append( intElement );
//            caaColumn.append( intElement );
//            thallColumn.append( intElement );
//            outputColumn.append( intElement );
        }

        return dfBuilder.build();
    }
}
