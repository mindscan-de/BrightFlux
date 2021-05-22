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
package de.mindscan.brightflux.dataframes;

import java.util.UUID;

/**
 * TODO: use some example code for a dataframe, e.g. write them as a unit test
 *       and then develop from there. Use pandas examples for reading some files
 *       and access some of the data - and then develop from here to a java specific 
 *       syntax.
 */
public class DataFrameImpl {

    private String nameOfDataFrame;
    private UUID uuidOfDataFrame;

    public DataFrameImpl( String nameOfDataFrame ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.randomUUID();
    }

    public DataFrameImpl( String nameOfDataFrame, UUID uuid ) {
        this.nameOfDataFrame = nameOfDataFrame;
        this.uuidOfDataFrame = UUID.fromString( uuid.toString() );
    }

    /**
     * @return the uuid of the data frame
     */
    public UUID getUuid() {
        return uuidOfDataFrame;
    }

    /**
     * @return the name Of DataFrame
     */
    public String getName() {
        return nameOfDataFrame;
    }

//    /**
//     * 
//     */
//    public class DTypes {
//
//    }
//
//    /**
//     * 
//     */
//    public class DataFrameIndex {
//
//    }
//
//    /**
//     * 
//     */
//    public class DataColumn {
//
//    }
//
//    /**
//     * return the column labels of the dataframe
//     * @return
//     */
//    public DataFrameIndex getColumns() {
//        return null;
//    }
//
//    /**
//     * return the types of the columns of the dataframe
//     * @return
//     */
//    public DTypes getDTypes() {
//        return null;
//    }
//
//    public void head() {
//
//    }
//
//    public void tail() {
//
//    }
//
//    public void to_csv() {
//        // to_excel
//        // to_clipboard
//        // to dict
//        // ....
//        // to 
//    }
//    // column-names
//    // columns
//    // rows
//
//    // how about calculated columns (eg. uncorrected time, synced time?)
//    // each dataframe has an implicit or explicit id-column (which can serve as a primary key)
//    // they can be numbered, but they can also be sparse integers (e.g. position of a start of a line in a file)
}
