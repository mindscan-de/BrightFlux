package de.mindscan.brightflux.system.favrecipes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    @Test
    public void testAddFavorite_AddFavoriteRetrieveBack_returnsSameFavorite() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path aPath = Mockito.mock( Path.class, "aPath" );

        // act
        favRecipesComponent.addFavorite( "a", aPath );

        // assert
        Path result = favRecipesComponent.getFavorite( "a" );
        assertThat( result, is( sameInstance( aPath ) ) );
    }

    @Test
    public void testAddFavorite_AddSecondLayerPathRetrieveBack_returnsSameFavorite() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );

        // act
        favRecipesComponent.addFavorite( "a::a", a_aPath );

        // assert
        Path result = favRecipesComponent.getFavorite( "a::a" );
        assertThat( result, is( sameInstance( a_aPath ) ) );
    }

    @Test
    public void testAddFavorite_AddTwoSecondLayerPathRetrieveBackAA_returnsAAFavorite() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        Path a_bPath = Mockito.mock( Path.class, "a_bPath" );

        // act
        favRecipesComponent.addFavorite( "a::a", a_aPath );
        favRecipesComponent.addFavorite( "a::b", a_bPath );

        // assert
        Path result = favRecipesComponent.getFavorite( "a::a" );
        assertThat( result, is( sameInstance( a_aPath ) ) );
    }

    @Test
    public void testAddFavorite_AddTwoSecondLayerPathRetrieveBackAB_returnsABFavorite() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        Path a_bPath = Mockito.mock( Path.class, "a_bPath" );

        // act
        favRecipesComponent.addFavorite( "a::a", a_aPath );
        favRecipesComponent.addFavorite( "a::b", a_bPath );

        // assert
        Path result = favRecipesComponent.getFavorite( "a::b" );
        assertThat( result, is( sameInstance( a_bPath ) ) );
    }

    @Test
    public void testAddFavorite_AddSecondLayerPathAddIntermediateNodeAsLeafNode_throwsIllegalArgumentException() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        Path aPath = Mockito.mock( Path.class, "aPath" );

        // act
        favRecipesComponent.addFavorite( "a::a", a_aPath );

        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            favRecipesComponent.addFavorite( "a", aPath );
        } );
    }

    @Test
    public void testAddFavorite_AddSThirdLayerPathAddIntermediateNodeAsLeafNode_throwsIllegalArgumentException() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_a_aPath = Mockito.mock( Path.class, "a_a_aPath" );
        Path aPath = Mockito.mock( Path.class, "aPath" );

        // act
        favRecipesComponent.addFavorite( "a::a::a", a_a_aPath );

        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            favRecipesComponent.addFavorite( "a", aPath );
        } );
    }

    @Test
    public void testAddFavorite_AddFirstLayerPathAddCollidingPrefixNodeAsLeafNode_throwsIllegalArgumentException() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        Path aPath = Mockito.mock( Path.class, "aPath" );

        // act
        favRecipesComponent.addFavorite( "a", aPath );

        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            favRecipesComponent.addFavorite( "a::a", a_aPath );
        } );
    }

    @Test
    public void testAddFavorite_AddFirstLayerPathAddCollidingPrefixFhirdLevelNodeAsLeafNode_throwsIllegalArgumentException() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_a_aPath = Mockito.mock( Path.class, "a_a_aPath" );
        Path aPath = Mockito.mock( Path.class, "aPath" );

        // act
        favRecipesComponent.addFavorite( "a", aPath );

        // assert
        assertThrows( IllegalArgumentException.class, () -> {
            favRecipesComponent.addFavorite( "a::a::a", a_a_aPath );
        } );
    }

    @Test
    public void testGetAllIntermediateNodes_LevelOneItem_IntermediateNodesEmpty() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path aPath = Mockito.mock( Path.class, "aPath" );
        favRecipesComponent.addFavorite( "a", aPath );

        // act
        List<String> result = favRecipesComponent.getAllIntermediateNodes();

        // assert
        assertThat( result, empty() );
    }

    @Test
    public void testGetAllIntermediateNodes_LevelTwoItem_IntermediateNodesCollectionSizeIsOne() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        favRecipesComponent.addFavorite( "a::a", a_aPath );

        // act
        List<String> result = favRecipesComponent.getAllIntermediateNodes();

        // assert
        assertThat( result, hasSize( 1 ) );
    }

    @Test
    public void testGetAllIntermediateNodes_LevelTwoItem_ContainsOnlyOneIntermediateNode() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_aPath = Mockito.mock( Path.class, "a_aPath" );
        favRecipesComponent.addFavorite( "a::a", a_aPath );

        // act
        List<String> result = favRecipesComponent.getAllIntermediateNodes();

        // assert
        assertThat( result, containsInAnyOrder( "a" ) );
    }

    @Test
    public void testGetAllIntermediateNodes_LevelThreeItem_IntermediateNodesCollectionSizeIsTwo() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_a_aPath = Mockito.mock( Path.class, "a_a_aPath" );
        favRecipesComponent.addFavorite( "a::a::a", a_a_aPath );

        // act
        List<String> result = favRecipesComponent.getAllIntermediateNodes();

        // assert
        assertThat( result, hasSize( 2 ) );
    }

    @Test
    public void testGetAllIntermediateNodes_LevelThreeItem_ContainsBothIntermediateNodes() throws Exception {
        // arrange
        FavRecipesComponent favRecipesComponent = new FavRecipesComponent();
        Path a_a_aPath = Mockito.mock( Path.class, "a_a_aPath" );
        favRecipesComponent.addFavorite( "a::a::a", a_a_aPath );

        // act
        List<String> result = favRecipesComponent.getAllIntermediateNodes();

        // assert
        assertThat( result, containsInAnyOrder( "a", "a::a" ) );
    }

}
