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
package de.mindscan.brightflux.system.recipes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.ingest.IngestEngine;

/**
 * 
 */
public class RecipeUtils {

    public static DataFrame applyRecipeToDataFrame( BFRecipe recipe, DataFrame currentDataFrame, Map<String, DataFrameRowQueryCallback> callbacks ) {
        List<DataFrameJournalEntry> executableEntries = getExecutableRecipeEntries( recipe );
        if (executableEntries.size() > 0) {
            // apply the recipe onto the data frame in a serial manner
            for (DataFrameJournalEntry entry : executableEntries) {
                String msg = entry.getLogMessage();

                // according to the entry type we need to select the correct strategy to apply here... 
                switch (entry.getEntryType()) {
                    case LOAD:
                        // we already filtered them out before
                        break;
                    case SELECT_WHERE: {
                        String query = msg;
                        currentDataFrame = currentDataFrame.query( query );
                        break;
                    }
                    case ROWCALLBACK: {
                        String query = msg;
                        currentDataFrame = currentDataFrame.queryCB( query, callbacks );
                        break;
                    }
                    case TOKENIZE: {
                        String query = msg;
                        currentDataFrame = currentDataFrame.queryTKN( query, IngestEngine::execute );
                        break;
                    }
                    default:
                        throw new NotYetImplemetedException();
                }
            }
        }
        return currentDataFrame;
    }

    private static List<DataFrameJournalEntry> getExecutableRecipeEntries( BFRecipe recipe ) {
        return recipe.getRecipeEntries().stream()//
                        .filter( entry -> entry.getEntryType() != DataFrameJournalEntryType.LOAD )//
                        .collect( Collectors.toList() );
    }

}
