package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;

public class DataFrameQueryLanguageTokenizerTest {

    @Test
    public void testTokenize_UserNullInput_emptyIterator() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( null );

        // assert
        assertThat( toList( result ), empty() );
    }

    @Test
    public void testTokenize_emptyString_emptyIterator() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "" );

        // assert
        assertThat( toList( result ), empty() );
    }

    @Test
    public void testTokenize_WhiteSpaceOneTab_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "\t" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_WhiteSpaceTwoTabsCombinedWhiteSpace_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "\t\t" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_AllWhiteSpacesCombined_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( " \t\t \r\n  \n\r  " );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_OpenParenthesis_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "(" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_CloseParenthesis_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( ")" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_OpenParenthesisCloseParenthesis_hasSizeTwo() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "()" );

        // assert
        assertThat( toList( result ), hasSize( 2 ) );
    }

    @Test
    public void testTokenize_NumberWithLengthOne_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "0" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_NumberWithLengthTwo_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "10" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_NumberWithLengthThree_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "210" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_NumberWithLengthFour_hasSizeOne() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "3210" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    // TODO: the other numbers 43210, 543210, 6543210, 76543210, 876543210, 9876543210

    @Test
    public void testTokenize_10NumberWith9Spaces_hasSize19() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "9 8 7 6 5 4 3 2 1 0" );

        // assert
        assertThat( toList( result ), hasSize( 19 ) );
    }

    @Test
    public void testTokenize_OnlyOneOperatorSize2Equals_hasSize1() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "==" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_OnlyOneOperatorSize2NotEquals_hasSize1() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "!=" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_OnlyOneOperatorSize2LessOrEqual_hasSize1() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( "<=" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_OnlyOneOperatorSize2GreaterOrEqual_hasSize1() throws Exception {
        // arrange
        DataFrameQueryLanguageTokenizer tokenizer = new DataFrameQueryLanguageTokenizer();

        // act
        Iterator<DFQLToken> result = tokenizer.tokenize( ">=" );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    private List<DFQLToken> toList( Iterator<DFQLToken> iterator ) {
        ArrayList<DFQLToken> result = new ArrayList<DFQLToken>();
        iterator.forEachRemaining( result::add );
        return result;
    }

}