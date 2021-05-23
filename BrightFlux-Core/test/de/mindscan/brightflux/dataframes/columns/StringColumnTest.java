package de.mindscan.brightflux.dataframes.columns;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

public class StringColumnTest {

    @Test
    public void testStringColumn_DefaultCTor_expectNameIsNull() throws Exception {
        // arrange
        StringColumn stringColumn = new StringColumn();

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testStringColumnString_CTorWithName1_nameIsName1() throws Exception {
        // arrange
        StringColumn stringColumn = new StringColumn( "Name1" );

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name1" ) );
    }

    @Test
    public void testStringColumnString_CTorWithName2_nameIsName2() throws Exception {
        // arrange
        StringColumn stringColumn = new StringColumn( "Name2" );

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name2" ) );
    }

    @Test
    public void testSetColumnName_DefaultCTorThenRenameColumnWithName1_nameIsName1() throws Exception {
        // arrange
        StringColumn stringColumn = new StringColumn();

        // act
        stringColumn.setColumnName( "Name1" );

        // assert
        String result = stringColumn.getColumnName();
        assertThat( result, equalTo( "Name1" ) );
    }

}
