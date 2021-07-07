package de.mindscan.brightflux.viewer.dispatcher;

import org.junit.jupiter.api.Test;

public class SimpleEventDispatcherTest {

    @Test
    public void testDispatchEvent_dispatchNullEvent_expectNoException() throws Exception {
        // arrange
        SimpleEventDispatcher dispatcher = new SimpleEventDispatcher();

        // act
        // assert
        dispatcher.dispatchEvent( null );
    }

}
