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
package de.mindscan.brightflux.system.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.ingest.IngestEngine;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;
import de.mindscan.brightflux.system.events.BFEvent;
import de.mindscan.brightflux.system.events.DataFrameLoadedEvent;

/**
 * 
 */
public class IngestSpecialRAW implements BFCommand {

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {

        JobConfiguration config = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );

        Path path = Paths.get( "Y:\\TestDatasetABC\\ABC.raw" );
        config.setTokenizerConfiguration( "de.mindscan.brightflux.ingest.tokenizers.SpecialRAWTokenizerImpl" );
        config.setDataFrameName( path.getFileName().toString() );
        config.setIngestInputFilePath( path );

        DataFrame resultDataFrame = IngestEngine.execute( config );
        eventConsumer.accept( new DataFrameLoadedEvent( resultDataFrame ) );
    }

}
