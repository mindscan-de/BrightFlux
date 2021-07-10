package de.mindscan.brightflux.system.dispatcher;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.system.commands.BFCommand;
import de.mindscan.brightflux.system.dispatcher.EventDispatcher;
import de.mindscan.brightflux.system.dispatcher.SimpleCommandDispatcher;

public class SimpleCommandDispatcherTest {

    @Test
    public void testDispatchCommand_SimpleCommandExecution_CommandExecutedOnce() throws Exception {
        // arrange
        SimpleCommandDispatcher dispatcher = new SimpleCommandDispatcher();
        BFCommand command = Mockito.mock( BFCommand.class, "command" );

        // act
        dispatcher.dispatchCommand( command );

        // assert
        Mockito.verify( command, times( 1 ) ).execute( org.mockito.Matchers.any() );
    }

    @Test
    public void testDispatchCommand_SimpleCommandExecution_TwoEventsDispatched() throws Exception {
        // arrange
        EventDispatcher eventDispatcher = Mockito.mock( EventDispatcher.class, "eventDispatcher" );
        SimpleCommandDispatcher dispatcher = new SimpleCommandDispatcher( eventDispatcher );
        BFCommand command = Mockito.mock( BFCommand.class, "command" );

        // act
        dispatcher.dispatchCommand( command );

        // assert
        Mockito.verify( eventDispatcher, times( 2 ) ).dispatchEvent( Mockito.any() );
    }

    // TODO: test, that tests, that a CommandStartEventIsDispatched
    // TODO: test, that tests, that a CommandFinishedEventIsDispatched
    // TODO: test, that tests, that a CommandStartEventIsDispatched - if command throws exceptions
    // TODO: test, that tests, that a CommandExceptionEventIsDispatched - if command throws exceptions

}
