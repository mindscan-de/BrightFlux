package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

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
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLNumberNode.class ) ) );
    }

    @Test
    public void testParseLiteral_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseLiteral_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseLiteral_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_Number_expectInstanceofDFQLNumberNode() throws Exception {
        // arrange
        String dfqlQuery = "123";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLNumberNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_NumberAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'123'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UnderscoreXyzAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'_xyz_'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_FooAsString_expectInstanceofDFQLStringNode() throws Exception {
        // arrange
        String dfqlQuery = "'foo'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLStringNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_FooAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "foo";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "DF";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_UnderscoreUppercaseDFAsIdentifier_expectInstanceofDFQLIdentifierNode() throws Exception {
        // arrange
        String dfqlQuery = "_DF";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLIdentifierNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelection_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLPrimarySelectionNode.class ) ) );
    }

    @Test
    public void testParseMemberSelection_DataframeColumnSelectionStartsWith_expectPrimarySelectionNode() throws Exception {
        // arrange
        String dfqlQuery = "df.'columnname'.startsWith";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseMemberSelection();

        // assert
        assertThat( result, is( instanceOf( DFQLPrimarySelectionNode.class ) ) );
    }

}
