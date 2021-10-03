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

import java.util.HashMap;
import java.util.Map;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;

/**
 * 
 */
public class HighlighterCallbacks {

    private static class Holder {
        private static HighlighterCallbacks instance = new HighlighterCallbacks();
    }

    public static HighlighterCallbacks getInstance() {
        return Holder.instance;
    }

    public static void initializeWithProjectRegistry( ProjectRegistry projectRegistry ) {
        HighlighterCallbacks currentInstance = getInstance();

        currentInstance.generateCallbacks( projectRegistry );
    }

    /**
     * @param projectRegistry
     */
    private void generateCallbacks( final ProjectRegistry projectRegistry ) {
        DataFrameRowQueryCallback highlightRed = new DataFrameRowQueryCallback() {
            @Override
            public void apply( DataFrameRow row ) {
                BFCommand highlightRedCommand = DataFrameCommandFactory.highlightRow( row.getDataFrameInternal(), row.getOriginalRowIndex(), "red" );
                projectRegistry.getCommandDispatcher().dispatchCommand( highlightRedCommand );
            }
        };
        callbacks.put( "highlight_red", highlightRed );

        // --

        DataFrameRowQueryCallback highlightGreen = new DataFrameRowQueryCallback() {
            @Override
            public void apply( DataFrameRow row ) {
                BFCommand highlightGreenCommand = DataFrameCommandFactory.highlightRow( row.getDataFrameInternal(), row.getOriginalRowIndex(), "green" );
                projectRegistry.getCommandDispatcher().dispatchCommand( highlightGreenCommand );
            }
        };
        callbacks.put( "highlight_green", highlightGreen );

        // --

        DataFrameRowQueryCallback highlightBlue = new DataFrameRowQueryCallback() {
            @Override
            public void apply( DataFrameRow row ) {
                BFCommand highlightBlueCommand = DataFrameCommandFactory.highlightRow( row.getDataFrameInternal(), row.getOriginalRowIndex(), "blue" );
                projectRegistry.getCommandDispatcher().dispatchCommand( highlightBlueCommand );
            }
        };
        callbacks.put( "highlight_blue", highlightBlue );

        // --

        DataFrameRowQueryCallback highlightPink = new DataFrameRowQueryCallback() {
            @Override
            public void apply( DataFrameRow row ) {
                BFCommand highlightPinkCommand = DataFrameCommandFactory.highlightRow( row.getDataFrameInternal(), row.getOriginalRowIndex(), "pink" );
                projectRegistry.getCommandDispatcher().dispatchCommand( highlightPinkCommand );
            }
        };
        callbacks.put( "highlight_pink", highlightPink );

        // -- 

        DataFrameRowQueryCallback highlightYellow = new DataFrameRowQueryCallback() {
            @Override
            public void apply( DataFrameRow row ) {
                BFCommand highlightYellowCommand = DataFrameCommandFactory.highlightRow( row.getDataFrameInternal(), row.getOriginalRowIndex(), "yellow" );
                projectRegistry.getCommandDispatcher().dispatchCommand( highlightYellowCommand );
            }
        };
        callbacks.put( "highlight_red", highlightYellow );
    }

    private Map<String, DataFrameRowQueryCallback> callbacks = new HashMap<>();

    private HighlighterCallbacks() {
        callbacks = new HashMap<>();
    }

    /**
     * @return the callback
     */
    public Map<String, DataFrameRowQueryCallback> getCallbacks() {
        return callbacks;
    }
}
