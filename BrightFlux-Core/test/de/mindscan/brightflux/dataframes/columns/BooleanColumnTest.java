package de.mindscan.brightflux.dataframes.columns;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

public class BooleanColumnTest {

    @Test
    public void testBooleanColumn_DefaultCTor_expectNameIsNull() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn();

        // act
        String result = booleanColumn.getColumnName();

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testBooleanColumnString_CTorWithName1_nameIsName1() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn( "Name1" );

        // act
        String result = booleanColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name1" ) );
    }

    @Test
    public void testStringColumnString_CTorWithName2_nameIsName2() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn( "Name2" );

        // act
        String result = booleanColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name2" ) );
    }

    @Test
    public void testSetColumnName_DefaultCTorThenRenameColumnWithName1_nameIsName1() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn();

        // act
        booleanColumn.setColumnName( "Name1" );

        // assert
        String result = booleanColumn.getColumnName();
        assertThat( result, equalTo( "Name1" ) );
    }

}
