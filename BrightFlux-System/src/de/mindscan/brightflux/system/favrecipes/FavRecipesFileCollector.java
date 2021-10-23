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

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Actually it is a file crawler
 */
public class FavRecipesFileCollector {

    private FavRecipesComponent favoriteRecipes;

    /**
     * 
     */
    public FavRecipesFileCollector( FavRecipesComponent favoriteRecipes ) {
        this.favoriteRecipes = favoriteRecipes;
    }

    public void collect( Path directory ) {
        try {
            FileVisitOption options = FileVisitOption.FOLLOW_LINKS;

            List<Path> files = Files.walk( directory, 1, options )//
                            .filter( p -> !Files.isDirectory( p ) )//
                            .filter( p -> p.toString().endsWith( ".bfrecipe" ) )//
                            .collect( Collectors.toList() );

            for (Path file : files) {
                System.out.println( "Found a recipe. '" + file.toString() + "'" );
                String fileName = file.getFileName().toString();

                String[] splitFileName = fileName.split( "\\.bfrecipe" );
                if (splitFileName == null) {
                    continue;
                }

                String[] target = splitFileName[0].split( "\\." );
                if (target == null) {
                    continue;
                }

                try {
                    String key = favoriteRecipes.buildKey( target );
                    favoriteRecipes.addFavorite( key, file );
                }
                catch (Exception e) {
                    System.out.println( "ERROR Favorites Recipes. " + file.toString() + " ignored, because this violates Naming conventions." );
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
