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
package de.mindscan.brightflux.dataframes.dfquery;

import java.util.List;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLListNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.compiler.RowFilterPredicateCompileStrategy;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLSelectStatementNode;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 * We use a compiler to compile a Node into the objects understood by the dataframe.
 * 
 * I don't know whether it is a good idea to hard code the PredicateFactory here - i have to think about this stuff... 
 * Maybe we can 
 */
public class DataFrameQueryLanguageCompiler {

    private RowFilterPredicateCompileStrategy rowFilterStrategy = new RowFilterPredicateCompileStrategy();

    // TODO: ColumnColumn Predicate
    // TODO: PredicatePredicate Predicate
    // TODO: BiBi Predicate

    // TODO: boolean predicate functions like contains, startsWith, endsWith, and "not"

    public DataFrameRowFilterPredicate compileToRowFilterPredicate( DFQLNode node ) {
        return rowFilterStrategy.compile( node );
    }

    public List<String> getColumNamesAsStrings( DFQLNode node ) {
        if (node instanceof DFQLListNode) {
            return ((DFQLListNode) node).getNodes().stream().map( this::columnNameExtractor ).collect( Collectors.toList() );
        }
        else if (node instanceof TypedDFQLSelectStatementNode) {
            return getColumNamesAsStrings( ((TypedDFQLSelectStatementNode) node).getSelectedColumns() );
        }

        throw new NotYetImplemetedException();
    }

    private String columnNameExtractor( DFQLNode node ) {
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

}
