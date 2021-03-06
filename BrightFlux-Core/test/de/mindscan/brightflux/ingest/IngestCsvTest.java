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

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.dataframes.columns.DoubleColumn;
import de.mindscan.brightflux.dataframes.columns.IntegerColumn;

public class IngestCsvTest {

    private final static Path path = Paths.get(
                    "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart_original.csv" );

    private static final String SPECIAL_INDEX_COLUMN = DataFrameSpecialColumns.INDEX_COLUMN_NAME;
    private static final String SPECIAL_ORIGINAL_INDEX_COLUMN = DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME;

    @Test
    public void testLoadAsDataFrame_loadDaataset_has16Columns() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        Collection<DataFrameColumn<?>> result = frame.getColumns();
        assertThat( result, hasSize( 16 ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_has16ColumnNamesInOrder() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        Collection<String> result = frame.getColumnNames();
        assertThat( result, contains( SPECIAL_INDEX_COLUMN, SPECIAL_ORIGINAL_INDEX_COLUMN, "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh",
                        "exng", "oldpeak", "slp", "caa", "thall", "output" ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_has15IntColumns() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        Collection<String> result = frame.getColumns().stream().filter( c -> IntegerColumn.class.isInstance( c ) ).map( c -> c.getColumnName() )
                        .collect( Collectors.toList() );

        assertThat( result, contains( SPECIAL_INDEX_COLUMN, SPECIAL_ORIGINAL_INDEX_COLUMN, "age", "sex", "cp", "trtbps", "chol", "fbs", "restecg", "thalachh",
                        "exng", "slp", "caa", "thall", "output" ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_has1DoubleColumns() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        Collection<String> result = frame.getColumns().stream().filter( c -> DoubleColumn.class.isInstance( c ) ).map( c -> c.getColumnName() )
                        .collect( Collectors.toList() );

        assertThat( result, contains( "oldpeak" ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_FirstColumnIsNotEmpty() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        DataFrameColumn<?> result = frame.getColumns().iterator().next();

        assertThat( result.getSize(), Matchers.greaterThan( 0 ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_FirstColumnHas303Elements() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        DataFrameColumn<?> result = frame.getColumns().iterator().next();

        assertThat( result.getSize(), equalTo( 303 ) );
    }

    @Test
    public void testLoadAsDataFrame_loadDataSet_All16ColumnsHave303Elements() throws Exception {
        // arrange
        IngestCsv heartCsv = new IngestCsv();

        // act
        DataFrame frame = heartCsv.loadAsDataFrame( path );

        // assert
        List<Integer> result = frame.getColumns().stream().map( c -> c.getSize() ).collect( Collectors.toList() );

        assertThat( result, contains( 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303, 303 ) );
    }

}
