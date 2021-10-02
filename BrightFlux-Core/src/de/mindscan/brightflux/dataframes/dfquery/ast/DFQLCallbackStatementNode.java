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
package de.mindscan.brightflux.dataframes.dfquery.ast;

/**
 * 
 */
public class DFQLCallbackStatementNode implements DFQLNode {

    private DFQLNode whereClause = new DFQLEmptyNode();
    private DFQLNode parsedDataFrames = new DFQLEmptyNode();
    private DFQLNode parsedCallBackIdentifier = new DFQLEmptyNode();

    /**
     * @param whereClause
     */
    public void setWhereClause( DFQLNode whereClause ) {
        this.whereClause = whereClause;
    }

    /**
     * @return the whereClause
     */
    public DFQLNode getWhereClause() {
        return whereClause;
    }

    /**
     * @param parsedCallBackIdentifier the parsedCallBackIdentifier to set
     */
    public void setCallBackIdentifier( DFQLNode parsedCallBackIdentifier ) {
        this.parsedCallBackIdentifier = parsedCallBackIdentifier;
    }

    /**
     * @return the parsedCallBackIdentifier
     */
    public DFQLNode getCallBackIdentifier() {
        return parsedCallBackIdentifier;
    }

    /**
     * @param parsedDataFrames the parsedDataFrames to set
     */
    public void setDataFrames( DFQLNode parsedDataFrames ) {
        this.parsedDataFrames = parsedDataFrames;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String describeNodeOperation() {
        StringBuilder sb = new StringBuilder();
        // TODO Auto-generated method stub

        sb.append( "CALLBACK " );
        sb.append( parsedCallBackIdentifier.describeNodeOperation() );
        sb.append( " FROM " );
        sb.append( parsedDataFrames.describeNodeOperation() );

        String whereClauseAsString = whereClause.describeNodeOperation();
        if (!whereClauseAsString.isEmpty()) {
            sb.append( " WHERE " );
            sb.append( whereClauseAsString );
        }

        return sb.toString();
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
