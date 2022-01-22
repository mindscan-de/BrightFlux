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

    Pattern pattern = Pattern.compile( "\\{\\{(data):(.+?)\\}\\}" );

    public String renderFileTemplate( String templateName, Map<String, String> data ) {
        // Read template from file "templateName" and then use renderTemplate
        String template = readFromFile( templateName );

        return renderTemplate( template, data );
    }

    private String readFromFile( String templateName ) {
        return "";
    }

    public String renderTemplate( String template, Map<String, String> data ) {

        String rendered = template;

        rendered = replace_callback( template, pattern, new Function<Matcher, String>() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public String apply( Matcher m ) {
                switch (m.group( 1 )) {
                    case "data":
                        return data.getOrDefault( m.group( 2 ), "" );
                    default:
                        throw new NotYetImplemetedException( "this seems not to be cool right now." );
                }
            }
        } );

        return rendered;
    }

    private String replace_callback( String template, Pattern pattern, Function<Matcher, String> callback ) {
        StringBuffer resultString = new StringBuffer();
        Matcher regexMatcher = pattern.matcher( template );
        while (regexMatcher.find()) {
            regexMatcher.appendReplacement( resultString, callback.apply( regexMatcher ) );
        }
        regexMatcher.appendTail( resultString );

        return resultString.toString();
    }

}
