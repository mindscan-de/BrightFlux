package de.mindscan.brightflux.system.favrecipes;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FavRecipesFileCollectorTest {

    @Test
    public void testCollect() throws Exception {
        // arrange
        FavRecipesComponent recipesComponent = new FavRecipesComponent();
        FavRecipesFileCollector collector = new FavRecipesFileCollector( recipesComponent );

        // act
        collector.collect( Paths.get( "..\\Brightflux-Viewer\\favrecipes" ) );

        // assert
        // cpxuas

    }

    @Test
    public void testCollect_extractIntermediateNodes() throws Exception {
        // arrange
        FavRecipesComponent recipesComponent = new FavRecipesComponent();
        FavRecipesFileCollector collector = new FavRecipesFileCollector( recipesComponent );

        // act
        collector.collect( Paths.get( "..\\Brightflux-Viewer\\favrecipes" ) );

        // assert
        List<String> allIntermediateNodes = recipesComponent.getAllIntermediateNodes();
        for (String string : allIntermediateNodes) {
            System.out.println( string );

        }
    }

    @Test
    public void testCollect_extractLeafNodes() throws Exception {
        // arrange
        FavRecipesComponent recipesComponent = new FavRecipesComponent();
        FavRecipesFileCollector collector = new FavRecipesFileCollector( recipesComponent );

        // act
        collector.collect( Paths.get( "..\\Brightflux-Viewer\\favrecipes" ) );

        // assert
        List<String> allIntermediateNodes = recipesComponent.getAllLeafNodes();
        for (String string : allIntermediateNodes) {
            System.out.println( string );

        }
    }

}
