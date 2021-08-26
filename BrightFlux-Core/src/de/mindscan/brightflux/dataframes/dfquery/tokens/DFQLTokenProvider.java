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
package de.mindscan.brightflux.dataframes.dfquery.tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * implement a simple dfql-token provider, which will be used in the parser.
 */
public class DFQLTokenProvider {

    private List<DFQLToken> list;
    private DFQLToken defaultToken;
    private DFQLToken value;

    private int currentPosition;
    private int markedPosition;

    private final static int MARKED_NONE = Integer.MIN_VALUE;

    /**
     * 
     */
    public DFQLTokenProvider( Iterator<DFQLToken> tokenIterator ) {
        this.list = toList( tokenIterator );
        this.currentPosition = 0;
        this.defaultToken = null;
        this.value = null;
        this.markedPosition = MARKED_NONE;
    }

    /**
     * This method returns the element from the last operation (e.g. next, lookahead)
     * 
     * @return the last element 
     */
    public DFQLToken last() {
        return this.value;
    }

    /**
     * This method returns the next element at the current position and moves to the next position index
     * 
     * @return the current / token.
     */
    public DFQLToken next() {
        try {
            this.value = this.list.get( this.currentPosition );
            this.currentPosition++;
        }
        catch (IndexOutOfBoundsException ioobe) {
            throw new NoSuchElementException( "No more elements in this TokenProvider." );
        }
        return this.value;
    }

    /**
     * This method returns the current element at the current position but doesn't move the current position index
     * 
     * @return the current token
     */
    public DFQLToken lookahead() {
        return lookahead( 0 );
    }

    /**
     * This method returns the skipCount next elemnt to the current element at the skipcount + current positon but 
     * lookahead doesn't move the current position index.
     * 
     * @param skipCount number of positions ahead to the current positions
     * @return The token at the positon (currentPosition + skipCount)
     */
    public DFQLToken lookahead( int skipCount ) {
        try {
            this.value = this.list.get( this.currentPosition + skipCount );
        }
        catch (IndexOutOfBoundsException e) {
            return this.defaultToken;
        }
        return this.value;
    }

    // save current position
    public void pushMarker() {
        this.markedPosition = this.currentPosition;
    }

    // discard the current position
    public void discardMarker() {
        this.markedPosition = MARKED_NONE;
    }

    // restore previously saved position
    public void popMarker() {
        if (this.markedPosition != MARKED_NONE) {
            this.currentPosition = this.markedPosition;
        }
        else {
            throw new IllegalStateException( "PopMarker has an invalid state." );
        }
    }

    // convert the iterator into a list representation
    private static List<DFQLToken> toList( Iterator<DFQLToken> iterator ) {
        ArrayList<DFQLToken> result = new ArrayList<>();
        iterator.forEachRemaining( result::add );
        return result;
    }

}
