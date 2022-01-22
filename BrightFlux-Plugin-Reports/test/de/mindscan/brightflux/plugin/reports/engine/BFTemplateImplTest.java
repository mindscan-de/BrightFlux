package de.mindscan.brightflux.plugin.reports.engine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testRenderTemplate_HelloWorldString_returnsHelloWorldString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        // act
        String result = bfTemplateImpl.renderTemplate( "Hello World!", Collections.emptyMap() );

        // assert
        assertThat( result, equalTo( "Hello World!" ) );
    }

    @Test
    public void testRenderTemplate_NamedHelloString_returnsFilledNamedHelloString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();
        templateData.put( "userName", "BrightFlux User" );

        // act
        String result = bfTemplateImpl.renderTemplate( "Hello {{data:userName}}!", templateData );

        // assert
        assertThat( result, equalTo( "Hello BrightFlux User!" ) );
    }

    @Test
    public void testRenderTemplate_HelloBrightfluxString_returnsFilledHelloBrightfluxString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();
        templateData.put( "userName", "BrightFlux" );

        // act
        String result = bfTemplateImpl.renderTemplate( "Hello {{data:userName}}!", templateData );

        // assert
        assertThat( result, equalTo( "Hello BrightFlux!" ) );
    }

}
