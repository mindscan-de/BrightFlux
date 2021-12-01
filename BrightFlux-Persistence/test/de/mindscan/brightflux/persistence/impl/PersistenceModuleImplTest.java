package de.mindscan.brightflux.persistence.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PersistenceModuleImplTest {

    @Test
    public void testGetNamespaceId_setNameSpaceIDToOne_expectNamespaceIsOne() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 1, "startup" );

        // act
        int result = module.getNamespaceId();

        // assert
        assertThat( result, equalTo( 1 ) );
    }

    @Test
    public void testGetNamespaceId_setNameSpaceIDToTwo_expectNamespaceIsTwo() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 2, "annotator" );

        // act
        int result = module.getNamespaceId();

        // assert
        assertThat( result, equalTo( 2 ) );
    }

    @Test
    public void testGetNamespaceName_setNamespaceNameToStartup_expectNamespaceNameIsStartup() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 1, "startup" );

        // act
        String result = module.getNamespaceName();

        // assert
        assertThat( result, equalTo( "startup" ) );
    }

    @Test
    public void testGetNamespaceName_setNamespaceNameToAnnotator_expectNamespaceNameIsAnnotator() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 2, "annotator" );

        // act
        String result = module.getNamespaceName();

        // assert
        assertThat( result, equalTo( "annotator" ) );
    }

    @Test
    public void testSetIntValue_EarlyUISetIntWidthZero_WidthToBeZero() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 0x100, "early.ui" );
        module.setIntValue( "width", 0 );

        // act
        int result = module.getIntValue( "width" );

        // assert
        assertThat( result, equalTo( 0 ) );
    }

    @Test
    public void testSetIntValue_EarlyUISetIntWidth800_WidthToBe800() throws Exception {
        // arrange
        PersistenceModuleImpl module = new PersistenceModuleImpl( 0x100, "early.ui" );
        module.setIntValue( "width", 800 );

        // act
        int result = module.getIntValue( "width" );

        // assert
        assertThat( result, equalTo( 800 ) );
    }

}
