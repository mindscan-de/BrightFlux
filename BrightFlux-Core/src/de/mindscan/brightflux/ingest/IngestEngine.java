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
package de.mindscan.brightflux.ingest;

import java.util.Iterator;
import java.util.List;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameBuilder;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompiler;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2Impl;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.parser.DataFrameParser;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizer;

/**
 * The first step for the ingest engine is, that it executes one ingest job at a time. Later it can be extended 
 * with a Job Queue, Worker threads, Futures and Promises, a scheduler, A Job Management, Plugin Handling and so 
 * forth. 
 */
public class IngestEngine {

    /**
     * This method will execute a Ingest-JobConfiguration and return a data frame from it.
     * @param config
     * @return
     */
    public static DataFrame execute( JobConfiguration config ) {
        // prepare pipeline
        DataTokenizer tokenizer = config.tokenizerFactoryInstance.buildTokenizerInstance( config.getTokenizerConfiguration() );
        DataFrameParser dfParser = config.parserFactoryInstance.buildDataFrameParserInstance();
        DataFrameCompiler dfCompiler = config.compilerFactoryInstance.buildDataFrameCompilerInstance();

        Iterator<DataToken> tokens = executeTokenizeStrategy( config, tokenizer );

        List<DataFrameColumn<DataToken>> parsedDataFrameColumns = dfParser.parse( tokens );
        List<DataFrameColumn<?>> compiledDataFrameColumns = dfCompiler.compileDataFrameColumns( parsedDataFrameColumns );

        DataFrame newDataFrame = buildCompiledDataFrame( config, compiledDataFrameColumns );

        // TODO: use a serializer in dfquery to generate this journal entry, so the parser and serializer are synchronous to each other 
        newDataFrame.appendJournal( DataFrameJournalEntryType.LOAD, "LOAD '" + config.getIngestInputPath() + "' as df" );
        return newDataFrame;
    }

    private static Iterator<DataToken> executeTokenizeStrategy( JobConfiguration config, DataTokenizer tokenizer ) {
        // Tokenizer tells, it is a text file based strategy
        if (tokenizer.isStringBased()) {
            // TODO: from config determine which kind of input pipeline to select...
            // TODO: Actually we should not know how the tokenizer is doing his job... We should not use this information here to do the "right" thing

            // Reminder:
            // path and such should be part of the Ingest pipeline configuration, also the input (file, network, mqtt, etc)
            // should be part of the pipeline configuration and be plugable

            DataSourceV2Impl dataSourceV2Impl = new DataSourceV2Impl();
            dataSourceV2Impl.setInput( config.getIngestInputPath() );

            return tokenizer.tokenize( dataSourceV2Impl );
        }

        // Tokenizer tells, it is a binary file based strategy
        if (tokenizer.isBinaryBased()) {

            // TODO: how do we want to manage this? 
            // We have some kind of skipstream on a binary format and then have a consumer
//            ArrayList<DataToken> result = new ArrayList<DataToken>();
//            return result.iterator();

            DataSourceV2Impl dataSourceV2Impl = new DataSourceV2Impl();
            dataSourceV2Impl.setInput( config.getIngestInputPath() );

            return tokenizer.tokenize( dataSourceV2Impl );
        }

        throw new IllegalArgumentException( "Illegal tokenizer strategy encoded." );
    }

    private static DataFrame buildCompiledDataFrame( JobConfiguration config, List<DataFrameColumn<?>> compiledDataFrameColumns ) {
        DataFrameBuilder dfBuilder = new DataFrameBuilder().addName( config.getDataFrameName() );

        if (compiledDataFrameColumns != null) {
            dfBuilder.addColumns( compiledDataFrameColumns );
        }

        return dfBuilder.build();
    }

}
