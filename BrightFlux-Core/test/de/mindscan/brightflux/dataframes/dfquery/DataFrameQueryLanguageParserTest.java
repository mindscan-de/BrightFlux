package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLSelectStatementNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.parser.DataFrameQueryLanguageParserFactory;

public class DataFrameQueryLanguageParserTest {

    @Test
    public void testParseLiteral_Number_expectInstanceofDFQLNumberNode() throws Exception {
        // arrange
        String dfqlQuery = "123";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLNumberNode.class );
    }

    @Test
    public void testParseLiteral_Number_expectSameNumberAsDescription() throws Exception {
        // arrange
        String dfqlQuery = "123";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "123" );
    }

    @Test
    public void testParseLiteral_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseLiteral_NumberAsString_expectSameNumberAsStringDescription() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "'123'" );
    }

    @Test
    public void testParseLiteral_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseLiteral_UnderscoreXyzAsString_expectSameStringAsDescription() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "'_xyz_'" );
    }

    @Test
    public void testParseLiteral_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseLiteral_FooAsString_expectSameStringAsDescription() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "'foo'" );
    }

    @Test
    public void testParseLiteral_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseLiteral_FooAsIdentifier_expectFooAsIdentiferDescription() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "foo" );
    }

    @Test
    public void testParseLiteral_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseLiteral_UppercaseDFAsIdentifier_expectUpperCaseDFAsIdentiferDescription() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "DF" );
    }

    @Test
    public void testParseLiteral_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseLiteral_UnderscoreUppercaseDFAsIdentifier_expectUnderscoreUpperCaseDFAsIdentiferDescription() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertNodeDescription( result, "_DF" );
    }

    @Test
    public void testParseMemberSelection_Number_expectInstanceofDFQLNumberNode() throws Exception {
        // arrange
        String dfqlQuery = "123";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLNumberNode.class );
    }

    @Test
    public void testParseMemberSelection_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseMemberSelection_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseMemberSelection_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLStringNode.class );
    }

    @Test
    public void testParseMemberSelection_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseMemberSelection_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseMemberSelection_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLIdentifierNode.class );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelection_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLPrimarySelectionNode.class );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelection_expectPrimarySelectionDFColoumnSelectionDescriptionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeDescription( result, "df.'columnname'" );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelectionStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeType( result, DFQLPrimarySelectionNode.class );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelectionStartsWith_expectSameDescription() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertNodeDescription( result, "df.'columnname'.startsWith" );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionInvocationOfStartsWith_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith()";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLApplyNode.class );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionInvocationOfStartsWith_expectSameDescription() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith()";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeDescription( result, "df.'columnname'.startsWith()" );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionNoInvocationOfStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLPrimarySelectionNode.class );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringParameterArgument_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLApplyNode.class );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringParameterArgument_expectSameNodeDescriptione()
                    throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeDescription( result, "df.'columnname'.startsWith('0x666')" );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringArgumentAndInt_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLApplyNode.class );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringArgumentAndInt_expectSameNodeDescription()
                    throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeDescription( result, "df.'columnname'.startsWith('0x666',1)" );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithTwoStringArguments_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",\"\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLApplyNode.class );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithTwoStringArguments_expectSameNodeDescription() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",\"\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeDescription( result, "df.'columnname'.startsWith('0x666','')" );
    }

    @Test
    public void testParseExpression_DataFrameColumsEqualsNumber_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'==1";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLBinaryOperatorNode.class );
    }

    @Test
    public void testParseExpression_DataFrameColumsEqualsNumber_expectSameNodeDescription() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'==1";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeDescription( result, "(df.'columnname'==1)" );
    }

    @Test
    public void testParseExpression_DataFrameColumEqualsNumberInParenthesis_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLBinaryOperatorNode.class );
    }

    @Test
    public void testParseExpression_DataFrameColumEqualsNumberInParenthesis_expectSameNodeDescription() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeDescription( result, "(df.'columnname'==1)" );
    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberAllInParenthesisEqualsEachOther_expectSameNodeDescription() throws Exception {
        // arrange
        String dfqlQuery = "((df.'columnname'==1) == (df.'othercolumnname'==666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeDescription( result, "((df.'columnname'==1)==(df.'othercolumnname'==666))" );
    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOther_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1) == (df.'othercolumnname'==666)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLBinaryOperatorNode.class );
    }

    //TODO: fix me
