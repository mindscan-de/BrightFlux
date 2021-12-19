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
package de.mindscan.brightflux.plugin.highlighter;

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameSpecialColumns;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.highlighter.writer.HighlighterJsonLWriterImpl;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.events.dataframe.BFAbstractDataFrameEvent;
import de.mindscan.brightflux.system.highlighter.events.DataFrameClearHighlightRowEvent;
import de.mindscan.brightflux.system.highlighter.events.DataFrameHighlightRowEvent;
import de.mindscan.brightflux.system.highlighter.events.HighlighterDataFrameCreatedEvent;

/**
 * 
 */
public class HighlighterComponent implements ProjectRegistryParticipant {

    public static final String HIGHLIGHT_DATAFRAME_NAME = "logHighlightFrame";

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
        registerClearHighlightRowEvent( projectRegistry );
        registerHighlightDFCreateEvent( projectRegistry );
    }

    /**
     * @param projectRegistry
     */
    private void registerHighlightDFCreateEvent( ProjectRegistry projectRegistry ) {
        BFEventListener dataframeCreatedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof HighlighterDataFrameCreatedEvent) {
                    DataFrame dataframe = ((BFAbstractDataFrameEvent) event).getDataFrame();
                    if (dataframe != null) {
                        logHighlightFrame = dataframe;
                    }
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( HighlighterDataFrameCreatedEvent.class, dataframeCreatedListener );
    }

    /**
     * @param projectRegistry
     */
    private void registerHighlightRowEvent( ProjectRegistry projectRegistry ) {
        BFEventListener highlightListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof DataFrameHighlightRowEvent) {
                    // DataFrame dataframe = ((DataFrameHighlightRowEvent) event).getDataFrame();
                    if (logHighlightFrame != null) {
                        String color = ((DataFrameHighlightRowEvent) event).getColor();
                        int row = ((DataFrameHighlightRowEvent) event).getRow();
                        logHighlightFrame.setAt( DataFrameSpecialColumns.INDEX_COLUMN_NAME, row, row );
                        logHighlightFrame.setAt( DataFrameSpecialColumns.ORIGINAL_INDEX_COLUMN_NAME, row, row );
                        logHighlightFrame.setAt( HIGHLIGHT_COLOR_VALUE_COLUMN_NAME, row, color );
                    }
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameHighlightRowEvent.class, highlightListener );
    }

    /**
     * @param projectRegistry
     */
    private void registerClearHighlightRowEvent( ProjectRegistry projectRegistry ) {
        BFEventListener clearHighlightListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof DataFrameClearHighlightRowEvent) {
                    DataFrame dataframe = ((DataFrameClearHighlightRowEvent) event).getDataFrame();
                    // TODO: use the data of the event and fill the data intor the logDataFrame
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( DataFrameClearHighlightRowEvent.class, clearHighlightListener );
    }

    /**
     * @return the logHighlightFrame
     */
    public DataFrame getLogHighlightFrame() {
        return logHighlightFrame;
    }

    // TODO: basic idea on how to save the highlighter dataframe... 
    public void saveHighlightFrame( Path highlightFilePath ) {
        HighlighterJsonLWriterImpl highlighterJSONLWriterImpl = new HighlighterJsonLWriterImpl();

        highlighterJSONLWriterImpl.writeFile( logHighlightFrame, highlightFilePath );
    }

}
