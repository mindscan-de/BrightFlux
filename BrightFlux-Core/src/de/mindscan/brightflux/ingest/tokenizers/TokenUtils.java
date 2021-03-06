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

import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.token.ColumnHeaderToken;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.IdentifierToken;
import de.mindscan.brightflux.ingest.token.LineSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;
import de.mindscan.brightflux.ingest.token.TextToken;

/**
 * 
 */
public class TokenUtils {

    static DataToken createToken( Class<? extends DataToken> currentTokenType, String valueString ) {
        if (NumberToken.class.equals( currentTokenType )) {
            return NumberToken.create( valueString );
        }
        else if (ColumnSeparatorToken.class.equals( currentTokenType )) {
            return ColumnSeparatorToken.create();
        }
        else if (IdentifierToken.class.equals( currentTokenType )) {
            return IdentifierToken.create( valueString );
        }
        else if (LineSeparatorToken.class.equals( currentTokenType )) {
            // TODO: maybe count the lines...?
            return LineSeparatorToken.create();
        }
        else if (TextToken.class.equals( currentTokenType )) {
            return TextToken.create( valueString );
        }
        else if (ColumnHeaderToken.class.equals( currentTokenType )) {
            return ColumnHeaderToken.create( valueString );
        }

        throw new IllegalArgumentException( "tokentype is: " + currentTokenType );
    }

    static DataToken createToken( Class<? extends DataToken> currentTokenType, String valueString, String otherValue ) {
        if (ColumnHeaderToken.class.equals( currentTokenType )) {
            return ColumnHeaderToken.create( valueString, otherValue );
        }

        throw new IllegalArgumentException( "tokentype is: " + currentTokenType );
    }

}
