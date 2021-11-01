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
package de.mindscan.brightflux.ingest.datasource.impl;

import java.util.function.Predicate;

import de.mindscan.brightflux.ingest.datasource.DataSourceLexer;

/**
 * TODO: try a very simple conversion for the datasource lexer...
 */
public class DataFrameBackedDataSourceLexer implements DataSourceLexer {

    /** 
     * {@inheritDoc}
     */
    @Override
    public char charAtTokenEnd() {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public char charAtTokenStart() {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenEndBeforeInputEnd() {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenStartBeforeInputEnd() {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEnd() {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getTokenString() {
        // TODO Auto-generated method stub
        return null;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEndWhileNot( Predicate<Character> object ) {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void incrementTokenEndWhile( Predicate<Character> object ) {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getTokenEnd() {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getTokenStart() {
        // TODO Auto-generated method stub
        return 0;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void prepareNextToken() {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void resetTokenPositions() {
        // TODO Auto-generated method stub
        // TODO maybe reinitialize the dataframe iterator.. here
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void advanceToNextToken() {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        // intentionally left empty, because no extra resource is acquired, but we may 
    }

}