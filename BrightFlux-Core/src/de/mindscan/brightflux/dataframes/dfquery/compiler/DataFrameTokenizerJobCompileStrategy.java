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

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.dfquery.DataFrameTokenizerJob;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLTokenizerStatementNode;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * We want to compile a tokenizer command, wich can be executed by the ingest
 */
public class DataFrameTokenizerJobCompileStrategy {

    public DataFrameTokenizerJob compile( DFQLNode node ) {
        if (node instanceof TypedDFQLTokenizerStatementNode) {
            // TODO : have a return type and return this.
            return compile_TypedDFQLTokenizerStatementNode( (TypedDFQLTokenizerStatementNode) node );
        }

        throw new NotYetImplemetedException( "Node type (" + node.getClass().getSimpleName() + ") is not supported." );
    }

    private DataFrameTokenizerJob compile_TypedDFQLTokenizerStatementNode( TypedDFQLTokenizerStatementNode node ) {

        // TODO: extract the node
        DataFrame dataFrame = null;
        // TODO: extract the column names 
        String[] columnsToTransfer = null;
        // TODO: extract the name of the column 
        String columnToTokenize = null;
        // TODO: missing the ingestProcessorName
        String ingestionProcessorName = null;

        return new DataFrameTokenizerJobImpl( dataFrame, columnsToTransfer, columnToTokenize, ingestionProcessorName );
    }

}
