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
                        Arguments.of( ColumnTypes.COLUMN_TYPE_SPARSE_INT, SparseIntegerColumn.class )

        );
    }

}
