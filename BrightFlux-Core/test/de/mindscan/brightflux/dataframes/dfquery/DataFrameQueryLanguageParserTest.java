package de.mindscan.brightflux.dataframes.dfquery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;
import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNumberNode;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLToken;
import de.mindscan.brightflux.dataframes.dfquery.tokens.DFQLTokenProvider;

public class DataFrameQueryLanguageParserTest {

    @Test
    public void testParseLiteral_Number_() throws Exception {
        // arrange
        String dfqlQuery = "123";
        Iterator<DFQLToken> tokenIterator = new DataFrameQueryLanguageTokenizer().tokenize( dfqlQuery );

        DataFrameQueryLanguageParser parser = new DataFrameQueryLanguageParser();
        parser.setTokenProvider( new DFQLTokenProvider( tokenIterator ) );

        // act
        DFQLNode result = parser.parseLiteral();

        // assert
        assertThat( result, is( instanceOf( DFQLNumberNode.class ) ) );
    }

}
