package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLEmptyNode;

public class DataFrameQueryLanguageCompilerTest {

    @Test
    public void testCompileToRowFilterPredicate_EmptyNode_CompileToAny() throws Exception {
        // arrange
        DataFrameQueryLanguageCompiler compiler = new DataFrameQueryLanguageCompiler();

        // act
        DataFrameRowFilterPredicate predicate = compiler.compileToRowFilterPredicate( new DFQLEmptyNode() );

        // assert
        String result = predicate.describeOperation();
        assertThat( result, equalTo( "true" ) );
    }

}
