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

/**
 * 
 */
public class CSVTokenizerTerminals {

    // Numbers
    public final static char[] TERMINAL_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    // Quotes
    public final static char[] TERMINAL_QUOTES = new char[] { '\'', '"' };

    // --------------
    // service method
    // --------------

    public static boolean isCharIn( char currentChar, char[] charSet ) {
        for (int i = 0; i < charSet.length; i++) {
            if (currentChar == charSet[i]) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------
    // service method for recognizing the csv terminals
    // ------------------------------------------------

    static boolean isDigit( char currentChar ) {
        return isCharIn( currentChar, TERMINAL_DIGITS );
    }

    public static boolean isStartOfQuote( char currentChar ) {
        return isCharIn( currentChar, TERMINAL_QUOTES );
    }

    static boolean isStartOfIdentifier( char currentChar ) {
        return Character.isJavaIdentifierStart( currentChar );
    }

    public static boolean isPartOfIdentifier( char currentChar ) {
        return Character.isJavaIdentifierPart( currentChar );
    }

    static boolean isFraction( char currentChar ) {
        return currentChar == '.';
    }

    static boolean isDigitOrFraction( char currentChar ) {
        return isDigit( currentChar ) || isFraction( currentChar );
    }

}
