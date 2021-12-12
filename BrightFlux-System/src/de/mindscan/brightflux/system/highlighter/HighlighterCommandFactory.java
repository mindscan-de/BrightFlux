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

import java.nio.file.Path;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.system.highlighter.commands.CreateHighlightDataFrameCommand;
import de.mindscan.brightflux.system.highlighter.commands.DataFrameClearHighlightRowCommand;
import de.mindscan.brightflux.system.highlighter.commands.DataFrameHighlightRowCommand;
import de.mindscan.brightflux.system.highlighter.commands.LoadHighlightDataFrameCommand;
import de.mindscan.brightflux.system.highlighter.commands.SaveHighlightDataFrameCommand;

/**
 * 
 */
public class HighlighterCommandFactory {

    public static BFCommand createHighlightDataFrame() {
        return new CreateHighlightDataFrameCommand();
    }

    public static BFCommand saveHighlightDataFrame( DataFrame highlightDataFrame, Path targetFilePath ) {
        return new SaveHighlightDataFrameCommand( highlightDataFrame, targetFilePath );
    }

    public static BFCommand loadHighlightDataFrame( Path highlightFilePath ) {
        return new LoadHighlightDataFrameCommand( highlightFilePath );
    }

    public static BFCommand highlightRow( DataFrame inputDataFrame, int rowIndex, String color ) {
        return new DataFrameHighlightRowCommand( inputDataFrame, rowIndex, color );
    }

    public static BFCommand clearHighlightRow( DataFrame ingestedDF, int rowIndex ) {
        return new DataFrameClearHighlightRowCommand( ingestedDF, rowIndex );
    }

}
