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
package de.mindscan.brightflux.system.commands.recipes;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.ingest.IngestEngine;
import de.mindscan.brightflux.recipe.BFRecipe;
import de.mindscan.brightflux.recipe.BFRecipeIO;
import de.mindscan.brightflux.system.events.BFEventFactory;

/**
 * 
 */
public class RecipeExecuteCommand implements BFCommand {

    private DataFrame inputDataFrame;
    private Path recipeFilePath;
    private Map<String, DataFrameRowQueryCallback> callbacks;

    /**
     * @param inputDataFrame
     * @param recipeFilePath
     */
    public RecipeExecuteCommand( DataFrame inputDataFrame, Path recipeFilePath ) {
        this( inputDataFrame, recipeFilePath, new HashMap<>() );
    }

    /**
     * 
     */
    public RecipeExecuteCommand( DataFrame inputDataFrame, Path recipeFilePath, Map<String, DataFrameRowQueryCallback> callbacks ) {
        this.inputDataFrame = inputDataFrame;
        this.recipeFilePath = recipeFilePath;
        this.callbacks = callbacks;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        BFRecipe recipe = BFRecipeIO.loadFromFile( recipeFilePath );

        if (recipe != null) {
            List<DataFrameJournalEntry> executableEntries = getExecutableRecipeEntries( recipe );

            if (executableEntries.size() > 0) {
                DataFrame currentDataFrame = inputDataFrame;

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

                if (currentDataFrame != inputDataFrame) {
                    eventConsumer.accept( BFEventFactory.dataframeCreated( currentDataFrame, inputDataFrame ) );
                }
            }

            // Nothing to do...
        }
        else {
            throw new NotYetImplemetedException();
        }
    }

    private List<DataFrameJournalEntry> getExecutableRecipeEntries( BFRecipe recipe ) {
        return recipe.getRecipeEntries().stream()//
                        .filter( entry -> entry.getEntryType() != DataFrameJournalEntryType.LOAD )//
                        .collect( Collectors.toList() );
    }

}
