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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.transform;

import java.util.function.Function;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.StringWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.StringWidgetVisualizerProvider;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.TimestampWidgetVisualizerProvider;

/**
 * 
 */
public class ETVColumnTransformer {

    private final String columnName;
    private final String targetVisualizer;
    private final String widgetName;
    private final Function<String, String> stringTransformer;
    private boolean useSelectedRow;

    public ETVColumnTransformer( String columnName, String targetVisualizer, String widgetName, Function<String, String> stringTransformer ) {
        this( columnName, targetVisualizer, widgetName, stringTransformer, false );
    }

    public ETVColumnTransformer( String columnName, String targetVisualizer, String widgetName, Function<String, String> stringTransformer,
                    boolean useSelectedRow ) {
        this.columnName = columnName;
        this.targetVisualizer = targetVisualizer;
        this.widgetName = widgetName;
        this.stringTransformer = stringTransformer;
        this.useSelectedRow = useSelectedRow;
    }

    public void execute( DataFrameRow computedRow, DataFrameRow selectedRow, Function<String, Object> getWidgetByName ) {
        // extract
        Object columnObject = useSelectedRow ? selectedRow.get( columnName ) : computedRow.get( this.columnName );

        // transform
        String transformedMessage = stringTransformer.apply( String.valueOf( columnObject ) );

        // get widget by Name
        Object widget = getWidgetByName.apply( widgetName );

        // use target visualizer to
        switch (targetVisualizer) {
            case "stringVisualizer": {
                if (widget instanceof StringWidgetVisualizerProvider) {
                    StringWidgetVisualizer stringVisualizer = ((StringWidgetVisualizerProvider) widget).getStringVisualizer();
                    stringVisualizer.setScalar( transformedMessage );
                }
                break;
            }
            case "timestampVisualizer": {
                if (widget instanceof TimestampWidgetVisualizerProvider) {
                    TimestampWidgetVisualizer timestampVisualizer = ((TimestampWidgetVisualizerProvider) widget).getTimestampVisualizer();
                    timestampVisualizer.setTimestamp( transformedMessage );
                }
            }
        }
    }

}
