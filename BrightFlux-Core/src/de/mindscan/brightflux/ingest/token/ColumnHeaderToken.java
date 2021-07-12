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
package de.mindscan.brightflux.ingest.token;

import de.mindscan.brightflux.ingest.DataToken;

/**
 * In case of a binary parser, we might want to already define the column headers and also provide type hints
 * for the columns, such that the data frame compiler doesn't need to guess or do the type interference for the 
 * column or its type. 
 */
public class ColumnHeaderToken implements DataToken {

    private String columnHeaderName;
    private String typeHint;

    /**
     * 
     */
    public ColumnHeaderToken( String columnHeaderName ) {
        this( columnHeaderName, null );
    }

    /**
     * 
     */
    public ColumnHeaderToken( String columnHeaderName, String typeHint ) {
        this.columnHeaderName = columnHeaderName;
        this.typeHint = typeHint;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        if (typeHint == null) {
            return columnHeaderName;
        }

        return columnHeaderName + "::<" + typeHint.trim() + ">";
    }

    /**
     * @return the columnHeaderName
     */
    public String getColumnHeaderName() {
        return columnHeaderName;
    }

    /**
     * @return the typeHint
     */
    public String getTypeHint() {
        return typeHint;
    }
}
