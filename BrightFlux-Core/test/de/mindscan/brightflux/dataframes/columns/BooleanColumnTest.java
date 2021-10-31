package de.mindscan.brightflux.dataframes.columns;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;

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

    @Test
    public void testGetColumnType_DefaultCtor_reportsIsColumnTypeBool() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn();

        // act
        String result = booleanColumn.getColumnType();

        // assert
        assertThat( result, equalTo( ColumnTypes.COLUMN_TYPE_BOOL ) );
    }

    @Test
    public void testGetColumnValueType_DefaultCtor_reportsColumnValueTypeBool() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn();

        // act
        String result = booleanColumn.getColumnValueType();

        // assert
        assertThat( result, equalTo( ColumnValueTypes.COLUMN_TYPE_BOOL ) );
    }

    @Test
    public void testCloneColumnEmpty_DefaultCtor_IsNotSameInstance() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn();

        // act
        DataFrameColumn<Boolean> result = booleanColumn.cloneColumnEmpty();

        // assert
        assertThat( result, not( sameInstance( booleanColumn ) ) );
    }

    @Test
    public void testCloneColumnEmpty_DefaultCtorHavingMyName_ColumnHasMyName() throws Exception {
        // arrange
        BooleanColumn booleanColumn = new BooleanColumn( "myName" );

        // act
        String result = booleanColumn.cloneColumnEmpty().getColumnName();

        // assert
        assertThat( result, equalTo( "myName" ) );
    }

}
