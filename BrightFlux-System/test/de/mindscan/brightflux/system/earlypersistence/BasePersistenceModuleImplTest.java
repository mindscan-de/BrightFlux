package de.mindscan.brightflux.system.earlypersistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.mindscan.brightflux.persistence.PersistenceModule;
import de.mindscan.brightflux.persistence.PersistenceModuleFactory;

public class BasePersistenceModuleImplTest {

    @Test
    public void testGetStringArrayValue() throws Exception {
        // arrange
        Path path = Paths.get( "../BrightFlux-Viewer/persistence" );
        String namespace = "annotator-plugin";

        PersistenceModule persistenceModule = PersistenceModuleFactory.createModuleInstance( path, namespace );
        BasePersistenceModuleImpl basePersistence = new BasePersistenceModuleImpl( persistenceModule );

        // act
        List<String> result = Arrays.asList( basePersistence.getStringArrayValue( "annotator.snippets" ) );

        // assert
        assertThat( result, containsInAnyOrder( "ddd" ) );
    }

}
