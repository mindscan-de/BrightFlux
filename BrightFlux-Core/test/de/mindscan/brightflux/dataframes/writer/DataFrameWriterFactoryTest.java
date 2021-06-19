package de.mindscan.brightflux.dataframes.writer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameImpl;
import de.mindscan.brightflux.ingest.IngestHeartCsv;

public class DataFrameWriterFactoryTest {

    private final static Path inputPath = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );
    private final static Path outputPath = Paths.get(
                    "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart_output.csv" );

    @Test
    public void testCreate_CSV_returnsInstanceOfDataFrameWriter() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();
        DataFrameImpl df = heartCsv.loadCsvAsDataFrameV2( inputPath );

        // act
        DataFrameWriter result = DataFrameWriterFactory.create( "CSV" );

        // assert
        assertThat( result, is( instanceOf( DataFrameWriter.class ) ) );
    }

    @Test
    public void testCreate_() throws Exception {
        // arrange
        IngestHeartCsv heartCsv = new IngestHeartCsv();
        DataFrameImpl df = heartCsv.loadCsvAsDataFrameV2( inputPath );

        // act
        DataFrameWriter writer = DataFrameWriterFactory.create( "CSV" );

        // assert -  we should compare the content of the input file to the output file
        writer.writeToFile( df, outputPath );
    }

}
