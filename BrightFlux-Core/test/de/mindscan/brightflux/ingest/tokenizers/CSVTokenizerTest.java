package de.mindscan.brightflux.ingest.tokenizers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.ingest.DataToken;

public class CSVTokenizerTest {

    @Test
    public void testTokenize_emptyString_returnsEmptyTokenList() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "" );

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testTokenize_StringContainingNumber_returnsNonEmptyTokenList() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "1" );

        // assert
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testTokenize_StringContainingSingleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "1" );

        // assert
        assertThat( result, hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingDoubleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "12" );

        // assert
        assertThat( result, hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingTripleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "123" );

        // assert
        assertThat( result, hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingSingleColumnSeparator_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();

        // act
        Collection<DataToken> result = tokenizer.tokenize( "," );

        // assert
        assertThat( result, hasSize( 1 ) );
    }

    @Test
    public void testTokenize_SetColumnSeparatorSemicolonStringContainingSingleColumnSeparator_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizer tokenizer = new CSVTokenizer();
        tokenizer.setColumnSeparator( ";" );

        // act
        Collection<DataToken> result = tokenizer.tokenize( ";" );

        // assert
        assertThat( result, hasSize( 1 ) );
    }

}
