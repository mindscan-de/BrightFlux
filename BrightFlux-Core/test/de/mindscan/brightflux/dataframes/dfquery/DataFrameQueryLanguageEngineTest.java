package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.dataframes.DataFrame;

public class DataFrameQueryLanguageEngineTest {

    @Test
    public void testExecuteDFTokenize_TokenizeStatementExtractColumnNames_expectOrgIdxAndTimestamoAsColumnnamesToTransfer() throws Exception {
        // arrange
        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
        JobRecorder recorder = new JobRecorder();

        // act
        DataFrame df = null;
        engine.executeDFTokenize( df, "SELECT df.'__org_idx', df.'h1.ts' TOKENIZE df.'h1.msg' USING 'myTokenizer' FROM df", recorder::jobRecorder );

        // assert
        String[] result = recorder.getRecordedjob().getColumnNamesToTransfer();
        assertThat( result, equalTo( new String[] { "__org_idx", "h1.ts" } ) );
    }

    @Test
    public void testExecuteDFTokenize_TokenizeStatementExtractColumnNameToTokenize_expectColumnnameToTokenize() throws Exception {
        // arrange
        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
        JobRecorder recorder = new JobRecorder();

        // act
        DataFrame df = null;
        engine.executeDFTokenize( df, "SELECT df.'__org_idx', df.'h1.ts' TOKENIZE df.'h1.msg' USING 'myTokenizer' FROM df", recorder::jobRecorder );

        // assert
        String result = recorder.getRecordedjob().getInputColumnNameToTokenize();
        assertThat( result, equalTo( "h1.msg" ) );
    }

    @Test
    public void testExecuteDFTokenize_TokenizeStatementExtractProcessorName_expectMyTokenizerAsTokenizer() throws Exception {
        // arrange
        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
        JobRecorder recorder = new JobRecorder();

        // act
        DataFrame df = null;
        engine.executeDFTokenize( df, "SELECT df.'__org_idx', df.'h1.ts' TOKENIZE df.'h1.msg' USING 'myTokenizer' FROM df", recorder::jobRecorder );

        // assert
        String result = recorder.getRecordedjob().getIngestProcessorName();
        assertThat( result, equalTo( "myTokenizer" ) );
    }

    @Test
    public void testExecuteDFTokenize_TokenizeStatementExtractDataframe_expectSameDataFrame() throws Exception {
        // arrange
        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();
        JobRecorder recorder = new JobRecorder();
        DataFrame df = Mockito.mock( DataFrame.class, "df" );

        // act
        engine.executeDFTokenize( df, "SELECT df.'__org_idx', df.'h1.ts' TOKENIZE df.'h1.msg' USING 'myTokenizer' FROM df", recorder::jobRecorder );

        // assert
        DataFrame result = recorder.getRecordedjob().getDataFrame();
        assertThat( result, is( sameInstance( df ) ) );
    }

}
