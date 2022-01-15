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
package de.mindscan.brightflux.dataframes.dfquery.runtime;

import java.util.List;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;

/**
 * 
 */
public class TypedDFQLTokenizerStatementNode implements TypedDFQLNode {

    // SELECT
    private DFQLNode transferColumns;
    // TOKENIZE
    private DFQLNode tokenizeInputColumn;
    // USING
    private DFQLNode ingestProcessor;
    // FROM
    private List<TypedDFQLDataFrameNode> dataFrames;

    /**
     * 
     */
    public TypedDFQLTokenizerStatementNode( DFQLNode transferColumns, DFQLNode tokenizeInputColumn, DFQLNode ingestProcessor,
                    List<TypedDFQLDataFrameNode> dataFrames ) {
        this.transferColumns = transferColumns;
        this.tokenizeInputColumn = tokenizeInputColumn;
        this.ingestProcessor = ingestProcessor;
        this.dataFrames = dataFrames;
    }

    public DFQLNode getTransferColumns() {
        return transferColumns;
    }

    public DFQLNode getTokenizeInputColumn() {
        return tokenizeInputColumn;
    }

    public DFQLNode getIngestProcessor() {
        return ingestProcessor;
    }

    public List<TypedDFQLDataFrameNode> getDataFrames() {
        return dataFrames;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeNodeOperation() {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeNodeOperationDebug() {
        // TODO Auto-generated method stub
        return null;
    }

}
