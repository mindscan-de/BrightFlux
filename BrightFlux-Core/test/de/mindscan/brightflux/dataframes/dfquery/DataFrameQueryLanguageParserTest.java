package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLIdentifierNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLPrimarySelectionNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLStringNode;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;

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
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionInvocationOfStartsWith_expectPrimarySelectionNode() throws Exception {
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
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringParameterArgument_expectPrimarySelectionNode()
                    throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertNodeType( result, DFQLApplyNode.class );
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
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOther_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1) == (df.'othercolumnname'==666)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLBinaryOperatorNode.class );
    }

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
    public void testParseExpression_EmptyString_expectEmptyNode() throws Exception {
        // arrange
        String dfqlQuery = "";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertNodeType( result, DFQLEmptyNode.class );
    }

    private void assertNodeType( DFQLNode result, Class<? extends DFQLNode> type ) {
        assertThat( result, is( instanceOf( type ) ) );
    }

    private void assertNodeDescription( DFQLNode result, String expectedDescription ) {
        assertThat( result.describeNodeOperation(), equalTo( expectedDescription ) );
    }

    private DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();
        tokenizer.setIgnoreWhiteSpace( true );

        Iterator<DFQLToken> tokenIterator = tokenizer.tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );
        return parser;
    }

}
