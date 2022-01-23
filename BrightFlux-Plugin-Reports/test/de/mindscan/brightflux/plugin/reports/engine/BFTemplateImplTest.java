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

    @Test
    public void testRenderTemplate_TwoDataFields_returnsFilledUserNameAndEngineNameString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();
        templateData.put( "userName", "BrightFlux User" );
        templateData.put( "engineName", "BrightFlux Engine" );

        // act
        String result = bfTemplateImpl.renderTemplate( "Hello {{data:userName}}! This is '{{data:engineName}}' serving you.", templateData );

        // assert
        assertThat( result, equalTo( "Hello BrightFlux User! This is 'BrightFlux Engine' serving you." ) );
    }

    @Test
    public void testRenderTemplate_TwoDataFieldsButOnlyUserNameFilled_returnsFilledUserNameAndEmptyEngineNameString() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();
        templateData.put( "userName", "BrightFlux User" );
        // templateData.put( "engineName", "BrightFlux Engine" );

        // act
        String result = bfTemplateImpl.renderTemplate( "Hello {{data:userName}}! This is '{{data:engineName}}' serving you.", templateData );

        // assert
        assertThat( result, equalTo( "Hello BrightFlux User! This is '' serving you." ) );
    }

    @Test
    public void testRenderTemplate_NoBlockGivenDepthOne_returnsPreliminaryAnalysisHeaderAndTwoSomeNewlines() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        // act
        String result = bfTemplateImpl.renderTemplate( "h4. Preliminary Analysis\r\n" + "\r\n" + "{{block:begin:X}}{{data:evidence_description}}\r\n" + "\r\n"
                        + "{code}\r\n" + "{code}\r\n" + "\r\n" + "{{block:end:X}}\r\n" + "", templateData );

        // assert
        assertThat( result, equalTo( "h4. Preliminary Analysis\r\n" + "\r\n" + "\r\n" ) );
    }

    @Test
    public void testRenderTemplate_OneBlockGivenDepthOne_returnsPreliminaryAndBlockContent() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        // act
        bfTemplateImpl.block( "X", templateData );
        String result = bfTemplateImpl.renderTemplate( // 
                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "{{block:end:X}}\r\n" //
                                        + "",
                        templateData );

        // assert
        assertThat( result, equalTo( //
                        "h4. Preliminary Analysis\r\n" // 
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_NoBlockGivenDepthTwo_returnsPreliminaryAnalysisHeaderAndSomeNewlines() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        // act
        String result = bfTemplateImpl.renderTemplate( "h4. Preliminary Analysis\r\n" + "\r\n" + "{{block:begin:X}}{{data:evidence_description}}\r\n" + "\r\n"
                        + "{code}\r\n" + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" + "{{block:end:Y}}\r\n"
                        + "{code}\r\n" + "\r\n" + "{{block:end:X}}\r\n" + "", templateData );

        // assert
        assertThat( result, equalTo( "h4. Preliminary Analysis\r\n" + "\r\n" + "\r\n" ) );
    }

}
