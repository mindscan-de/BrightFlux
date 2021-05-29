package de.mindscan.brightflux.ingest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameImpl;

public class IngestHeartCsvTest {

    @Test
    public void testLoadCsvAsDataFrame_loadDataSet_has14Columns() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();

        // act
        DataFrameImpl frame = heartCsv.loadCsvAsDataFrame();

        // assert
        Collection<DataFrameColumn> result = frame.getColumns();
        assertThat ( result , hasSize ( 14 ) );

    }

}
