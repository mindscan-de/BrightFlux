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
package de.mindscan.brightflux.plugin.search.backend.furiousiron;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class SearchResultItemModel {
    private String queryResultSimpleFilename;
    private String queryResultFilePath;
    private long numberOfLinesInFile;
    private long fileSize;
    private Map<String, String> classifierMap;
    private Map<Integer, String> preview;

    public SearchResultItemModel( String queryResultSimpleFilename, String queryResultFilePath ) {
        this.queryResultSimpleFilename = queryResultSimpleFilename;
        this.queryResultFilePath = queryResultFilePath;
        this.classifierMap = new HashMap<>();
    }

    public String getQueryResultSimpleFilename() {
        return queryResultSimpleFilename;
    }

    public String getQueryResultFilePath() {
        return queryResultFilePath;
    }

    public long getNumberOfLinesInFile() {
        return numberOfLinesInFile;
    }

    public void setNumberOfLinesInFile( long numberOfLinesInFile ) {
        this.numberOfLinesInFile = numberOfLinesInFile;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize( long fileSize ) {
        this.fileSize = fileSize;
    }

    public Map<String, String> getClassifierMap() {
        return classifierMap;
    }

    public void setClassifierMap( Map<String, String> classifierMap ) {
        this.classifierMap = classifierMap;
    }

    public void setPreview( Map<Integer, String> preview ) {
        this.preview = preview;
    }

    public Map<Integer, String> getPreview() {
        return preview;
    }

}
