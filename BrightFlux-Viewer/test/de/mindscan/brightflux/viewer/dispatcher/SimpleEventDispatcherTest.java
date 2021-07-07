package de.mindscan.brightflux.viewer.dispatcher;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.viewer.events.BFEvent;
import de.mindscan.brightflux.viewer.events.BFEventListener;
import de.mindscan.brightflux.viewer.events.CommandExecutionFinishedEvent;
import de.mindscan.brightflux.viewer.events.CommandExecutionStartedEvent;

public class SimpleEventDispatcherTest {

    @Test
    public void testDispatchEvent_dispatchNullEvent_expectNoException() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();

        // act + assert
        dispatcher.dispatchEvent( null );
    }

    @Test
    public void testDispatchEvent_dispatchNonRegisteredClass_expectNoException() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();
        BFEvent unregisterdEventType = Mockito.mock( CommandExecutionStartedEvent.class, "unregisterdEventType" );

        // act + assert
        dispatcher.dispatchEvent( unregisterdEventType );
    }

    @Test
    public void testDispatchEvent_dispatchRegisteredEventClass_invokedHandleEventOnListener() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "unregisterdEventType" );
        BFEventListener listener = Mockito.mock( BFEventListener.class, "listener" );

        dispatcher.registerEventListener( registerdEvent.getClass(), listener );

        // act
        dispatcher.dispatchEvent( registerdEvent );

        // assert
        Mockito.verify( listener, times( 1 ) ).handleEvent( registerdEvent );
    }

    @Test
    public void testDispatchEvent_RegisterDifferentEventClassButDispatchUnregistered_handlerNotInvoked() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "unregisterdEventType" );
        BFEvent unRegisterdEvent = Mockito.mock( CommandExecutionFinishedEvent.class, "unregisterdEventType" );
        BFEventListener listener = Mockito.mock( BFEventListener.class, "listener" );

        dispatcher.registerEventListener( registerdEvent.getClass(), listener );

        // act
        dispatcher.dispatchEvent( unRegisterdEvent );

        // assert
        Mockito.verify( listener, Mockito.never() ).handleEvent( unRegisterdEvent );
    }

}
