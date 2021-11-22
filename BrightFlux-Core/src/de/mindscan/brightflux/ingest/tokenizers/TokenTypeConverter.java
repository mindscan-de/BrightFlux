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
package de.mindscan.brightflux.ingest.tokenizers;

import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.token.NumberToken;
import de.mindscan.brightflux.ingest.token.TextToken;

/**
 * 
 */
public class TokenTypeConverter {

    public static Class<? extends DataToken> getTokenTypeForValueType( String columnValueType ) {
        switch (columnValueType) {
            case ColumnValueTypes.COLUMN_TYPE_BOOL:
                throw new NotYetImplemetedException( "This 'Column_Type_Bool' can currently not be converted into TokenType: " );
            case ColumnValueTypes.COLUMN_TYPE_DOUBLE:
                return NumberToken.class;
            case ColumnValueTypes.COLUMN_TYPE_FLOAT:
                return NumberToken.class;
            case ColumnValueTypes.COLUMN_TYPE_INT:
                return NumberToken.class;
            case ColumnValueTypes.COLUMN_TYPE_LONG:
                return NumberToken.class;
            case ColumnValueTypes.COLUMN_TYPE_STRING:
                return TextToken.class;
            default:
                throw new NotYetImplemetedException( "This columValueType '" + columnValueType + "' can not be converted into TokenType: " );
        }
    }

}
