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
package de.mindscan.brightflux.ingest.engine;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2;
import de.mindscan.brightflux.ingest.datasource.DataSourceV2Factory;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;

/**
 * 
 * Ingest Input: can be path, but should also be possible to define more input.
        // path and such should be part of the Ingest pipeline configuration, also the input (file, network, mqtt, etc)
        // should be part of the pipeline configuration and be pluggable
 * 
 */
public class JobConfiguration {

    public static final String MODE_DATAFRAME = "dataframe";
    public static final String MODE_PATH = "path";

    public DataTokenizerFactory tokenizerFactoryInstance;
    public DataFrameParserFactory parserFactoryInstance;
    public DataFrameCompilerFactory compilerFactoryInstance;

    private String tokenizerConfiguration;
    private String dataFrameName;
    private Path ingestInputPath;
    private DataFrame ingestDataFrame;
    private String ingestMode;
    private String[] transferColumns;
    private String ingestDataFrameInputColumnName;

    /**
     * 
     */
    public JobConfiguration( DataTokenizerFactory tokenizerFactoryInstance, DataFrameParserFactory parserFactoryInstance,
                    DataFrameCompilerFactory compilerFactoryInstance ) {
        this.tokenizerFactoryInstance = tokenizerFactoryInstance;
        this.parserFactoryInstance = parserFactoryInstance;
        this.compilerFactoryInstance = compilerFactoryInstance;
    }

    /**
     * @param tokenizerConfiguration
     */
    public void setTokenizerConfiguration( String tokenizerConfiguration ) {
        this.tokenizerConfiguration = tokenizerConfiguration;
    }

    /**
     * @return the tokenizerConfiguration
     */
    public String getTokenizerConfiguration() {
        return tokenizerConfiguration;
    }

    /**
     * @param string
     */
    public void setDataFrameName( String dataFrameName ) {
        this.dataFrameName = dataFrameName;
    }

    /**
     * @return the dataFrameName
     */
    public String getDataFrameName() {
        return dataFrameName;
    }

    /**
     * @param path
     */
    public void setIngestInputFilePath( Path ingestInputPath ) {
        this.ingestMode = MODE_PATH;
        this.ingestInputPath = ingestInputPath;
    }

    /**
     * @return the ingestInputPath
     */
    public Path getIngestInputPath() {
        return ingestInputPath;
    }

    public void setIngestInputDataFrame( DataFrame ingestDataFrame, String[] transferColumns, String ingestDataFrameInputColumnName ) {
        this.ingestMode = MODE_DATAFRAME;
        this.ingestDataFrame = ingestDataFrame;
        this.transferColumns = transferColumns;
        this.ingestDataFrameInputColumnName = ingestDataFrameInputColumnName;
    }

    public String getIngestMode() {
        return ingestMode;
    }

    // dataframe mode only
    public DataFrame getIngestDataFrame() {
        if (!MODE_DATAFRAME.equals( getIngestMode() )) {
            throw new IllegalArgumentException( "Only usable in data frame ingestion mode." );
        }
        return ingestDataFrame;
    }

    // dataframe mode only
    public String[] getTransferColumns() {
        if (!MODE_DATAFRAME.equals( getIngestMode() )) {
            throw new IllegalArgumentException( "Only usable in data frame ingestion mode." );
        }
        return transferColumns;
    }

    // dataframe mode only
    public String getIngestDataFrameInputColumnName() {
        if (!MODE_DATAFRAME.equals( getIngestMode() )) {
            throw new IllegalArgumentException( "Only usable in data frame ingestion mode." );
        }
        return ingestDataFrameInputColumnName;
    }

    public DataSourceV2 getDataSource() {
        switch (this.getIngestMode()) {
            case MODE_DATAFRAME:
                return DataSourceV2Factory.createDataSourceV2( ingestDataFrame, transferColumns, ingestDataFrameInputColumnName );
            case MODE_PATH:
                return DataSourceV2Factory.createDataSourceV2( ingestInputPath );
            default:
                throw new NotYetImplemetedException();
        }
    }

    /**
     * 
     */
    public String getOperationAsDFQLStatement() {
        switch (this.getIngestMode()) {
            case MODE_PATH:
                return "LOAD '" + getIngestInputPath() + "' as df";
            case MODE_DATAFRAME:
                String allTransferColumns = Arrays.stream( transferColumns ).map( this::columnConverter ).collect( Collectors.joining( ", " ) );
                String tokenizer = tokenizerConfiguration;

                return "SELECT " + allTransferColumns + // 
                                " TOKENIZE " + columnConverter( ingestDataFrameInputColumnName ) + //
                                " USING '" + tokenizer + "' from df";

            default:
                throw new NotYetImplemetedException( "Could not retrieve the DFQLStatement for this ingest job configuration" );
        }
    }

    private String columnConverter( String columnName ) {
        return "df.'" + columnName + "'";
    }

}