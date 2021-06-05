package de.mindscan.brightflux.ingest.token;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class NumberTokenTest {

    @Test
    public void testGetValue_CreateWithCtorValueZeroString_expectZeroString() throws Exception {
        // arrange
        NumberToken token = new NumberToken( "0" );

        // act
        String result = token.getValue();

        // assert
        assertThat( result, equalTo( "0" ) );
    }

    @Test
    public void testGetValue_CreateWithCtorValueOneString_expectOneString() throws Exception {
        // arrange
        NumberToken token = new NumberToken( "1" );

        // act
        String result = token.getValue();

        // assert
        assertThat( result, equalTo( "1" ) );
    }

    @Test
    public void testGetValue_CreateWithCtorValueOneTwoThreeString_expectOneTwoThreeString() throws Exception {
        // arrange
        NumberToken token = new NumberToken( "123" );

        // act
        String result = token.getValue();

        // assert
        assertThat( result, equalTo( "123" ) );
    }

    @Test
    public void testCreate_CreateWithStaticMethodUsingZeroString_expectZeroString() throws Exception {
        // arrange

        // act
        NumberToken token = NumberToken.create( "0" );

        // assert
        String result = token.getValue();
        assertThat( result, equalTo( "0" ) );
    }

    @Test
    public void testCreate_CreateWithStaticMethodUsingOneString_expectOneString() throws Exception {
        // arrange

        // act
        NumberToken token = NumberToken.create( "1" );

        // assert
        String result = token.getValue();
        assertThat( result, equalTo( "1" ) );
    }

    @Test
    public void testCreate_CreateWithStaticMethodUsingOneTwpoThreeString_expectOneTwoThreeString() throws Exception {
        // arrange

        // act
        NumberToken token = NumberToken.create( "123" );

        // assert
        String result = token.getValue();
        assertThat( result, equalTo( "123" ) );
    }

}
