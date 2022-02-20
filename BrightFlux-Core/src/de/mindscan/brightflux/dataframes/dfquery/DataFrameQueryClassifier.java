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
package de.mindscan.brightflux.dataframes.dfquery;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLCallbackStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLTokenizeStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.parser.DataFrameQueryLanguageParserFactory;

/**
 * 
 */
public class DataFrameQueryClassifier {

    public static DataFrameQueryType classifyDFQLQuery( String query ) {
        DataFrameQueryLanguageParser parser = DataFrameQueryLanguageParserFactory.createParser( query );
        DFQLNode statement = parser.parseDFQLStatement();

        if (statement instanceof DFQLTokenizeStatementNode) {
            return DataFrameQueryType.DMS_TOKENIZE;
        }
        else if (statement instanceof DFQLSelectStatementNode) {
            return DataFrameQueryType.DMS_SELECT;
        }
        else if (statement instanceof DFQLCallbackStatementNode) {
            return DataFrameQueryType.PS_CALLBACK;
        }

        return DataFrameQueryType.UNKNOWN;
    }

}
