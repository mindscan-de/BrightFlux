/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.plugin.reports.engine;

import static de.mindscan.brightflux.plugin.reports.engine.BFTemplateUtils.replace_callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;

/**
 *
 * 
    // OK, what to do here:
    // this in no form final right now, and we still might want to 
    // rephrase and rename things.

    // we read a template from disk
    // we parse that template and parse all blocks
    // we replace all blocks by block place holders
    // we keep the blocks by name and their replacement.
    // so we have actually a parsed template

    // This parsed template can be manually parsed and skipped.

    // Then we have all the block operations on the template, which provide the data for each placement operation

    // then we have a render operation which renders all the blocks and their replacements

    // so the basic pipeline is: 
    // # read template from disk 
    // # parse template
    // # fill the template using the block operations, data and environment
    // # then say render using data.
 * 
 */
public class BFTemplateImpl {

    private static final String DATA_KEYWORD = "data";
    private static final String PLACE_BLOCK_KEYWORD = "placeBlock";

    private static final String BEGIN_BLOCK_DETECTOR_STRING = "\\{\\{block:begin:(.+?)\\}\\}";
    private static final String NAMED_BLOCK_DETECTOR_STRING = "\\{\\{block:begin:%s\\}\\}(.*)\\{\\{block:end:%s\\}\\}";

    private static final Pattern pattern = Pattern.compile( "\\{\\{(" + DATA_KEYWORD + "|" + PLACE_BLOCK_KEYWORD + "):(.+?)\\}\\}" );
    private static final Pattern detectBlockStart = Pattern.compile( BEGIN_BLOCK_DETECTOR_STRING );

    private LinkedList<BFTemplateBlockData> templateBlockData = new LinkedList<>();
    private Map<String, String> templateReplacements = new HashMap<>();

    public BFTemplateImpl() {
        // intentionally left blank
    }

    public String renderFileTemplate( String templateName, Map<String, String> data ) {
        // Read template from file "templateName" and then use renderTemplate
        String template = readFromFile( templateName );

        return renderTemplate( template, data );
    }

    private String readFromFile( String templateName ) {
        return "";
    }

    public String renderTemplate( String template, Map<String, String> data ) {
        List<String> collectedBlockNames = collectBlockNamesReversedOrder( template );

        String preProcessedTemplate = compileTemplateBlockNames( template, collectedBlockNames );

        return renderTemplateInternal( preProcessedTemplate, data );
    }

    public void block( String blockName, Map<String, String> templateData ) {
        templateBlockData.add( new BFTemplateBlockDataImpl( blockName, templateData ) );
    }

    private List<String> collectBlockNamesReversedOrder( String template ) {
        List<String> collectedBlockNames = new ArrayList<>();
        replace_callback( template, detectBlockStart, new Function<Matcher, String>() {
            @Override
            public String apply( Matcher m ) {
                collectedBlockNames.add( m.group( 1 ) );
                return "";
            }
        } );
        Collections.reverse( collectedBlockNames );
        return collectedBlockNames;
    }

    private String compileTemplateBlockNames( String template, List<String> collectedBlockNames ) {
        String preProcessedTemplate = template;
        for (String blockName : collectedBlockNames) {
            String namedBlockDetector = String.format( NAMED_BLOCK_DETECTOR_STRING, blockName, blockName );
            Pattern namedBlockReplacer = Pattern.compile( namedBlockDetector, Pattern.DOTALL );
            preProcessedTemplate = replace_callback( preProcessedTemplate, namedBlockReplacer, new Function<Matcher, String>() {
                @Override
                public String apply( Matcher m ) {
                    templateReplacements.put( blockName, m.group( 1 ) );
                    return "{{" + PLACE_BLOCK_KEYWORD + ":" + blockName + ":-}}";
                }
            } );
        }
        return preProcessedTemplate;
    }

    private String renderTemplateInternal( String template, Map<String, String> data ) {
        String rendered = template;
        rendered = replace_callback( template, pattern, new Function<Matcher, String>() {
            @Override
            public String apply( Matcher m ) {
                switch (m.group( 1 )) {
                    case DATA_KEYWORD: {
                        return data.getOrDefault( m.group( 2 ), "" );
                    }
                    case PLACE_BLOCK_KEYWORD: {
                        String nameAndGeneration = m.group( 2 );
                        String[] split = nameAndGeneration.split( ":" );

                        if (templateBlockData.isEmpty()) {
                            return "";
                        }

                        BFTemplateBlockData peeked = templateBlockData.peekFirst();

                        if (peeked.isBlockName( split[0] )) {
                            // remove the peeked block from list.
                            peeked = templateBlockData.getFirst();
                            return renderTemplateInternal( templateReplacements.get( split[0] ), peeked.getTemplateData() );
                        }
                        else {
                            return "";
                        }
                    }
                    default:
                        throw new NotYetImplemetedException( "this seems not to be cool right now." );
                }
            }
        } );

        return rendered;
    }

}
