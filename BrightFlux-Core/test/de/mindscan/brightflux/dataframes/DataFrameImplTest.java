package de.mindscan.brightflux.dataframes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.columns.BooleanColumn;
import de.mindscan.brightflux.ingest.IngestHeartCsv;

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

    @Test
    public void testSetName_CTorWithName1ThenSetName2_expectSetName2() throws Exception {
        // arrange
        DataFrameImpl dataFrame = new DataFrameImpl( "Name1" );

        // act
        dataFrame.setName( "Name2" );

        // assert
        String result = dataFrame.getName();
        assertThat( result, equalTo( "Name2" ) );
    }

    @Test
    public void testAddColumn_addFirstColumnEmpty_expectThrowsNoException() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        dataFrame.addColumn( new BooleanColumn( "Column1" ) );

        // assert (no exception is thrown)
    }

    @Test
    public void testAddColumn_addFirstColumnLength1_expectThrowsNoException() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );

        // act
        dataFrame.addColumn( column );

        // assert (no exception is thrown)
    }

    @Test
    public void testGetSize_CTorOnly_expectSizeIsZero() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );

        // act
        int result = dataFrame.getSize();

        // assert
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testGetSize_addFirstColumnEmpty_expectSizeIsZero() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        dataFrame.addColumn( column );

        // act
        int result = dataFrame.getSize();

        // assert
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testGetSize_addFirstColumnLength1_expectSizeIsOne() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );
        dataFrame.addColumn( column );

        // act
        int result = dataFrame.getSize();

        // assert (no exception is thrown)
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetSize_addFirstColumnLengthTwo_expectSizeIsTwo() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );

        column.append( true );
        column.append( true );

        dataFrame.addColumn( column );

        // act
        int result = dataFrame.getSize();

        // assert (no exception is thrown)
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testGetSize_addFirstColumnLengthIsOneSecondIsEmpty_expectSizeIsOne() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );

        BooleanColumn emptyColumn = new BooleanColumn( "Column2" );

        dataFrame.addColumn( column );
        dataFrame.addColumn( emptyColumn );

        // act
        int result = dataFrame.getSize();

        // assert 
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetSize_addFirstColumnLengthIsTwoSecondIsEmpty_expectSizeIsTwo() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );
        column.append( false );

        BooleanColumn emptyColumn = new BooleanColumn( "Column2" );

        dataFrame.addColumn( column );
        dataFrame.addColumn( emptyColumn );

        // act
        int result = dataFrame.getSize();

        // assert 
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testAddColumn_addFirstColumnLengthOneAndSecondOneEmpty_expectSecondColumnToHaveSizeOne() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );
        BooleanColumn secondColumn = new BooleanColumn( "Column2" );

        // act
        dataFrame.addColumn( column );
        dataFrame.addColumn( secondColumn );

        // assert 
        assertThat( secondColumn.getSize(), equalTo( 1 ) );
    }

    @Test
    public void testAddColumn_addFirstColumnLengthTwoAndSecondOneEmpty_expectSecondColumnToHaveSizeTwo() throws Exception {

        // arrange 
        DataFrameImpl dataFrame = new DataFrameImpl( "MyDataFrame" );
        BooleanColumn column = new BooleanColumn( "Column1" );
        column.append( true );
        column.append( false );
        BooleanColumn secondColumn = new BooleanColumn( "Column2" );

        // act
        dataFrame.addColumn( column );
        dataFrame.addColumn( secondColumn );

        // assert 
        assertThat( secondColumn.getSize(), equalTo( 2 ) );
    }

    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );

    @Test
    public void testHead_IngestHeartCsv_expect7Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        DataFrame dfResult = df.head();

        // assert
        int resultSize = dfResult.getSize();
        assertThat( resultSize, equalTo( DataFrameImpl.DEFAULT_HEAD_ROW_COUNT ) );
    }

    @Test
    public void testTail_IngestHeartCsv_expect7Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        DataFrame dfResult = df.tail();

        // assert
        int resultSize = dfResult.getSize();
        assertThat( resultSize, equalTo( DataFrameImpl.DEFAULT_TAIL_ROW_COUNT ) );
    }

}
