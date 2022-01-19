/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.system.commands.ingest;

import java.nio.file.Path;
import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.ingest.IngestEngine;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.engine.JobConfigurationFactory;
import de.mindscan.brightflux.system.events.BFEventFactory;

/**
 * This is just a test for a binary oriented data reader for a file, which i have laying around here. 
 * This is the way to find out which kind of binary file reading capabilities must be developed over time. 
 * The SpecialRAWTokenizer is just a proof of concept for reading binary files to dataframes by converting them to iterable tokens.
 * 
 * It also serves as an incubation example which is used to test the Plugin-Concept, even with custom tokenizer plugins.
 */
public class IngestSpecialRAW implements BFCommand {

    private Path path;

    /**
     * 
     */
    public IngestSpecialRAW( Path path ) {
        this.path = path;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {

        JobConfiguration config = JobConfigurationFactory.createJobConfiguration( path, "de.mindscan.brightflux.ingest.tokenizers.SpecialRAWTokenizerImpl" );

        DataFrame resultDataFrame = IngestEngine.execute( config );
        eventConsumer.accept( BFEventFactory.dataframeLoaded( resultDataFrame ) );
    }

}
