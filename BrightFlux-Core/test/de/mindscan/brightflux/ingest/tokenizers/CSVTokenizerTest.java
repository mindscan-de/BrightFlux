package de.mindscan.brightflux.ingest.tokenizers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
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

}
