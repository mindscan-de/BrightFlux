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

    @Test
    public void testStringColumnStringStringArray_SetNameEmptyNamesEmptyStringArray_expextNameIsEmptyNames() throws Exception {
        // arrange
        StringColumn stringColumn = new StringColumn( "EmptyNames", new String[0] );

        // act
        String resuilt = stringColumn.getColumnName();

        // assert
        assertThat( resuilt, equalTo( "EmptyNames" ) );
    }

    @Test
    public void testIsEmpty_DefaultCTor_ExpectTrue() {
        // Arrange
        StringColumn stringColumn = new StringColumn();

        // Act
        boolean result = stringColumn.isEmpty();

        // Assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsEmpty_ConstructorWithEmptyArray_ExpectTrue() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[0] );

        // Act
        boolean result = stringColumn.isEmpty();

        // Assert
        assertThat( result, equalTo( true ) );
    }

    @Test
    public void testIsEmpty_ConstructorWithNonEmptyArray_ExpectFalse() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );

        // Act
        boolean result = stringColumn.isEmpty();

        // Assert
        assertThat( result, equalTo( false ) );
    }

    @Test
    public void testAt_ConstructorWithNonEmptyArrayContainingValue1At0_ExpectValue1() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );

        // Act
        String result = stringColumn.at( 0 );

        // Assert
        assertThat( result, equalTo( "Value1" ) );
    }

    @Test
    public void testAt_ConstructorWithNonEmptyArrayContainingTwoValues_ExpectValue1At0() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1", "Value2" } );

        // Act
        String result = stringColumn.at( 0 );

        // Assert
        assertThat( result, equalTo( "Value1" ) );
    }

    @Test
    public void testAt_ConstructorWithNonEmptyArrayContainingValue2At1_ExpectValue2() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1", "Value2" } );

        // Act
        String result = stringColumn.at( 1 );

        // Assert
        assertThat( result, equalTo( "Value2" ) );
    }

    @Test
    public void testGetSize_DefaultConstructorOnly_expectZero() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName" );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithEmptyArray_expectZero() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[0] );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithArrayLengthOne_expectOne() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithArrayLengthTwo_expectTwo() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1", "Value2" } );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithEmptyArrayAndAppendOneValue_expectOne() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[0] );
        stringColumn.append( "Value1" );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithArraySizeOneAndAppendOneValue_expectTwo() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );
        stringColumn.append( "Value2" );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testGetSize_DefaultConstructorWithEmptyArrayAndAppendTwoValues_expectTwo() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName" );
        stringColumn.append( "Value1" );
        stringColumn.append( "Value2" );

        // Act
        int result = stringColumn.getSize();

        // Assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testGet_DefaultConstructorWithTwoValuesGetAtIndexZero_expectValue1() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1", "Value2" } );

        // Act
        String result = stringColumn.get( 0 );

        // Assert
        assertThat( result, equalTo( "Value1" ) );
    }

    @Test
    public void testGet_DefaultConstructorWithTwoValuesGetAtIndexOne_expectValue2() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1", "Value2" } );

        // Act
        String result = stringColumn.get( 1 );

        // Assert
        assertThat( result, equalTo( "Value2" ) );
    }

    @Test
    public void testAppend_DefaultConstructorAppenValue1AndGetFirst_expectValue1() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName" );

        // Act
        stringColumn.append( "Value1" );

        // Assert
        String result = stringColumn.get( 0 );
        assertThat( result, equalTo( "Value1" ) );
    }

    @Test
    public void testAppend_DefaultConstructorAppendTwoValuesAndGetecond_expectValue2() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName" );

        // Act
        stringColumn.append( "Value1" );
        stringColumn.append( "Value2" );

        // Assert
        String result = stringColumn.get( 1 );
        assertThat( result, equalTo( "Value2" ) );
    }

    @Test
    public void testAppend_DefaultConstructorWithEmptyArrayAppendTwoValuesAndGetSecond_expectValue2() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[0] );

        // Act
        stringColumn.append( "Value1" );
        stringColumn.append( "Value2" );

        // Assert
        String result = stringColumn.get( 1 );
        assertThat( result, equalTo( "Value2" ) );
    }

    @Test
    public void testAppend_DefaultConstructorWithArrayLengthOneAppendOneValueAndGetSecond_expectValue2() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );

        // Act
        stringColumn.append( "Value2" );

        // Assert
        String result = stringColumn.get( 1 );
        assertThat( result, equalTo( "Value2" ) );
    }

    @Test
    public void testAppend_DefaultConstructorWithArrayLengthOneAppendOneValueAndGetFirst_expectValue1() {
        // Arrange
        StringColumn stringColumn = new StringColumn( "AnyName", new String[] { "Value1" } );

        // Act
        stringColumn.append( "Value2" );

        // Assert
        String result = stringColumn.get( 0 );
        assertThat( result, equalTo( "Value1" ) );
    }

    // tpxu_method

}
