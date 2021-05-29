package de.mindscan.brightflux.dataframes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.columns.BooleanColumn;

public class DataFrameImplTest {

    @Test
    public void testGetName_CtorWithNameOnlySetFirstName_expectFirstName() throws Exception {
        // arrange
        DataFrameImpl dataFrameImpl = new DataFrameImpl( "FirstName" );

        // act
        String result = dataFrameImpl.getName();

        // assert
        assertThat( result, equalTo( "FirstName" ) );
    }

    @Test
    public void testGetName_CtorWithNameOnlySetSecondName_expectSecondName() throws Exception {
        // arrange
        DataFrameImpl dataFrameImpl = new DataFrameImpl( "SecondName" );

        // act
        String result = dataFrameImpl.getName();

        // assert
        assertThat( result, equalTo( "SecondName" ) );
    }

    @Test
    public void testGetName_CtorWithNameAndUUIDSetFirstName_expectFirstName() throws Exception {
        // arrange
        DataFrameImpl dataFrameImpl = new DataFrameImpl( "FirstName", UUID.randomUUID() );

        // act
        String result = dataFrameImpl.getName();

        // assert
        assertThat( result, equalTo( "FirstName" ) );
    }

    @Test
    public void testGetName_CtorWithNameAndUUIDSetSecondName_expectSecondName() throws Exception {
        // arrange
        DataFrameImpl dataFrameImpl = new DataFrameImpl( "SecondName", UUID.randomUUID() );

        // act
        String result = dataFrameImpl.getName();

        // assert
        assertThat( result, equalTo( "SecondName" ) );
    }

    @Test
    public void testGetName_CtorWithNameAndUUIDSetRandomUUID_expectSameUUID() throws Exception {
        // arrange
        UUID expectedUUID = UUID.randomUUID();
        DataFrameImpl dataFrameImpl = new DataFrameImpl( "AnyName", expectedUUID );

        // act
        UUID result = dataFrameImpl.getUuid();

        // assert
        assertThat( result, equalTo( expectedUUID ) );
    }

    @Test
    public void testGetColumns_DefaultCTor_ColumnsListIsEmpty() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        Collection<DataFrameColumn<?>> result = dataFrame.getColumns();

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testGetColumnNames_DefaultCTor_ColumnsListIsEmpty() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        Collection<String> result = dataFrame.getColumnNames();

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testAddColumn_DefaultCTorThenAddOneColumn_ColumnsListNotEmpty() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        dataFrame.addColumn( new BooleanColumn( "Column1" ) );

        // assert
        Collection<DataFrameColumn<?>> result = dataFrame.getColumns();
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testGetColumns_DefaultCTorThenAddOneColumn_ColumnsListContainsSameBoolean() throws Exception {
        // arrange
        BooleanColumn booleanColumn1 = new BooleanColumn( "Column1" );

        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        dataFrame.addColumn( booleanColumn1 );

        // act
        Collection<DataFrameColumn<?>> result = dataFrame.getColumns();

        // assert
        assertThat( result, contains( booleanColumn1 ) );
    }

    @Test
    public void testGetColumns_DefaultCTorThenAddThreeColumn_ColumnsListContainsSameBooleanColumns() throws Exception {
        // arrange
        BooleanColumn booleanColumn1 = new BooleanColumn( "Column1" );
        BooleanColumn booleanColumn2 = new BooleanColumn( "Column2" );
        BooleanColumn booleanColumn3 = new BooleanColumn( "Column3" );

        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        dataFrame.addColumn( booleanColumn1 );
        dataFrame.addColumn( booleanColumn2 );
        dataFrame.addColumn( booleanColumn3 );

        // act
        Collection<DataFrameColumn<?>> result = dataFrame.getColumns();

        // assert
        assertThat( result, contains( booleanColumn1, booleanColumn2, booleanColumn3 ) );
    }

    @Test
    public void testGetColumns_DefaultCTorThenAddThreeColumnOneDuplicate_ColumnsListContainsTwoBooleanColumns() throws Exception {
        // arrange
        BooleanColumn booleanColumn1 = new BooleanColumn( "Column1" );
        BooleanColumn booleanColumn2 = new BooleanColumn( "Column2" );

        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        dataFrame.addColumn( booleanColumn1 );
        dataFrame.addColumn( booleanColumn2 );
        dataFrame.addColumn( booleanColumn1 );

        // act
        Collection<DataFrameColumn<?>> result = dataFrame.getColumns();

        // assert
        assertThat( result, contains( booleanColumn1, booleanColumn2 ) );
    }

    @Test
    public void testAddColumn_DefaultCTorThenAddOneColumn_ColumnNamesListNotEmpty() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        dataFrame.addColumn( new BooleanColumn( "Column1" ) );

        // assert
        Collection<String> result = dataFrame.getColumnNames();
        assertThat( result, not( empty() ) );
    }

    @Test
    public void testGetColumnNames_DefaultCTorThenAddOneColumn_ColumnNamesListContainsColumn1() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        dataFrame.addColumn( new BooleanColumn( "Column1" ) );

        // act
        Collection<String> result = dataFrame.getColumnNames();

        // assert
        assertThat( result, contains( "Column1" ) );
    }

    @Test
    public void testGetColumnNames_DefaultCTorThenAddThreeColumns_ColumnNamesListContainsColumn1ToColumn3() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        dataFrame.addColumn( new BooleanColumn( "Column1" ) );
        dataFrame.addColumn( new BooleanColumn( "Column2" ) );
        dataFrame.addColumn( new BooleanColumn( "Column3" ) );

        // act
        Collection<String> result = dataFrame.getColumnNames();

        // assert
        assertThat( result, contains( "Column1", "Column2", "Column3" ) );
    }

}
