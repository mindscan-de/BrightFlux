package de.mindscan.brightflux.system.videoannotator;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.system.videoannotator.events.VideoAnnotationVideoObjectClosedEvent;
import de.mindscan.brightflux.system.videoannotator.events.VideoAnnotationVideoObjectCreatedEvent;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;
import de.mindscan.brightflux.videoannotation.impl.VideoAnnotatorVideoObjectImpl;

public class VideoObjectEventListenerAdapterTest {

    @Test
    public void testHandleEventVideoAnnotatorVideoObject_VideoObjectClosedEvent_expectInvocationWithVAVOEventInstance() throws Exception {
        // arrange
        VideoAnnotatorVideoObjectImpl object2 = Mockito.mock( VideoAnnotatorVideoObjectImpl.class, "object2" );
        VideoAnnotationVideoObjectClosedEvent videoObjectEvent = new VideoAnnotationVideoObjectClosedEvent( object2 );

        VideoObjectEventListenerAdapter adapter = new VideoObjectEventListenerAdapter();
        VideoObjectEventListenerAdapter adapterSpy = Mockito.spy( adapter );

        // act
        adapterSpy.handleEvent( videoObjectEvent );

        // assert
        Mockito.verify( adapterSpy, times( 1 ) ).handleVdideoObject( object2 );
    }

    @Test
    public void testHandleEventVideoAnnotatorVideoObject_VideoObjectCreatedEvent_expectInvocationWithVAVOEventInstance() throws Exception {
        // arrange
        VideoAnnotatorVideoObjectImpl object2 = Mockito.mock( VideoAnnotatorVideoObjectImpl.class, "object2" );
        VideoAnnotationVideoObjectCreatedEvent videoObjectEvent = new VideoAnnotationVideoObjectCreatedEvent( object2 );

        VideoObjectEventListenerAdapter adapter = new VideoObjectEventListenerAdapter();
        VideoObjectEventListenerAdapter adapterSpy = Mockito.spy( adapter );

        // act
        adapterSpy.handleEvent( videoObjectEvent );

        // assert
        Mockito.verify( adapterSpy, times( 1 ) ).handleVdideoObject( object2 );
    }

    @Test
    public void testHandleEventVideoAnnotatorVideoObject_NullEventNotOfBFVideoObjectEventType_expectNoInvocation() throws Exception {
        // arrange
        BFEvent nullEvent = Mockito.mock( BFEvent.class, "nullEvent" );

        VideoObjectEventListenerAdapter adapter = new VideoObjectEventListenerAdapter();
        VideoObjectEventListenerAdapter adapterSpy = Mockito.spy( adapter );

        // act
        adapterSpy.handleEvent( nullEvent );

        // assert
        Mockito.verify( adapterSpy, Mockito.never() ).handleVdideoObject( Mockito.any( VideoAnnotatorVideoObject.class ) );
    }

    @Test
    public void testHandleEventVideoAnnotatorVideoObject_EventIsOfBFVideoObjectEventType_expectOneInvocation() throws Exception {
        // arrange
        BFVideoObjectEvent bfVideoObjectEvent = Mockito.mock( BFVideoObjectEvent.class, "bfVideoObjectEvent" );

        VideoObjectEventListenerAdapter adapter = new VideoObjectEventListenerAdapter();
        VideoObjectEventListenerAdapter adapterSpy = Mockito.spy( adapter );

        // act
        adapterSpy.handleEvent( bfVideoObjectEvent );

        // assert
        Mockito.verify( adapterSpy, Mockito.times( 1 ) ).handleVdideoObject( Mockito.any( VideoAnnotatorVideoObject.class ) );
    }

}
