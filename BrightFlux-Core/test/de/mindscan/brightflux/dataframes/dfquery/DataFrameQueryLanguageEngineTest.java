package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrame;

public class DataFrameQueryLanguageEngineTest {

    @Test
    public void testExecuteDFTokenize_TokenizeStatementExtractColumnNames_expectOrgIdxAndTimestamoAsColumnnamesToTransfer() throws Exception {
        // arrange
        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
        JobRecorder recorder = new JobRecorder();

        // act
        DataFrame df = null;
        engine.executeDFTokenize( df, "SELECT df.'__org_idx', df.'h1.ts' TOKENIZE df.'h1.msg' USING 'myjobName' FROM df", recorder::jobRecorder );

        // assert
        String[] result = recorder.getRecordedjob().getColumnNamesToTransfer();
        assertThat( result, equalTo( new String[] { "__org_idx", "h1.ts" } ) );
    }

}
