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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This component detects all recipes in a favorites folder and collect these 
 */
public class FavRecipesComponent {

    private final static String ROOT = "";
    /**
     * favoriteRecipes only contains leaf-nodes of the tree
     */
    private Map<String, Path> favoriteRecipes = new TreeMap<>();

    /**
     * this contains the Nodeset of non recipes.
     */
    private Set<String> intermediateNodes = new TreeSet<>();

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
        // only put recipes in when the parent key is no leaf node
        if (checkKeyParentIsLeaf( key )) {
            throw new IllegalArgumentException(
                            "A parent of given key '" + key + "' is already a Leaf Node. Try renaming this recipe: " + pathToRecipe.toString() );
        }

        if (checkKeyIsInterMediateNode( key )) {
            throw new IllegalArgumentException(
                            "This key '" + key + "' is part of another registered recipe. Try renaming this recipe: " + pathToRecipe.toString() );
        }

        // register all parents as intermediate nodes
        registerNodeAndParentsAsIntermediateNodes( FavRecipesKeyUtils.calculateParent( key ) );

        // set leaf node path
        favoriteRecipes.put( key, pathToRecipe );
    }

    private void registerNodeAndParentsAsIntermediateNodes( String key ) {
        if (ROOT.equals( key ) || checkKeyIsInterMediateNode( key )) {
            return;
        }

        intermediateNodes.add( key );

        registerNodeAndParentsAsIntermediateNodes( FavRecipesKeyUtils.calculateParent( key ) );
    }

    private boolean checkKeyIsInterMediateNode( String key ) {
        return intermediateNodes.contains( key );
    }

    private boolean checkKeyParentIsLeaf( String key ) {
        String parentKey = FavRecipesKeyUtils.calculateParent( key );
        if (ROOT.equals( parentKey )) {
            return false;
        }

        if (favoriteRecipes.containsKey( parentKey )) {
            return true;
        }

        return checkKeyParentIsLeaf( parentKey );
    }

    // To build the cascading menu structure
    public List<String> getAllIntermediateNodes() {
        return intermediateNodes.stream().collect( Collectors.toList() );
    }

    // To build the menu item structure
    public List<String> getAllLeafNodes() {
        return favoriteRecipes.keySet().stream().collect( Collectors.toList() );
    }

}
