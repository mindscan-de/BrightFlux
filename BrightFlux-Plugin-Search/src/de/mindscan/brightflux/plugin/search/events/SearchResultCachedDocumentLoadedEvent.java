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
package de.mindscan.brightflux.plugin.search.events;

import de.mindscan.brightflux.framework.events.BFEvent;

/**
 * 
 */
public class SearchResultCachedDocumentLoadedEvent implements BFEvent {

    private String cachedDocumentContent;
    private String pathOfDocument;

    /**
     * 
     */
    public SearchResultCachedDocumentLoadedEvent( String cachedDocumentContent, String pathOfDocument ) {
        this.cachedDocumentContent = cachedDocumentContent;
        this.pathOfDocument = pathOfDocument;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getBFEventMessage() {
        return String.format( "Cached document loaded: '%s'", pathOfDocument );
    }

    /**
     * @return the cachedDocumentContent
     */
    public String getCachedDocumentContent() {
        return cachedDocumentContent;
    }

    /**
     * @return the pathOfDocument
     */
    public String getPathOfDocument() {
        return pathOfDocument;
    }

}
