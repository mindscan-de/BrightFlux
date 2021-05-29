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
import java.nio.file.Paths;

import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.dataframes.columns.FloatColumn;
import de.mindscan.brightflux.dataframes.columns.IntegerColumn;

/**
 * 
 */
public class IngestHeartCsv {
    public DataFrameImpl loadCsvAsDataFrame() {
        Path path = Paths.get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );

        DataFrameImpl dataframe = new DataFrameImpl( path.getFileName().toString() );

        // age, sex, cp, trtbps, chol, fbs, restecg, thalachh, exng, oldpeak (float), slp, caa, thall, output

        IntegerColumn ageColumn = new IntegerColumn( "age" );
        IntegerColumn sexColumn = new IntegerColumn( "sex" );
        IntegerColumn cpColumn = new IntegerColumn( "cp" );
        IntegerColumn trtbpsColumn = new IntegerColumn( "trtbps" );
        IntegerColumn cholColumn = new IntegerColumn( "chol" );
        IntegerColumn fbsColumn = new IntegerColumn( "fbs" );
        IntegerColumn restecgColumn = new IntegerColumn( "restecg" );
        IntegerColumn thalachhColumn = new IntegerColumn( "thalachh" );
        IntegerColumn exngColumn = new IntegerColumn( "exng" );
        FloatColumn oldpeakColumn = new FloatColumn( "oldpeak" );
        IntegerColumn slpColumn = new IntegerColumn( "slp" );
        IntegerColumn caaColumn = new IntegerColumn( "cas" );
        IntegerColumn thallColumn = new IntegerColumn( "thall" );
        IntegerColumn outputColumn = new IntegerColumn( "output" );

        int intElement = 0;
        float floatElement = 0;

        // add values to the columns
        {
            ageColumn.append( intElement );
            sexColumn.append( intElement );
            cpColumn.append( intElement );
            trtbpsColumn.append( intElement );
            cholColumn.append( intElement );
            fbsColumn.append( intElement );
            restecgColumn.append( intElement );
            thalachhColumn.append( intElement );
            exngColumn.append( intElement );
            oldpeakColumn.append( floatElement );
            slpColumn.append( intElement );
            caaColumn.append( intElement );
            thallColumn.append( intElement );
            outputColumn.append( intElement );
        }

        // input columns
        dataframe.addColumns( ageColumn, sexColumn, cpColumn, trtbpsColumn, cholColumn, fbsColumn, restecgColumn );
        dataframe.addColumns( thalachhColumn, exngColumn, oldpeakColumn, slpColumn, caaColumn, thallColumn );

        // output columns
        dataframe.addColumn( outputColumn );

        return dataframe;
    }
}
