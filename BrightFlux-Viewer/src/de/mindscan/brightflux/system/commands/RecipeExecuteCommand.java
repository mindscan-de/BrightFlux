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
package de.mindscan.brightflux.system.commands;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRowFilterPredicate;
import de.mindscan.brightflux.dataframes.filterpredicate.DataFrameRowFilterPredicateFactory;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntry;
import de.mindscan.brightflux.dataframes.journal.DataFrameJournalEntryType;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.recipe.BFRecipe;
import de.mindscan.brightflux.recipe.BFRecipeIO;
import de.mindscan.brightflux.system.events.BFEvent;
import de.mindscan.brightflux.system.events.DataFrameLoadedEvent;

/**
 * 
 */
public class RecipeExecuteCommand implements BFCommand {

    private DataFrame inputDataFrame;
    private Path recipeFilePath;

    /**
     * @param inputDataFrame
     * @param recipeFilePath
     */
    public RecipeExecuteCommand( DataFrame inputDataFrame, Path recipeFilePath ) {
        this.inputDataFrame = inputDataFrame;
        this.recipeFilePath = recipeFilePath;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void execute( Consumer<BFEvent> eventConsumer ) {
        BFRecipe recipe = BFRecipeIO.loadFromFile( recipeFilePath );

        // TODO: apply the recipe
        if (recipe != null) {
            // use everything that is not a load command.
            List<DataFrameJournalEntry> recipeEntries = recipe.getRecipeEntries().parallelStream()
                            .filter( entry -> entry.getEntryType() != DataFrameJournalEntryType.LOAD ).collect( Collectors.toList() );

            if (recipeEntries.size() > 0) {
                // apply the receipt onto the data frame in a serial manner
                DataFrame currentDataFrame = inputDataFrame;
                for (DataFrameJournalEntry entry : recipeEntries) {
                    String predicate = entry.getLogMessage();

                    // according to the entry type we need to select the correct strategy to apply here... 
                    switch (entry.getEntryType()) {
                        case LOAD:
                            // we already filtered them out before
                            break;
                        case SELECT_WHERE:
                            // TODO implement the real parsing.
                            // TODO: parse the predicate -> get an AST
                            // compile the selectionPredicate,
                            // then compile the rowfilterpredicate...
                            // get columndescriptions of parsed tree
                            // get predicate of parsed tree

                            // "parse..." - i mean we only can/must do these at the moment
                            // everything else is just a waste of time currently.
                            if (predicate.equals( "SELECT * FROM df WHERE ( df.'h2.msg'.contains ('0x666') )" )) {
                                DataFrameRowFilterPredicate mypredicate = DataFrameRowFilterPredicateFactory.containsStr( "h2.msg", "0x666" );
                                currentDataFrame = currentDataFrame.select().where( mypredicate );
                            }
                            else if (predicate.equals( "SELECT * FROM df WHERE ( ( df.'h2.sysctx' == 6 )  AND ( df.'h2.b8' == 10 )   )" )) {
                                DataFrameRowFilterPredicate mypredicate = DataFrameRowFilterPredicateFactory.and( // h2.sysctx == 6 && h2.b8==10 
                                                DataFrameRowFilterPredicateFactory.eq( "h2.sysctx", 6 ), DataFrameRowFilterPredicateFactory.eq( "h2.b8", 10 ) );
                                currentDataFrame = currentDataFrame.select().where( mypredicate );
                            }
                            else {
                                throw new NotYetImplemetedException();
                            }

                            break;
                        default:
                            throw new NotYetImplemetedException();
                    }

                }

                // DataFrameLoaded
                eventConsumer.accept( new DataFrameLoadedEvent( currentDataFrame ) );
            }

            // Nothing to do...

            // TODO: fire a event that this dataframe was processed / dataFrameLoaded / dataFrameCalculated
            throw new NotYetImplemetedException();
        }
        else {
            // TODO: fire a event, that the recipe load operation failed
            throw new NotYetImplemetedException();
        }
    }

}
