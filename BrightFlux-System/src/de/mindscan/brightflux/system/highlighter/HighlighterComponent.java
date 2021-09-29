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
package de.mindscan.brightflux.system.highlighter;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.highlighter.events.DataFrameHighlightRowEvent;
import de.mindscan.brightflux.system.highlighter.events.HighlighterDataFrameCreatedEvent;

/**
 * 
 */
public class HighlighterComponent implements ProjectRegistryParticipant {

    // HLS?
    public static final String HIGHLIGHT_COLOR_VALUE_COLUMN_NAME = "colorValue";
    public static final String HIGHLIGHT_COLOR_INTENSITY_COLUMN_NAME = "colorIntensity";

    private DataFrame logHighlightFrame = null;

    /**
     * 
     */
    public HighlighterComponent() {
        // intentionally left blank
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        registerHighlightRowEvent( projectRegistry );
        registerHighlightDFCreateEvent( projectRegistry );
    }

    /**
     * @param projectRegistry
     */
    private void registerHighlightDFCreateEvent( ProjectRegistry projectRegistry ) {
        BFEventListener listener = new BFEventListenerAdapter();
        projectRegistry.getEventDispatcher().registerEventListener( HighlighterDataFrameCreatedEvent.class, listener );
    }

    /**
     * @param projectRegistry
     */
    private void registerHighlightRowEvent( ProjectRegistry projectRegistry ) {
        BFEventListener listener = new BFEventListenerAdapter();
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameHighlightRowEvent.class, listener );

    }

    /**
     * @return the logHighlightFrame
     */
    public DataFrame getLogHighlightFrame() {
        return logHighlightFrame;
    }

}
