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
package de.mindscan.brightflux.dataframes.dfquery.ast;

/**
 * 
 */
public class DFQLTokenizeStatementNode implements DFQLNode {

    private DFQLNode directTransferColumnsList;
    private DFQLNode tokenizerName;
    // TODO: the columname 
    private DFQLNode dataFrames;

    /**
     * @param directTransferColumnsList
     */
    public void setDataFrameDirectTransferColumns( DFQLNode directTransferColumnsList ) {
        this.directTransferColumnsList = directTransferColumnsList;
    }

    /**
     * @return the directTransferColumnsList
     */
    public DFQLNode getDataFrameDirectTransferColumns() {
        return directTransferColumnsList;
    }

    /**
     * @param dataFrames
     */
    public void setDataFrames( DFQLNode dataFrames ) {
        this.dataFrames = dataFrames;
    }

    /**
     * @return the dataFrames
     */
    public DFQLNode getDataFrames() {
        return dataFrames;
    }

    /**
     * @param tokenizername
     */
    public void setTokenizerName( DFQLNode tokenizerName ) {
        this.tokenizerName = tokenizerName;
    }

    /**
     * @return the tokenizerName
     */
    public DFQLNode getTokenizerName() {
        return tokenizerName;
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
