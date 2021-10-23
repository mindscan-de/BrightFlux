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

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * This component detects all recipes in a favorites folder and collect these 
 */
public class FavRecipesComponent {

    /**
     * favoriteRecipes only contains leaf-nodes of the tree
     */
    private Map<String, Path> favoriteRecipes = new TreeMap<>();

    /**
     * 
     */
    public FavRecipesComponent() {
        favoriteRecipes = new TreeMap<>();
    }

    public Path getFavorite( String key ) {
        return favoriteRecipes.get( key );
    }

    public boolean isFavorite( String key ) {
        return favoriteRecipes.containsKey( key );
    }

    public void addFavorite( String key, Path pathToRecipe ) {

        // TODO: only put recipes in when the key parent is no leaf node
        favoriteRecipes.put( key, pathToRecipe );
    }

}
