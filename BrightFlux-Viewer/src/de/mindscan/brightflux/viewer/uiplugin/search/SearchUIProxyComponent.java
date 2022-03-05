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
package de.mindscan.brightflux.viewer.uiplugin.search;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.search.events.SearchResultCachedDocumentLoadedEvent;
import de.mindscan.brightflux.plugin.search.events.SearchResultDataframeCreatedEvent;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.viewer.uiplugin.search.events.SearchUIDataFrameRowRequestedEvent;
import de.mindscan.brightflux.viewer.uiplugin.search.persistence.SearchUIPersistenceModule;

/**
 * This is a class which will register to the search operations and will forward them if a search window is registered
 * in the case this is not opened, it will just do nothing, but we can later do this 
 */
public class SearchUIProxyComponent implements ProjectRegistryParticipant {

    private SearchWindow currentActiveSearchWindow = null;
    private ProjectRegistry projectRegistry;
    private SearchUIPersistenceModule persistenceModule;

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerSearchUIDataFrameRowRequsted( projectRegistry );
        registerSearchResultDataFrameCreated( projectRegistry );
        // TODO: move this to somewhere appropriate, 
        // currently this is okay to be hee, but "code evidence" should be handled by either the main program 
        // or a plugin specializing on code annotations.
        registerSearchResultCachedDocumentLoaded( projectRegistry );
    }

    private void registerSearchUIDataFrameRowRequsted( ProjectRegistry projectRegistry ) {
        BFEventListener listener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof SearchUIDataFrameRowRequestedEvent) {
                    if (currentActiveSearchWindow != null) {
                        currentActiveSearchWindow.searchRequestedRow( ((SearchUIDataFrameRowRequestedEvent) event).getRequestedRow() );
                        currentActiveSearchWindow.bringToTop();
                    }
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SearchUIDataFrameRowRequestedEvent.class, listener );
    }

    private void registerSearchResultDataFrameCreated( ProjectRegistry projectRegistry ) {
        BFEventListener searchDFCreatedlistener = new BFEventListenerAdapter() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof SearchResultDataframeCreatedEvent) {
                    if (currentActiveSearchWindow != null) {
                        // TODO access the details as well..., so we can see the lines....
                        currentActiveSearchWindow.addSearchResultDataFrame( ((SearchResultDataframeCreatedEvent) event).getDataFrame() );
                        currentActiveSearchWindow.bringToTop();
                    }
                }

            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SearchResultDataframeCreatedEvent.class, searchDFCreatedlistener );
    }

    /**
     * @param projectRegistry2
     */
    private void registerSearchResultCachedDocumentLoaded( ProjectRegistry projectRegistry2 ) {
        BFEventListener cachedDocumentListener = new BFEventListenerAdapter() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof SearchResultCachedDocumentLoadedEvent) {
                    SearchResultCachedDocumentLoadedEvent cachedDocumentEvent = (SearchResultCachedDocumentLoadedEvent) event;

                    if (currentActiveSearchWindow != null) {
                        currentActiveSearchWindow.addSearchResultCachedFile( cachedDocumentEvent.getPathOfDocument(),
                                        cachedDocumentEvent.getCachedDocumentContent() );
                        currentActiveSearchWindow.bringToTop();
                    }

                    // TODO: remove these files.
                    System.out.println( "===============================================================" );
                    System.out.println( "---------------------------------------------------------------" );
                    System.out.println( cachedDocumentEvent.getPathOfDocument() );
                    System.out.println( "---------------------------------------------------------------" );
                    System.out.println( cachedDocumentEvent.getCachedDocumentContent() );
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SearchResultCachedDocumentLoadedEvent.class, cachedDocumentListener );
    }

    // register the current search window
    public void registerCurrentActiveSearchWindow( SearchWindow activeSearchWindow ) {
        this.currentActiveSearchWindow = activeSearchWindow;
    }

    // unregister the current search window
    public void unregisterCurrentActiveSearchWindow() {
        this.currentActiveSearchWindow = null;
    }

    public boolean hasCurrentActiveSearchWindow() {
        return this.currentActiveSearchWindow != null;
    }

    public void focusCurrentActiveSearchWindow() {
        if (hasCurrentActiveSearchWindow()) {
            currentActiveSearchWindow.bringToTop();
        }
    }

    public void setPerstistenceModule( SearchUIPersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    public SearchUIPersistenceModule getPersistenceModule() {
        return persistenceModule;
    }

}
