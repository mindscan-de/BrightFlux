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

import de.mindscan.brightflux.dataframes.DataFrameFactory;
import de.mindscan.brightflux.dataframes.DataFrameImpl;

/**
 * 
 */
public class IngestHeartCsv {

    public DataFrameImpl loadCsvAsDataFrame( Path path ) {

//        // age, sex, cp, trtbps, chol, fbs, restecg, thalachh, exng, oldpeak (float), slp, caa, thall, output
        DataFrameImpl dataframe = DataFrameFactory.create( path.getFileName().toString(),
                        new String[] { "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh", "exng", "oldpeak", "slp", "cas", "thall", "output" },
                        // TODO: input format (currently 'int' and 'float' only)
                        // we want to say something like int32b (int 32 binary) , int32t (int 32 text)
                        // column format should be negotiable (currently 'int' and 'float' only)
                        new String[] { "int", "int", "int", "int", "int", "int", "int", "int", "int", "float", "int", "int", "int", "int" } );

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

        return dataframe;
    }
}
