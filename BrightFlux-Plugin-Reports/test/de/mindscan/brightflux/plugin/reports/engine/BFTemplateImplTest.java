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
        Map<String, String> emptyBlockData = new HashMap<>();

        // act
        bfTemplateImpl.block( "X", emptyBlockData );
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
    public void testRenderTemplate_OneBlockGivenAndBlockDataDepthOne_returnsPreliminaryAndBlockContentWithBlockData() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockData = new HashMap<>();
        blockData.put( "evidence_description", "XContent One" );

        // act
        bfTemplateImpl.block( "X", blockData );
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
                                        + "XContent One\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_TwoBlocksGivenAndBlockDataDepthOne_returnsPreliminaryAndTwoBlocksContentWithBlockData() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockDataOne = new HashMap<>();
        blockDataOne.put( "evidence_description", "XContent One" );
        Map<String, String> blockDataTwo = new HashMap<>();
        blockDataTwo.put( "evidence_description", "XContent Two" );

        // act
        bfTemplateImpl.block( "X", blockDataOne );
        bfTemplateImpl.block( "X", blockDataTwo );

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
                                        // Block One
                                        + "XContent One\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        // Block Two
                                        + "XContent Two\r\n" //
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
        String result = bfTemplateImpl.renderTemplate( //

                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" //
                                        + "{{block:end:Y}}\r\n" //
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
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_OneBlockGivenDepthTwo_returnsPreliminaryAnalysisHeaderAndFirstBlock() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockDataOne = new HashMap<>();
        blockDataOne.put( "evidence_description", "XContent One" );

        // act
        bfTemplateImpl.block( "X", blockDataOne );

        // act
        String result = bfTemplateImpl.renderTemplate( //

                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" //
                                        + "{{block:end:Y}}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "{{block:end:X}}\r\n" //
                                        + "",
                        templateData );

        // assert
        assertThat( result, equalTo( //
                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "XContent One\r\n" //
                                        + "\r\n"//
                                        + "{code}\r\n"//
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_OneXOneYBlockGivenDepthTwo_returnsPreliminaryAnalysisHeaderAndBothBlocks() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockDataOne = new HashMap<>();
        blockDataOne.put( "evidence_description", "XContent One" );
        Map<String, String> blockDataTwo = new HashMap<>();
        blockDataTwo.put( "extracontent", "XtraContent One" );

        // act
        bfTemplateImpl.block( "X", blockDataOne );
        bfTemplateImpl.block( "Y", blockDataTwo );

        // act
        String result = bfTemplateImpl.renderTemplate( //

                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" //
                                        + "{{block:end:Y}}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "{{block:end:X}}\r\n" //
                                        + "",
                        templateData );

        // assert
        assertThat( result, equalTo( //
                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "XContent One\r\n" //
                                        + "\r\n"//
                                        + "{code}\r\n"//
                                        + "XtraContent One{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_OneXTwoYBlockGivenDepthTwo_returnsPreliminaryAnalysisHeaderAndBothBlocks() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockDataOne = new HashMap<>();
        blockDataOne.put( "evidence_description", "XContent One" );
        Map<String, String> blockDataTwo = new HashMap<>();
        blockDataTwo.put( "extracontent", "XtraContent One" );
        Map<String, String> blockDataThree = new HashMap<>();
        blockDataThree.put( "extracontent", "XtraContent Two" );

        // act
        bfTemplateImpl.block( "X", blockDataOne );
        bfTemplateImpl.block( "Y", blockDataTwo );
        bfTemplateImpl.block( "Y", blockDataThree );

        // act
        String result = bfTemplateImpl.renderTemplate( //

                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" //
                                        + "{{block:end:Y}}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "{{block:end:X}}\r\n" //
                                        + "",
                        templateData );

        // assert
        assertThat( result, equalTo( //
                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "XContent One\r\n" //
                                        + "\r\n"//
                                        + "{code}\r\n"//
                                        + "XtraContent One{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "XtraContent Two{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "\r\n" //
                                        + "" ) );
    }

    @Test
    public void testRenderTemplate_TwoXThreeYBlockGivenDepthTwo_returnsPreliminaryAnalysisHeaderAndBothBlocks() throws Exception {
        // arrange
        BFTemplateImpl bfTemplateImpl = new BFTemplateImpl();

        Map<String, String> templateData = new HashMap<>();

        Map<String, String> blockDataOne = new HashMap<>();
        blockDataOne.put( "evidence_description", "XContent One" );
        Map<String, String> blockDataTwo = new HashMap<>();
        blockDataTwo.put( "extracontent", "XtraContent One" );
        Map<String, String> blockDataThree = new HashMap<>();
        blockDataThree.put( "extracontent", "XtraContent Two" );

        Map<String, String> blockDataFour = new HashMap<>();
        blockDataFour.put( "evidence_description", "XContent Two" );
        Map<String, String> blockDataFive = new HashMap<>();
        blockDataFive.put( "extracontent", "XtraContent Three" );

        // act
        bfTemplateImpl.block( "X", blockDataOne );
        bfTemplateImpl.block( "Y", blockDataTwo );
        bfTemplateImpl.block( "Y", blockDataThree );

        bfTemplateImpl.block( "X", blockDataFour );
        bfTemplateImpl.block( "Y", blockDataFive );

        // act
        String result = bfTemplateImpl.renderTemplate( //

                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //
                                        + "{{block:begin:X}}{{data:evidence_description}}\r\n" //
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "{{block:begin:Y}}{{data:extracontent}}{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n" //
                                        + "{{block:end:Y}}\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        + "{{block:end:X}}\r\n" //
                                        + "",
                        templateData );

        // assert
        assertThat( result, equalTo( //
                        "h4. Preliminary Analysis\r\n" //
                                        + "\r\n" //

                                        // Block X
                                        + "XContent One\r\n" //
                                        + "\r\n"//
                                        + "{code}\r\n"//
                                        + "XtraContent One{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "XtraContent Two{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "\r\n" //
                                        + "{code}\r\n" //
                                        + "\r\n" //
                                        // /Block X

                                        // Block X
                                        + "XContent Two\r\n" //
                                        + "\r\n"//
                                        + "{code}\r\n"//
                                        + "XtraContent Three{{columnData:h1.ts}}:{{columnData:h2.msg}}\r\n"//
                                        + "\r\n" //
                                        + "{code}\r\n"//
                                        + "\r\n"//
                                        // /Block X
                                        + "\r\n"//                                        
                                        + "" ) );
    }

}
