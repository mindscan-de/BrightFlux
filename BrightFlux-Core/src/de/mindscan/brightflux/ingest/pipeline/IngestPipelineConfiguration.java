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
package de.mindscan.brightflux.ingest.pipeline;

import de.mindscan.brightflux.ingest.compiler.DataFrameCompilerFactory;
import de.mindscan.brightflux.ingest.parser.DataFrameParserFactory;
import de.mindscan.brightflux.ingest.tokenizers.DataTokenizerFactory;

/**
 */
public class IngestPipelineConfiguration {
    /**
     * 
     */
    public DataTokenizerFactory tokenizerFactoryInstance;
    /**
     * 
     */
    public DataFrameParserFactory parserFactoryInstance;
    /**
     * 
     */
    public DataFrameCompilerFactory compilerFactoryInstance;
    private String tokenizerConfiguration;

    /**
     * 
     */
    public IngestPipelineConfiguration( DataTokenizerFactory tokenizerFactoryInstance, DataFrameParserFactory parserFactoryInstance,
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

}