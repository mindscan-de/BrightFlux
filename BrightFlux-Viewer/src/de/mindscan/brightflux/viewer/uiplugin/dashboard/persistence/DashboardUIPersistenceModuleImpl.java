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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.persistence;

import java.nio.file.Path;

import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;

/**
 * 
 */
public class DashboardUIPersistenceModuleImpl implements DashboardUIPersistenceModule {

    private static final String DASHBOARD_RECIPE_NAMES_KEY = "dashboard.names";
    private static final String DASHBOARD_RECIPES_KEY = "dashboard.recipes";
    private static final String DASHBOARD_INDEX_EXTRACTOR_RECIPE = "dashboard.index.extractor.recipe";

    private BasePersistenceModule persistenceModule;

    /**
     * 
     */
    public DashboardUIPersistenceModuleImpl( BasePersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    @Override
    public String[] getDashboardRecipesNames() {
        return persistenceModule.getStringArrayValue( DASHBOARD_RECIPE_NAMES_KEY );
    }

    @Override
    public Path getDashboardRecipe( int recipeIndex ) {
        String[] recipes = persistenceModule.getStringArrayValue( DASHBOARD_RECIPES_KEY );
        String recipePath = recipes[recipeIndex];
        return persistenceModule.evaluateAsPath( recipePath );
    }

    @Override
    public Path getDashboardIndexExtractorRecipePath() {
        String recipeValue = persistenceModule.getStringValue( DASHBOARD_INDEX_EXTRACTOR_RECIPE );
        return persistenceModule.evaluateAsPath( recipeValue );

    }
}
