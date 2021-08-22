package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLDataFrameNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;

public class DataFrameQueryLanguageCompilerTest {

    @Test
    public void testCompileToRowFilterPredicate_EmptyNode_CompileToAny() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( new DFQLEmptyNode() );

        // assert
        String result = predicate.describeOperation();
        assertThat( result, equalTo( "true" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithNamedColumnEqualsValueOne_compilesToColumnEqualsOneValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DFQLDataFrameNode dfn = new DFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 1 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "( df.'columnName' == 1 )" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithNamedColumnEqualsValueTwo_compilesToColumnEqualsTwoValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DFQLDataFrameNode dfn = new DFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "( df.'columnName' == 2 )" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnEqualsValueThree_compilesToColumnEqualsThreeValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        DFQLDataFrameNode dfn = new DFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "( df.'otherColumnName' == 3 )" ) );
    }

}
