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
package de.mindscan.brightflux.dataframes.dfquery.compiler;

import java.util.List;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLListNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLSelectStatementNode;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * 
 */
public class CompilerUtils {

    public static String extractColumnName( DFQLNode node ) {
        if (node instanceof DFQLIdentifierNode) {
            // Actually this should not happen...
            return (String) ((DFQLIdentifierNode) node).getRawValue();
        }
        else if (node instanceof TypedDFQLDataFrameColumnNode) {
            return ((TypedDFQLDataFrameColumnNode) node).getColumnName();
        }
        else if (node instanceof DFQLStringNode) {
            return (String) ((DFQLStringNode) node).getRawValue();
        }

        throw new NotYetImplemetedException();
    }

    public static List<String> extractColumnNamesAsStringList( DFQLNode node ) {
        if (node instanceof DFQLListNode) {
            return ((DFQLListNode) node).getNodes().stream().map( CompilerUtils::extractColumnName ).collect( Collectors.toList() );
        }
        else if (node instanceof TypedDFQLSelectStatementNode) {
            return extractColumnNamesAsStringList( ((TypedDFQLSelectStatementNode) node).getSelectedColumns() );
        }

        throw new NotYetImplemetedException();
    }

    public static List<DataFrame> extractDataframesAsList( List<TypedDFQLDataFrameNode> list ) {
        List<DataFrame> collected = list.stream().map( node -> node.getDataFrame() ).collect( Collectors.toList() );

        return collected;
    }

}
