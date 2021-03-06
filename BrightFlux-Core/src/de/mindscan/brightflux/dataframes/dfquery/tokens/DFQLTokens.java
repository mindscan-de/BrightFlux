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

/**
 * 
 */
public class DFQLTokens {

    public final static DFQLToken KEYWORD_SELECT = new DFQLToken( DFQLTokenType.KEYWORD, "SELECT" );
    public final static DFQLToken KEYWORD_ROWCALLBACK = new DFQLToken( DFQLTokenType.KEYWORD, "ROWCALLBACK" );
    public final static DFQLToken KEYWORD_FROM = new DFQLToken( DFQLTokenType.KEYWORD, "FROM" );
    public final static DFQLToken KEYWORD_WHERE = new DFQLToken( DFQLTokenType.KEYWORD, "WHERE" );
    public final static DFQLToken KEYWORD_NOT = new DFQLToken( DFQLTokenType.KEYWORD, "NOT" );
    public final static DFQLToken KEYWORD_TOKENIZE = new DFQLToken( DFQLTokenType.KEYWORD, "TOKENIZE" );
    public static final DFQLToken KEYWORDS_USING = new DFQLToken( DFQLTokenType.KEYWORD, "USING" );

    public final static DFQLToken OPERATOR_STAR = new DFQLToken( DFQLTokenType.OPERATOR, "*" );
    public final static DFQLToken OPERATOR_COMMA = new DFQLToken( DFQLTokenType.OPERATOR, "," );

}
