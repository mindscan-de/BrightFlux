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
import de.mindscan.brightflux.dataframes.DataFrameTokenizerJob;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompiler;
import de.mindscan.brightflux.ingest.engine.JobConfiguration;
import de.mindscan.brightflux.ingest.engine.JobConfigurationFactory;
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

        DataFrame newDataFrame = updateDataFrameJournal( config, buildCompiledDataFrame( config, compiledDataFrameColumns ) );

        return newDataFrame;
    }

    public static DataFrame execute( DataFrameTokenizerJob tokenizerJob ) {
        return execute( JobConfigurationFactory.createJobConfiguration( tokenizerJob ) );
    }

    public static DataFrame updateDataFrameJournal( JobConfiguration config, DataFrame newDataFrame ) {
        // TODO: refactor this to the right place.... Actually we work on config, that means we might want to move things to JobConfiguration to add the journal
        // on the other hand, a configobject should not do this...
        // 
        switch (config.getIngestMode()) {
            case JobConfiguration.MODE_DATAFRAME:
                newDataFrame.appendJournal( config.getIngestDataFrame().getJournal() );
                if (!config.isLogAppendSuppressed()) {
                    newDataFrame.appendJournal( DataFrameJournalEntryType.TOKENIZE, config.getOperationAsDFQLStatement() );
                }
                break;
            case JobConfiguration.MODE_PATH:
                newDataFrame.appendJournal( DataFrameJournalEntryType.LOAD, config.getOperationAsDFQLStatement() );
                break;
            default:
                throw new NotYetImplemetedException( "imlement this mode...." );
        }
        return newDataFrame;
    }

    private static Iterator<DataToken> executeTokenizeStrategy( JobConfiguration config, DataTokenizer tokenizer ) {
        // TODO: depending on the configuration we might want to setup the dataSource slightly better... whatever this means.
        // * for files we set the input using the ingestInputPath
        // * for dataframe columns we might want to setup a completely different input provider.

        return tokenizer.tokenize( config.getDataSource() );
    }

    private static DataFrame buildCompiledDataFrame( JobConfiguration config, List<DataFrameColumn<?>> compiledDataFrameColumns ) {
        DataFrameBuilder dfBuilder = new DataFrameBuilder().addName( config.getDataFrameName() );

        if (compiledDataFrameColumns != null) {
            dfBuilder.addColumns( compiledDataFrameColumns );
        }

        return dfBuilder.build();
    }

}
