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
package de.mindscan.brightflux.ingest.engine;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrameTokenizerJob;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;

/**
 * 
 */
public class JobConfigurationFactory {

    public static JobConfiguration createJobConfiguration() {
        JobConfiguration config = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );
        return config;
    }

    public static JobConfiguration createJobConfiguration( DataFrameTokenizerJob tokenizerJob ) {
        JobConfiguration config2 = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );

        config2.setTokenizerConfiguration( tokenizerJob.getIngestProcessorName() );
        config2.setDataFrameName( "HXX: " + tokenizerJob.getDataFrame().getTitle() );
        config2.setIngestInputDataFrame( tokenizerJob.getDataFrame(), tokenizerJob.getColumnNamesToTransfer(), tokenizerJob.getInputColumnNameToTokenize() );
        config2.suppressLogAppend( true );

        return config2;
    }

    public static JobConfiguration createJobConfiguration( Path path, String tokenizerName ) {
        JobConfiguration config = new JobConfiguration( DataTokenizerFactory.getInstance(), DataFrameParserFactory.getInstance(),
                        DataFrameCompilerFactory.getIntance() );
        config.setTokenizerConfiguration( tokenizerName );
        config.setDataFrameName( path.getFileName().toString() );
        config.setIngestInputFilePath( path );
        return config;
    }

}
