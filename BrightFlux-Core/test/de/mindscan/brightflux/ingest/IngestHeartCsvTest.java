package de.mindscan.brightflux.ingest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.dataframes.columns.DoubleColumn;
import de.mindscan.brightflux.dataframes.columns.IntegerColumn;

public class IngestHeartCsvTest {

    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );

    @Test
    public void testLoadCsvAsDataFrameV2_loadDaataset_has14Columns() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        Collection<DataFrameColumn<?>> result = frame.getColumns();
        assertThat( result, hasSize( 14 ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_has14ColumnNamesInOrder() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        Collection<String> result = frame.getColumnNames();
        assertThat( result,
                        contains( "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh", "exng", "oldpeak", "slp", "caa", "thall", "output" ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_has13IntColumns() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        Collection<String> result = frame.getColumns().stream().filter( c -> IntegerColumn.class.isInstance( c ) ).map( c -> c.getColumnName() )
                        .collect( Collectors.toList() );

        assertThat( result, contains( "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh", "exng", "slp", "caa", "thall", "output" ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_has1DoubleColumns() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        Collection<String> result = frame.getColumns().stream().filter( c -> DoubleColumn.class.isInstance( c ) ).map( c -> c.getColumnName() )
                        .collect( Collectors.toList() );

        assertThat( result, contains( "oldpeak" ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_FirstColumnIsNotEmpty() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        DataFrameColumn<?> result = frame.getColumns().iterator().next();

        assertThat( result.getSize(), Matchers.greaterThan( 0 ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_FirstColumnHas303Elements() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        DataFrameColumn<?> result = frame.getColumns().iterator().next();

        assertThat( result.getSize(), equalTo( 303 ) );
    }

    @Test
    public void testLoadCsvAsDataFrameV2_loadDataSet_All14ColumnHas303Elements() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        // assert
        List<Integer> result = frame.getColumns().stream().map( c -> c.getSize() ).collect( Collectors.toList() );

        assertThat( result, contains( 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303 ) );
    }

}
