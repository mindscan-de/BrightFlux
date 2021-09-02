package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorType;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.DFQLDataFrameColumnNode;
import de.mindscan.brightflux.dataframes.dfquery.runtime.TypedDFQLDataFrameNode;

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
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 1 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'columnName'==1)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithNamedColumnEqualsValueTwo_compilesToColumnEqualsTwoValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'columnName'==2)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnEqualsValueThree_compilesToColumnEqualsThreeValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( eq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'==3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnNotEqualsValueThree_compilesToColumnNotEqualsThreeValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode neq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.NEQ, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( neq );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'!=3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnGreaterOrEqualsValueThree_compilesToColumnGreaterOrEqualsThreeValuePredicate()
                    throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode ge = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.GE, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( ge );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'>=3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnLessOrEqualsValueThree_compilesToColumnLessOrEqualsThreeValuePredicate()
                    throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode le = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.LE, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( le );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'<=3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnGreaterThanValueThree_compilesToColumnGreaterThanThreeValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode gt = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.GT, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( gt );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'>3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithOtherNamedColumnLessThanValueThree_compilesToColumnLessThanThreeValuePredicate() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode lt = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.LT, left, right );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( lt );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "(df.'otherColumnName'<3)" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithTwoComparisonsCombinedWithAnd_compilesToTwoComparisonCombinedWithAnd() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // left:
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left_left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode left_right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode left_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left_left, left_right );

        // right:
        DFQLDataFrameColumnNode right_left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right_right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode right_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, right_left, right_right );

        // and
        DFQLBinaryOperatorNode and = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.AND, left_eq, right_eq );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( and );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "((df.'columnName'==3)&&(df.'otherColumnName'==2))" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithTwoComparisonsCombinedWithOr_compilesToTwoComparisonCombinedWithOr() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // left:
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left_left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode left_right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode left_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left_left, left_right );

        // right:
        DFQLDataFrameColumnNode right_left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right_right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode right_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, right_left, right_right );

        // and
        DFQLBinaryOperatorNode or = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.OR, left_eq, right_eq );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( or );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "((df.'columnName'==3)||(df.'otherColumnName'==2))" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithTwoComparisonsCombinedWithEqualTo_compilesToTwoComparisonCombinedWithEqualTo() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // left:
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left_left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode left_right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode left_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left_left, left_right );

        // right:
        DFQLDataFrameColumnNode right_left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right_right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode right_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, right_left, right_right );

        // and
        DFQLBinaryOperatorNode or = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left_eq, right_eq );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( or );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "((df.'columnName'==3)==(df.'otherColumnName'==2))" ) );
    }

    @Test
    public void testCompileToRowFilterPredicate_DFWithTwoComparisonsCombinedWithNotEqualTo_compilesToTwoComparisonCombinedWithNotEqualTo() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // left:
        TypedDFQLDataFrameNode dfn = new TypedDFQLDataFrameNode( null );
        DFQLDataFrameColumnNode left_left = new DFQLDataFrameColumnNode( dfn, "columnName" );
        DFQLNumberNode left_right = new DFQLNumberNode( Integer.valueOf( 3 ) );
        DFQLBinaryOperatorNode left_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, left_left, left_right );

        // right:
        DFQLDataFrameColumnNode right_left = new DFQLDataFrameColumnNode( dfn, "otherColumnName" );
        DFQLNumberNode right_right = new DFQLNumberNode( Integer.valueOf( 2 ) );
        DFQLBinaryOperatorNode right_eq = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.EQ, right_left, right_right );

        // and
        DFQLBinaryOperatorNode or = new DFQLBinaryOperatorNode( DFQLBinaryOperatorType.NEQ, left_eq, right_eq );

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( or );

        // assert
        String result = predicate.describeOperation().trim();
        assertThat( result, equalTo( "((df.'columnName'==3)!=(df.'otherColumnName'==2))" ) );
    }

}
