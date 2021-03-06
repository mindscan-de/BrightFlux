/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.dataframes.dfquery.compiler;

import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameTokenizerJob;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLTokenizerStatementNode;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 * This class implements a compiler from a TypedDFQLTokenizerStatementNode into a DataFrameTokenizerJob,
 * represented by an instance of DataFrameTokenizerJobImpl. This Job is meant to be executed by an ingest
 * engine to create a new data frame from a given data frame using a specialized tokenizer.
 *  
 */
public class DataFrameTokenizerJobCompileStrategy {

    public DataFrameTokenizerJob compile( DFQLNode node ) {
        if (node instanceof TypedDFQLTokenizerStatementNode) {
            return compile_TypedDFQLTokenizerStatementNode( (TypedDFQLTokenizerStatementNode) node );
        }

        throw new NotYetImplemetedException( "Node type (" + node.getClass().getSimpleName() + ") is not supported." );
    }

    private DataFrameTokenizerJob compile_TypedDFQLTokenizerStatementNode( TypedDFQLTokenizerStatementNode node ) {

        List<DataFrame> extractedDataframes = CompilerUtils.extractDataframesAsList( node.getDataFrames() );
        if (extractedDataframes.size() == 0) {
            throw new NotYetImplemetedException( "No dataframes found in given node." );
        }
        // extract the first node
        DataFrame dataFrame = extractedDataframes.get( 0 );

        // extract the column names
        List<String> columns = CompilerUtils.extractColumnNamesAsStringList( node.getTransferColumns() );
        String[] columnsToTransfer = columns.toArray( new String[0] );

        // extract the name of the column 
        String columnToTokenize = CompilerUtils.extractColumnName( node.getTokenizeInputColumn() );

        // extract the ingestProcessorName
        String ingestionProcessorName = CompilerUtils.extractColumnName( node.getIngestProcessor() );

        return new DataFrameTokenizerJobImpl( dataFrame, columnsToTransfer, columnToTokenize, ingestionProcessorName );
    }

}
