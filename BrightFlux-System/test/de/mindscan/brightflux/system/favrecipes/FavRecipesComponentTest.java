package de.mindscan.brightflux.system.favrecipes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class FavRecipesComponentTest {

    @Test
    public void testGetFavorite_CTorOnlyEmptyFavorites_returnsNull() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();

        // act
        Path result = favRecipesComponent.getFavorite( "a" );

        // assert
        assertThat( result, nullValue() );
    }

}
