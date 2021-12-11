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
package de.mindscan.brightflux.system.favrecipes;

import java.util.List;

import de.mindscan.brightflux.system.earlypersistence.EarlyPersistenceComponent;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class FavRecipesActivator {
    public static final String FAVORITE_RECIPES_DIRECTORY_KEY = "favorites.recipes.dir";

    public FavRecipesActivator() {
    }

    /**
     * @param systemServices
     */
    public void start( SystemServices systemServices ) {
        EarlyPersistenceComponent earlyPersistence = systemServices.getEarlyPersistence();

        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        FavRecipesFileCollector favRecipesCollector = new FavRecipesFileCollector( favRecipesComponent );
        // TODO 
        // - set a fully configured persistence module instead, where the component can do everything in its own
        // - The component can also register listeners if it wants to be informed about changes (e.g. config pages)   
        favRecipesCollector.collect( earlyPersistence.getPropertyAsPath( FAVORITE_RECIPES_DIRECTORY_KEY ) );
        systemServices.registerService( favRecipesComponent, FavRecipesComponent.class );

        // TODO: remove this if not needed any more...
        printDebugIntermediateNodes( favRecipesComponent );
    }

    public void printDebugIntermediateNodes( FavRecipesComponent favRecipesComponent ) {
        // some debug...
        List<String> intermediateNodes = favRecipesComponent.getAllIntermediateNodes();
        System.out.println( "Intermediate nodes for favorite recipes" );
        System.out.println( intermediateNodes );
    }
}
