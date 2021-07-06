package de.mindscan.brightflux.viewer.dispatcher;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.viewer.commands.BFCommand;

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

}
