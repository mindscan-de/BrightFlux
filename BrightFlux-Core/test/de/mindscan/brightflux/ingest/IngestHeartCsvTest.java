package de.mindscan.brightflux.ingest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameImpl;

public class IngestHeartCsvTest {

    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );

    @Test
    public void testLoadCsvAsDataFrame_loadDataSet_has14Columns() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrame( path );

        // assert
        Collection<DataFrameColumn<?>> result = frame.getColumns();
        assertThat( result, hasSize( 14 ) );
    }

    @Test
    public void testLoadCsvAsDataFrame_loadDataSet_has14ColumnNamesInOrder() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrame( path );

        // assert
        Collection<String> result = frame.getColumnNames();
        assertThat( result,
                        contains( "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh", "exng", "oldpeak", "slp", "caa", "thall", "output" ) );

    }

    @Test
    public void testLoadCsvAsDataFrameV2() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrameV2( path );

        Collection<DataFrameColumn<?>> result = frame.getColumns();
        assertThat( result, hasSize( 14 ) );
    }

}
