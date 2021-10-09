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
package de.mindscan.brightflux.dataframes.dfquery.runtime;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;

/**
 *
 * This typed node represents a column of a dataframe. This representation is used to introduce a type system
 * in the compiler step of the data frame query language.
 * 
 */
public class TypedDFQLDataFrameColumnNode implements TypedDFQLNode {

    private TypedDFQLDataFrameNode dataFrame;
    private String columnName;

    /**
     * 
     */
    public TypedDFQLDataFrameColumnNode( TypedDFQLDataFrameNode dataFrameNode, String columnName ) {
        this.dataFrame = dataFrameNode;
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataFrame getDataFrame() {
        return dataFrame.getDataFrame();
    }

    public TypedDFQLDataFrameNode getDataFrameNode() {
        return dataFrame;
    }

    public boolean isValidColumn() {
        return dataFrame.getDataFrame().hasColumn( columnName );
    }

    public DataFrameColumn<?> getColumn() {
        return dataFrame.getDataFrame().getColumn( columnName );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeNodeOperation() {
        return this.dataFrame.describeNodeOperation() + ".'" + this.columnName + "'";
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeNodeOperationDebug() {
        return "(dataframecolumn:" + dataFrame.describeNodeOperationDebug() + "; (columnName:'" + columnName + "'))";
    }
}
