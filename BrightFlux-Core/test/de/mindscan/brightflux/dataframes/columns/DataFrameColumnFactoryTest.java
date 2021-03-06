package de.mindscan.brightflux.dataframes.columns;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.columntypes.ColumnTypes;
import de.mindscan.brightflux.dataframes.columntypes.ColumnValueTypes;

public class DataFrameColumnFactoryTest {

    @ParameterizedTest
    @MethodSource( "provideTypeExpectedType" )
    public void testCreateColumnForTypeString_givenColumnType_expectExpectedColumnType( String type, Class<?> expectedColumnType ) throws Exception {
        // arrange

        // act
        DataFrameColumn<?> result = DataFrameColumnFactory.createColumnForType( type );

        // assert
        assertThat( result, is( instanceOf( expectedColumnType ) ) );
    }

    @ParameterizedTest
    @MethodSource( "provideColumnTypeColumnType" )
    public void testCreateColumnForTypeString_givenColumnType_reportsSameOrEquivalentColumnType( String type, String expectedColumnType ) throws Exception {
        // arrange

        // act
        String result = DataFrameColumnFactory.createColumnForType( type ).getColumnType();

        // assert
        assertThat( result, is( expectedColumnType ) );
    }

    @ParameterizedTest
    @MethodSource( "provideColumnTypeColumnValueType" )
    public void testCreateColumnForTypeString_givenColumnType_reportsSameOrEquivalentCalueType( String type, String expectedValueType ) throws Exception {
        // arrange

        // act
        String result = DataFrameColumnFactory.createColumnForType( type ).getColumnValueType();

        // assert
        assertThat( result, is( expectedValueType ) );
    }

    @ParameterizedTest
    @MethodSource( "provideTypeExpectedType" )
    public void testCreateColumnForTypeStringString_givenColumnType_expectExpectedColumnType( String type, Class<?> expectedColumnType ) throws Exception {
        // arrange

        // act
        DataFrameColumn<?> result = DataFrameColumnFactory.createColumnForType( "someName", type );

        // assert
        assertThat( result, is( instanceOf( expectedColumnType ) ) );
    }

    @ParameterizedTest
    @MethodSource( "provideTypeExpectedType" )
    public void testCreateColumnForTypeStringString_givenColumnTypeAndSomeName_columnNameIsSomeName( String type, Class<?> ignoredColumnType )
                    throws Exception {
        // arrange

        // act
        String result = DataFrameColumnFactory.createColumnForType( "someName", type ).getColumnName();

        // assert
        assertThat( result, is( "someName" ) );
    }

    @ParameterizedTest
    @MethodSource( "provideTypeExpectedType" )
    public void testCreateColumnForTypeStringString_givenColumnTypeAndSomeOtherName_columnNameIsSomeOtherName( String type, Class<?> ignoredColumnType )
                    throws Exception {
        // arrange

        // act
        String result = DataFrameColumnFactory.createColumnForType( "someOtherName", type ).getColumnName();

        // assert
        assertThat( result, is( "someOtherName" ) );
    }

    @Test
    public void testCreateColumnForTypeString_unsupportedColumnType_expectThrowsIllegalArgumentException() throws Exception {
        // arrange

        // act
        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            DataFrameColumn<?> result = DataFrameColumnFactory.createColumnForType( "unsupportedType" );
        } );
    }

    @Test
    public void testCreateColumnForTypeStringString_unsupportedColumnTypeWithSomeName_expectThrowsIllegalArgumentException() throws Exception {
        // arrange

        // act
        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            DataFrameColumn<?> result = DataFrameColumnFactory.createColumnForType( "someName", "unsupportedType" );
        } );
    }

    private static Stream<Arguments> provideTypeExpectedType() {
        return Stream.of( //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INT, IntegerColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INTEGER, IntegerColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_LONG, LongColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_FLOAT, FloatColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_DOUBLE, DoubleColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOL, BooleanColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOLEAN, BooleanColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_STRING, StringColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_STRING, SparseStringColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_INT, SparseIntegerColumn.class ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_LONG, SparseLongColumn.class ) );
    }

    private static Stream<Arguments> provideColumnTypeColumnType() {
        return Stream.of( //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INT, ColumnTypes.COLUMN_TYPE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INTEGER, ColumnTypes.COLUMN_TYPE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_LONG, ColumnTypes.COLUMN_TYPE_LONG ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_FLOAT, ColumnTypes.COLUMN_TYPE_FLOAT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_DOUBLE, ColumnTypes.COLUMN_TYPE_DOUBLE ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOL, ColumnTypes.COLUMN_TYPE_BOOL ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOLEAN, ColumnTypes.COLUMN_TYPE_BOOL ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_STRING, ColumnTypes.COLUMN_TYPE_STRING ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_STRING, ColumnTypes.COLUMN_TYPE_SPARSE_STRING ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_INT, ColumnTypes.COLUMN_TYPE_SPARSE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_LONG, ColumnTypes.COLUMN_TYPE_SPARSE_LONG ) );
    }

    private static Stream<Arguments> provideColumnTypeColumnValueType() {
        return Stream.of( //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INT, ColumnValueTypes.COLUMN_TYPE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_INTEGER, ColumnValueTypes.COLUMN_TYPE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_LONG, ColumnValueTypes.COLUMN_TYPE_LONG ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_FLOAT, ColumnValueTypes.COLUMN_TYPE_FLOAT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_DOUBLE, ColumnValueTypes.COLUMN_TYPE_DOUBLE ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOL, ColumnValueTypes.COLUMN_TYPE_BOOL ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_BOOLEAN, ColumnValueTypes.COLUMN_TYPE_BOOL ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_STRING, ColumnValueTypes.COLUMN_TYPE_STRING ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_STRING, ColumnValueTypes.COLUMN_TYPE_STRING ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_INT, ColumnValueTypes.COLUMN_TYPE_INT ), //
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_LONG, ColumnValueTypes.COLUMN_TYPE_LONG ) );
    }

}
