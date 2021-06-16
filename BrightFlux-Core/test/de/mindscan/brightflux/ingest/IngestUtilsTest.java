package de.mindscan.brightflux.ingest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class IngestUtilsTest {

    @Test
    public void testCalculateColumnSeparator_ThreeFirstLinesOfHeartCsv_returnsCommaAsSeparator() throws Exception {
        // arrange
        String head[] = { "age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,output", //
                        "63,1,3,145,233,1,0,150,0,2.3,0,0,1,1", "37,1,2,130,250,0,1,187,0,3.5,0,0,2,1" };

        // act
        String columnSeparator = IngestUtils.calculateColumnSeparator( head );

        // assert
        assertThat( columnSeparator, equalTo( "," ) );
    }

    @Test
    public void testCalculateNumberOfColumns_ThreeFirstLinesOfHeartCsv_returns14() throws Exception {
        // arrange
        String head[] = { "age,sex,cp,trtbps,chol,fbs,restecg,thalachh,exng,oldpeak,slp,caa,thall,output", //
                        "63,1,3,145,233,1,0,150,0,2.3,0,0,1,1", "37,1,2,130,250,0,1,187,0,3.5,0,0,2,1" };

        // act
        int numberOfColumns = IngestUtils.calculateNumberOfColumns( head, ',' );

        // assert
        assertThat( numberOfColumns, equalTo( 14 ) );
    }

}
