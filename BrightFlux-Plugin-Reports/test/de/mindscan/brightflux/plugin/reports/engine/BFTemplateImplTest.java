package de.mindscan.brightflux.plugin.reports.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class BFTemplateImplTest {

    @Test
    public void testRenderTemplate_emptyString_returnsEmptyString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        // act
        String result = bfTemplateImpl.renderTemplate( "", Collections.emptyMap() );

        // assert
        assertThat( result, equalTo( "" ) );
    }

}
