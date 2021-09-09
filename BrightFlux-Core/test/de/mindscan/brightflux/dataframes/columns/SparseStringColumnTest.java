package de.mindscan.brightflux.dataframes.columns;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameColumn;

public class SparseStringColumnTest {

    @Test
    public void testSparseStringColumn_DefaultCTor_expectNameIsNull() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn();

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, nullValue() );
    }

    @Test
    public void testSparseStringColumnString_CTorWithName1_nameIsName1() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "Name1" );

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name1" ) );
    }

    @Test
    public void testSparseStringColumnString_CTorWithName1_nameIsName2() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "Name2" );

        // act
        String result = stringColumn.getColumnName();

        // assert
        assertThat( result, equalTo( "Name2" ) );
    }

    @Test
    public void testSetColumnName_DefaultCTorThenSetColumnName_expectSameName() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn();

        // act
        stringColumn.setColumnName( "Name1" );

        // assert
        String result = stringColumn.getColumnName();
        assertThat( result, equalTo( "Name1" ) );
    }

    @Test
    public void testCloneColumnEmpty__isInstanceOfSparseStringColumn() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "XYZName" );

        // act
        DataFrameColumn<String> result = stringColumn.cloneColumnEmpty();

        // assert
        assertThat( result, is( instanceOf( SparseStringColumn.class ) ) );
    }

    @Test
    public void testCloneColumnEmpty_NamedSparseColumnXYZName_isInstanceOfSparseStringColumn() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "XYZName" );

        // act
        DataFrameColumn<String> result = stringColumn.cloneColumnEmpty();

        // assert
        assertThat( result, is( instanceOf( SparseStringColumn.class ) ) );
    }

    @Test
    public void testCloneColumnEmpty_NamedSparseColumnXYZName_isEmpty() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "XYZName" );

        // act
        DataFrameColumn<String> result = stringColumn.cloneColumnEmpty();

        // assert
        assertThat( result.isEmpty(), equalTo( true ) );
    }

    @Test
    public void testCloneColumnEmpty_NamedSparseColumnXYZName_ColumnNameIsSameName() throws Exception {
        // arrange
        SparseStringColumn stringColumn = new SparseStringColumn( "XYZName" );

        // act
        DataFrameColumn<String> result = stringColumn.cloneColumnEmpty();

        // assert
        assertThat( result.getColumnName(), equalTo( "XYZName" ) );
    }

}
