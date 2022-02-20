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
package de.mindscan.brightflux.dataframes.dfquery.parser;

import java.util.Iterator;

import de.mindscan.brightflux.dataframes.dfquery.DataFrameQueryLanguageParser;
import de.mindscan.brightflux.dataframes.dfquery.DataFrameQueryLanguageTokenizer;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;

/**
 * 
 */
public class DataFrameQueryLanguageParserFactory {

    public static DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();
        tokenizer.setIgnoreWhiteSpace( true );

        Iterator<DFQLToken> tokenIterator = tokenizer.tokenize( dfqlQuery );

        return new DataFrameQueryLanguageParser( new DFQLTokenProvider( tokenIterator ) );
    }

}
