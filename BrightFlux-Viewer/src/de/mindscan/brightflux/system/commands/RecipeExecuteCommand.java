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
import java.util.function.Consumer;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.recipe.BFRecipe;
import de.mindscan.brightflux.recipe.BFRecipeIO;
import de.mindscan.brightflux.system.events.BFEvent;

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

        // TODO: implement the receipt application onto the dataframe...
        throw new NotYetImplemetedException();
    }

}
