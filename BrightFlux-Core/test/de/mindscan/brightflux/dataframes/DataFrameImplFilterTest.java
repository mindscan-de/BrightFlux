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

import de.mindscan.brightflux.DataFrameTestUtils;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;

/**
 * 
 */
public class DataFrameImplFilterTest {

    private final static Path heartCSV = Paths.get(
                    "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\heart_original.csv" );

    private final static Path aspargusCSV = Paths.get(
                    "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\FAOSTAT_data_7-10-2021.csv" );

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAnyElement_expect303Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.any() );

        // assert
        assertThat( result, hasSize( 303 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIs0_expectZeroRows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.eq( "age", 0 ) );

        // assert
        assertThat( result, hasSize( 0 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIs50_expect7Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.eq( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 7 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsOneDotTwo_expect17Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.eq( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 17 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsNot50_expect296Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.neq( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 296 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsNotOneDotTwo_expect286Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.neq( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 286 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsOver50_expect208Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.gt( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 208 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsGreaterThanOneDotTwo_expect104Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.gt( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 104 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsOver50OrEqual_expect215Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.ge( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 208 + 7 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsGreaterOrEqualToOneDotTwo_expect121Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.ge( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 104 + 17 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsUnder50_expect88Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.lt( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 88 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsLessThanOneDotTwo_expect182Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.lt( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 182 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterAgeIsUnder50OrEqual_expect95Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.le( "age", 50 ) );

        // assert
        assertThat( result, hasSize( 88 + 7 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadHeartDatasetFilterOldpeakIsLessThanOrEqualOneDotTwo_expect199Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.le( "oldpeak", 1.2d ) );

        // assert
        assertThat( result, hasSize( 182 + 17 ) );
    }

    @Test
    public void testGetRowsByPredicate_LoadAspargusDatasetAreaColumnContainsINA_expect1000Rows() throws Exception {
        // arrange
        DataFrame df = DataFrameTestUtils.loadCSV( heartCSV );

        // act
        Collection<DataFrameRow> result = df.getRowsByPredicate( DataFrameRowFilterPredicateFactory.containsStr( "Area", "ina" ) );

        // assert
        assertThat( result, hasSize( 1000 ) );
    }

}