//    @Disabled( " it only reads the first expression for some reason..." )
//    @Test
//    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOther_expectSameNodeDescription() throws Exception {
//        // arrange
//        String dfqlQuery = "(df.'columnname'==1) == (df.'othercolumnname'==666)";
//        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );
//
//        // act
//        DFQLNode result = parser.parseExpression();
//
//        // assert
//        assertNodeDescription( result, "((df.'columnname'==1)==(df.'othercolumnname'==666))" );
//    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOtherInParenthesis_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "((df.'columnname'>=1)==(df.'othercolumnname'<=666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLBinaryOperatorNode.class );
    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOtherInParenthesis_expectSameDescription() throws Exception {
        // arrange
        String dfqlQuery = "((df.'columnname'>=1)==(df.'othercolumnname'<=666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeDescription( result, "((df.'columnname'>=1)==(df.'othercolumnname'<=666))" );
    }

    @Test
    public void testParseExpression_EmptyString_expectEmptyNode() throws Exception {
        // arrange
        String dfqlQuery = "";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLEmptyNode.class );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectStatementWithComplexWhereClause_expectSelectStatementNode() throws Exception {
        // arrange
        String dfqlQuery = "SELECT * FROM df WHERE ((df.'columnname'>=1) && (df.'othercolumnname'<=666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeType( result, DFQLSelectStatementNode.class );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectSimpleSelectStatemenet_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT * FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT * FROM df" );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectStatementWithComplexWhereClause_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT * FROM df WHERE ((df.'columnname'>=1) && (df.'othercolumnname'<=666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT * FROM df WHERE ((df.'columnname'>=1)&&(df.'othercolumnname'<=666))" );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectOneDataframeColumnSelectStatemenet_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT df.'column' FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT df.'column' FROM df" );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectTwoDataframeColumnSelectStatemenet_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT df.'column', df.'otherColumn' FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT df.'column',df.'otherColumn' FROM df" );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectTwoColumnNamesSelectStatemenet_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT 'column', 'otherColumn' FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT 'column','otherColumn' FROM df" );
    }

    @Test
    public void testParseDFQLCallbackStatement_HighlightRedForALlRows_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "ROWCALLBACK highlight_red FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLCallbackStatement();

        // assert
        assertNodeDescription( result, "ROWCALLBACK highlight_red FROM df" );
    }

    @Test
    public void testParseDFQLCallbackStatement_HighlightRedForALlRowsWHEREColumnValueIsOne_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "ROWCALLBACK highlight_red FROM df WHERE (df.'x'==1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLCallbackStatement();

        // assert
        assertNodeDescription( result, "ROWCALLBACK highlight_red FROM df WHERE (df.'x'==1)" );
    }

    @Test
    public void testParseDFQLSelectStatement_Tokenizerelection_expectSameQuery() throws Exception {
        String dfqlQuery = "SELECT '__idx','__orgidx' TOKENIZE df.'abc' USING 'XYZ' FROM df";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT '__idx','__orgidx' TOKENIZE df.'abc' USING 'XYZ' FROM df" );

    }

    @Test
    public void testParseDFQLSelectStatement_SelectStatementWithComplexWhereClauseBiAndApply_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT * FROM df WHERE ((df.'columnname'>=1) && (df.'othercolumnname'.contains('Debug')))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT * FROM df WHERE ((df.'columnname'>=1)&&df.'othercolumnname'.contains('Debug'))" );
    }

    @Test
    public void testParseDFQLSelectStatement_SelectStatementWithComplexWhereClauseBiOrApply_expectSameQuery() throws Exception {
        // arrange
        String dfqlQuery = "SELECT * FROM df WHERE ((df.'columnname'>=1) || (df.'othercolumnname'.contains('Debug')))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseDFQLSelectStatement();

        // assert
        assertNodeDescription( result, "SELECT * FROM df WHERE ((df.'columnname'>=1)||df.'othercolumnname'.contains('Debug'))" );
    }

    private void assertNodeType( DFQLNode result, Class<? extends DFQLNode> type ) {
        assertThat( result, is( instanceOf( type ) ) );
    }

    private void assertNodeDescription( DFQLNode result, String expectedDescription ) {
        assertThat( result.describeNodeOperation(), equalTo( expectedDescription ) );
    }

    private DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        return DataFrameQueryLanguageParserFactory.createParser( dfqlQuery );
    }

}
