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
package de.mindscan.brightflux.dataframes.dfquery.tokenizer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 */
public final class DFQLTokenizerTerminals {

    // Keywords
    public final static String[] TERMINAL_KEYWORDS = new String[] { "SELECT", "FROM", "WHERE", "ALL" /*, "AS"*/ };
    public final static Set<String> TERMINAL_KEYWORD_SET = convertToUppercaseSet( TERMINAL_KEYWORDS );

    // Operators
    public final static String[] operators = new String[] { "==", "!=", "<=", ">=", "&&", "||", "<", ">", ".", ",", "*", "+", "-", "!" };
    public final static Set<String> operatorTwoChars = filteredByLength( 2, operators );
    public final static Set<String> operatorOneChar = filteredByLength( 1, operators );
    public final static char[] firstMengeOperators = firstMenge( operators );

    // Whitespaces
    public final static char[] whitespace = new char[] { ' ', '\t', '\r', '\n' };

    // Numbers
    public final static char[] numbers = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    // Parenthesis
    public final static char[] parenthesis = new char[] { '(', ')' };

    // Quotes
    public final static char[] quotes = new char[] { '\'', '"' };

    // ----------------
    // Helper functions
    // ----------------    

    static Set<String> convertToUppercaseSet( String[] elements ) {
        return Arrays.stream( elements ).map( e -> e.toUpperCase() ).collect( Collectors.toSet() );
    }

    static Set<String> filteredByLength( int length, String[] elements ) {
        return Arrays.stream( elements ).filter( e -> e.length() == length ).collect( Collectors.toSet() );
    }

    static char[] firstMenge( String[] elements ) {
        // each first char only once
        Set<String> firstCharacters = Arrays.stream( elements ).map( e -> e.substring( 0, 1 ) ).collect( Collectors.toSet() );
        // convert strings of length 1 to char array
        return firstCharacters.stream().collect( Collectors.joining() ).toCharArray();
    }

}
