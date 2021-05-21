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
package de.mindscan.brightflux.project;

import de.mindscan.brightflux.project.parts.LogAnalysisIngestConfiguration;

/**
 * This class implements a Log-Analysis-Project. This describes the Log-Analysis-Project, 
 * so it can be loaded using the original files and recreate all the  
 * 
 * * Configuration 
 *   * what file(s) gets loaded
 *   * what ingestion was used for which file
 *   
 * * Is there an index stored?
 *  
 * * Global Dataframe configuration
 * * Local Dataframe configuration
 * * Swim lanes configuration
 * * Graph-data-base
 *   * Annotations
 *   * relations between Dataframes
 *   * video book marks
 *   
 * * Json file / ZIP-File structure of different JSON files, which are essentially a
 *   database for the loganalysis project
 *   
 * * have multiple configurations, so the same analysis can be presented in different 
 *   ways, e.g think of multiple diagrams presenting different aspects. 
 */
public class LogAnalysisProjectImpl implements LogAnalysisProject {

    private LogAnalysisIngestConfiguration ingestConfiguration;

    /**
     * simple ctor, may not survive? 
     */
    public LogAnalysisProjectImpl() {
        this.ingestConfiguration = new LogAnalysisIngestConfiguration();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public LogAnalysisIngestConfiguration getIngestConfiguration() {
        return ingestConfiguration;
    }

}
