/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.brightflux.dataframes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.ingest.IngestHeartCsv;

/**
 * 
 */
public class DataFrameImplFilterTest {

    private final static Path path = Paths
                    .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart.csv" );

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAnyElement_expect303Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.any() );

        // assert
        assertThat( result, hasSize( 303 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIs0_expectZeroRows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.eq( "age", 0 ) );

        // assert
        assertThat( result, hasSize( 0 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIs50_expect7Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.eq( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 7 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsNot50_expect296Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.neq( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 296 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsOver50_expect208Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.gt( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 208 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsOver50OrEqual_expect215Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.ge( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 208 + 7 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsUnder50_expect88Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.lt( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 88 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsUnder50OrEqual_expect95Rows() throws Exception {
        // arrange
        IngestHeartCsv csvReader = new IngestHeartCsv();
        DataFrame df = csvReader.loadCsvAsDataFrameV2( path );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.le( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 88 + 7 ) );
    }

}
