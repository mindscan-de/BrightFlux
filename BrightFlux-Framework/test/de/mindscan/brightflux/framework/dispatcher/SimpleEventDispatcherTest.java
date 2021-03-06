package de.mindscan.brightflux.framework.dispatcher;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.events.CommandExecutionFinishedEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionStartedEvent;

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
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "registerdEventType" );
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
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "registerdEventType" );
        BFEvent unRegisterdEvent = Mockito.mock( CommandExecutionFinishedEvent.class, "unregisterdEventType" );
        BFEventListener listener = Mockito.mock( BFEventListener.class, "listener" );

        dispatcher.registerEventListener( registerdEvent.getClass(), listener );

        // act
        dispatcher.dispatchEvent( unRegisterdEvent );

        // assert
        Mockito.verify( listener, Mockito.never() ).handleEvent( unRegisterdEvent );
    }

    @Test
    public void testDispatchEvent_dispatchRegisteredEventClassToTwoListeners_invokedHandleEventOnListenerOne() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "registerdEventType" );
        BFEventListener listener1 = Mockito.mock( BFEventListener.class, "listener1" );
        BFEventListener listener2 = Mockito.mock( BFEventListener.class, "listener2" );

        dispatcher.registerEventListener( registerdEvent.getClass(), listener1 );
        dispatcher.registerEventListener( registerdEvent.getClass(), listener2 );

        // act
        dispatcher.dispatchEvent( registerdEvent );

        // assert
        Mockito.verify( listener1, times( 1 ) ).handleEvent( registerdEvent );
    }

    @Test
    public void testDispatchEvent_dispatchRegisteredEventClassToTwoListeners_invokedHandleEventOnListenerTwo() throws Exception {
        // arrange
        EventDispatcher dispatcher = new SimpleEventDispatcher();
        BFEvent registerdEvent = Mockito.mock( CommandExecutionStartedEvent.class, "registerdEventType" );
        BFEventListener listener1 = Mockito.mock( BFEventListener.class, "listener1" );
        BFEventListener listener2 = Mockito.mock( BFEventListener.class, "listener2" );

        dispatcher.registerEventListener( registerdEvent.getClass(), listener1 );
        dispatcher.registerEventListener( registerdEvent.getClass(), listener2 );

        // act
        dispatcher.dispatchEvent( registerdEvent );

        // assert
        Mockito.verify( listener2, times( 1 ) ).handleEvent( registerdEvent );
    }

}
