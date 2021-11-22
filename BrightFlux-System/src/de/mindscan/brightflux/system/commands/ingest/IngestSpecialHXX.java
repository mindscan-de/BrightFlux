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

import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.ingest.IngestEngine;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;
import de.mindscan.brightflux.system.events.BFEventFactory;

/**
 * This is just a test for deriving a dataframe from the content of a parent dataframe
 * 
 * This parent dataframe is parsed/tokenized and compiled to a new dataframe. 
 * This command uses proprietary modules / reader. And works only on frames 
 * derived with another proprietary module / reader.
 * 
 * It also serves as an incubation example which is used to test a future plugin concept.
 */
public class IngestSpecialHXX implements BFCommand {

    private static final String HARDCODED_ORIGINAL_INDEX_COLUMN_NAME = DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME;
    private static final String HARDCODED_H1_TS = "h1.ts";

    private DataFrame contentDataFrame;
    private String sourceColumn;
    private String[] transferColumns;

    /**
     * 
     */
    public IngestSpecialHXX( DataFrame contentDataFrame, String sourceColumn ) {
        this.contentDataFrame = contentDataFrame;
        this.sourceColumn = sourceColumn;
        // this command can only be applied to files loaded with the SpecialRAWTokenizer, 
        // this are Columnnames depending on the other SpecialRAWTokenizer   
        this.transferColumns = new String[] { HARDCODED_ORIGINAL_INDEX_COLUMN_NAME, HARDCODED_H1_TS };
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {

        JobConfiguration config2 = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );

        config2.setTokenizerConfiguration( "de.mindscan.brightflux.ingest.tokenizers.SpecialHXXTokenizerImpl" );
        config2.setDataFrameName( "HXX: " + contentDataFrame.getTitle() );
        config2.setIngestInputDataFrame( this.contentDataFrame, this.transferColumns, this.sourceColumn );

        DataFrame compileHmiDataFrame = IngestEngine.execute( config2 );
        eventConsumer.accept( BFEventFactory.dataframeCreated( compileHmiDataFrame, contentDataFrame ) );
    }

}
