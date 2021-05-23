package de.mindscan.brightflux.dataframes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import java.util.Collection;
import java.util.UUID;

import org.junit.jupiter.api.Test;

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
        Collection<DataFrameColumn> result = dataFrame.getColumns();

        // assert
        assertThat( result, empty() );
    }

}
