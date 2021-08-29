package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLApplyNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLBinaryOperatorNode;
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
        assertThat( result, is( instanceOf( DFQLNumberNode.class ) ) );
    }

    @Test
    public void testParseLiteral_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_Number_expectInstanceofDFQLNumberNode() throws Exception {
        // arrange
        String dfqlQuery = "123";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLNumberNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelection_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLPrimarySelectionNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelectionStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLPrimarySelectionNode.class ) ) );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionInvocationOfStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith()";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertThat( result, is( instanceOf( DFQLApplyNode.class ) ) );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnSelectionNoInvocationOfStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertThat( result, is( instanceOf( DFQLPrimarySelectionNode.class ) ) );
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
        assertThat( result, is( instanceOf( DFQLApplyNode.class ) ) );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithSingleStringArgumentAndInt_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertThat( result, is( instanceOf( DFQLApplyNode.class ) ) );
    }

    @Test
    public void testParseMemberSelectionInvocation_DataframeColumnInvocationOfStartsWithTwoStringArguments_expectApplyNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith(\"0x666\",\"\")";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseMemberSelectionInvocation();

        // assert
        assertThat( result, is( instanceOf( DFQLApplyNode.class ) ) );
    }

    @Test
    public void testParseExpression_DataFrameColumsEqualsNumber_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'==1";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertThat( result, is( instanceOf( DFQLBinaryOperatorNode.class ) ) );
    }

    @Test
    public void testParseExpression_DataFrameColumEqualsNumberInParenthesis_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertThat( result, is( instanceOf( DFQLBinaryOperatorNode.class ) ) );
    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOther_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "(df.'columnname'==1) == (df.'othercolumnname'==666)";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertThat( result, is( instanceOf( DFQLBinaryOperatorNode.class ) ) );
    }

    @Test
    public void testParseExpression_DataTwoFrameColumEqualsNumberInParenthesisEqualsEachOtherInParenthesis_expectBinaryOperatorNode() throws Exception {
        // arrange
        String dfqlQuery = "((df.'columnname'>=1)==(df.'othercolumnname'<=666))";
        DataFrameQueryLanguageParser parser = createParser( dfqlQuery );

        // act
        DFQLNode result = parser.parseExpression();

        // assert
        assertThat( result, is( instanceOf( DFQLBinaryOperatorNode.class ) ) );
    }

    private DataFrameQueryLanguageParser createParser( String dfqlQuery ) {
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );
        return parser;
    }

}
