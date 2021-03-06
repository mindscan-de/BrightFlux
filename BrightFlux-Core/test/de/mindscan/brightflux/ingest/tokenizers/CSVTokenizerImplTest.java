package de.mindscan.brightflux.ingest.tokenizers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.ingest.DataToken;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2Impl;
import de.mindscan.brightflux.ingest.token.ColumnSeparatorToken;
import de.mindscan.brightflux.ingest.token.NumberToken;

public class CSVTokenizerImplTest {

    @Test
    public void testTokenize_emptyString_returnsEmptyTokenList() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "" ) );

        // assert
        assertThat( toList( result ), empty() );
    }

    @Test
    public void testTokenize_StringContainingNumber_returnsNonEmptyTokenList() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "1" ) );

        // assert
        assertThat( toList( result ), not( empty() ) );
    }

    @Test
    public void testTokenize_StringContainingSingleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "1" ) );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingDoubleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "12" ) );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingTripleDigitNumber_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "123" ) );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_StringContainingSingleColumnSeparator_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "," ) );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_SetColumnSeparatorSemicolonStringContainingSingleColumnSeparator_returnsTokenListOfLengthOne() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();
        tokenizer.setColumnSeparator( ";" );

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( ";" ) );

        // assert
        assertThat( toList( result ), hasSize( 1 ) );
    }

    @Test
    public void testTokenize_SetColumnSeparatorSemicolonStringContainingSingleColumnSeparator_returnsFirstTokenIsColumnSeparator() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();
        tokenizer.setColumnSeparator( ";" );

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( ";" ) );

        // assert
        assertThat( toList( result ).get( 0 ), equalTo( ColumnSeparatorToken.create() ) );
    }

    @Test
    public void testTokenize_StringContainingSingleColumnSeparator_returnsFirstTokenIsColumnSeparator() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "," ) );

        // assert
        assertThat( toList( result ).get( 0 ), equalTo( ColumnSeparatorToken.create() ) );
    }

    @Test
    public void testTokenize_StringContainingNumberSeparatorNumber_HasSizeThree() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "123,321" ) );

        // assert
        assertThat( toList( result ), hasSize( 3 ) );
    }

    @Test
    public void testTokenize_StringContainingNumberSeparatorNumber_FirstElementIsNumberToken() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "123,321" ) );

        // assert
        assertThat( toList( result ).get( 0 ), instanceOf( NumberToken.class ) );
    }

    @Test
    public void testTokenize_StringContainingNumberSeparatorNumber_SecondElementIsColumnSeparatorToken() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "123,321" ) );

        // assert
        assertThat( toList( result ).get( 1 ), instanceOf( ColumnSeparatorToken.class ) );
    }

    @Test
    public void testTokenize_StringContainingNumberSeparatorNumber_ThirdElementIsNumberToken() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "123,321" ) );

        // assert
        assertThat( toList( result ).get( 2 ), instanceOf( NumberToken.class ) );
    }

    @Test
    public void testTokenize_StringContainingHeadlineOfHeartCsv_Expect27Tokens() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,output" ) );

        // assert
        int __14_COLUMNS_IDENTIFIER = 14;
        int __13_COLUMN_SEPARATORS = 13;

        assertThat( toList( result ), hasSize( __14_COLUMNS_IDENTIFIER + __13_COLUMN_SEPARATORS ) );
    }

    @Test
    public void testTokenize_StringContainingHeadlineAnd3DataLines_Expect15Tokens() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "col1,col2\n1,2\n3,4\n5,6" ) );

        // assert
        int __2_COLUMNS_IDENTIFIER = 2;
        int __4_COLUMN_SEPARATORS = 4;
        int __3_LINE_SEPARATORS = 3;
        int __6_NUMBERS = 6;

        assertThat( toList( result ), hasSize( __2_COLUMNS_IDENTIFIER + __4_COLUMN_SEPARATORS + __3_LINE_SEPARATORS + __6_NUMBERS ) );
    }

    @Test
    public void testTokenize_AspargusDataFormat_() throws Exception {
        // arrange
        CSVTokenizerImpl tokenizer = new CSVTokenizerImpl();

        // act
        Iterator<DataToken> result = tokenizer.tokenize( asDataSource( "Argentina,Area harvested,Asparagus,1961,ha,450" ) );

        // assert
        assertThat( toList( result ), hasSize( 11 ) );
    }

    private List<DataToken> toList( Iterator<DataToken> iterator ) {
        ArrayList<DataToken> result = new ArrayList<DataToken>();
        iterator.forEachRemaining( result::add );
        return result;
    }

    private DataSourceV2Impl asDataSource( String inputString ) {
        DataSourceV2Impl datasource = new DataSourceV2Impl();
        datasource.setInput( inputString );
        return datasource;
    }

}
