/**
 * 
 * MIT License
 *
 * Copyright (c) 2021 Maxim Gansert, Mindscan
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */
package de.mindscan.brightflux.viewer.parts.mv;

import java.nio.file.Path;
import java.util.UUID;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.videoannotator.VideoAnnotatorComponent;
import de.mindscan.brightflux.plugin.videoannotator.VideoObjectEventListenerAdapter;
import de.mindscan.brightflux.plugin.videoannotator.VideoObjectInformation;
import de.mindscan.brightflux.plugin.videoannotator.commands.VideoAnnotatorCommandFactory;
import de.mindscan.brightflux.plugin.videoannotator.utils.VideoAnnotatorUtils;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.videoannotation.VideoAnnotatorVideoObject;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uicommands.UICommandFactory;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedHandler;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedListenerAdapter;
import de.mindscan.brightflux.viewer.uievents.UUIDRequestEventListenerAdapter;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class BFVideoAnnotationViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;
    private CTabFolder videoObjectTabFolder;
    private VideoAnnotatorComponent videoAnnotatorService;

    String[] templateNames = new String[] { "default", "Jira" };

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFVideoAnnotationViewComposite( Composite parent, int style ) {
        super( parent, style );

        this.videoAnnotatorService = SystemServices.getInstance().getService( VideoAnnotatorComponent.class );

        setLayout( new BorderLayout( 0, 0 ) );

        Composite composite = new Composite( this, SWT.NONE );
        composite.setLayoutData( BorderLayout.NORTH );
        composite.setLayout( new GridLayout( 6, false ) );

        Button addVideoButton = new Button( composite, SWT.NONE );
        addVideoButton.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                executeIntentAddVideo( parent.getShell() );
            }
        } );
        addVideoButton.setText( "Add Video ..." );
        addVideoButton.setBounds( 0, 0, 85, 23 );

        Button btnGenerateAndCopy = new Button( composite, SWT.NONE );
        btnGenerateAndCopy.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                executeIntentCopyToClipboardVideoAnnotationReport( parent.getShell() );
            }
        } );
        btnGenerateAndCopy.setText( "Generate Report" );

        ComboViewer comboViewer = new ComboViewer( composite, SWT.READ_ONLY );
        Combo combo = comboViewer.getCombo();

        comboViewer.setContentProvider( ArrayContentProvider.getInstance() );

        comboViewer.setInput( templateNames );

        Label label = new Label( composite, SWT.NONE );
        label.setText( "...." );

        Button btnLoadVideoAnnotations = new Button( composite, SWT.NONE );
        btnLoadVideoAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                executeIntentLoadVideoAnnotation( parent.getShell() );
            }
        } );
        btnLoadVideoAnnotations.setText( "Load Video Annotations ..." );
        btnLoadVideoAnnotations.setBounds( 0, 0, 120, 23 );

        Button btnSaveVideoAnnotations = new Button( composite, SWT.NONE );
        btnSaveVideoAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                executeIntentSaveVideoAnnotation( parent.getShell() );
            }
        } );
        btnSaveVideoAnnotations.setText( "Save Video Annotations ..." );
        btnSaveVideoAnnotations.setBounds( 0, 0, 121, 23 );

        Composite composite_1 = new Composite( this, SWT.NONE );
        composite_1.setLayoutData( BorderLayout.CENTER );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        videoObjectTabFolder = new CTabFolder( composite_1, SWT.BORDER );
        videoObjectTabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {
            @Override
            public void close( CTabFolderEvent event ) {
                if (event.item instanceof CTabItem) {
                    Control control = ((CTabItem) event.item).getControl();
                    if (control instanceof BFVideoAnnotationSingleVideoViewComposite) {
                        ((BFVideoAnnotationSingleVideoViewComposite) control).closeVideoObject();
                    }
                }
                super.close( event );
            }
        } );
        videoObjectTabFolder.setTabPosition( SWT.BOTTOM );
        videoObjectTabFolder.setSelectionBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT ) );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerVideoObjectCreatedEvent( projectRegistry );
        registerVideoObjectSelectRequestEvent( projectRegistry );
        registerDataFrameRowSelectionEvent( projectRegistry );
    }

    private void registerDataFrameRowSelectionEvent( ProjectRegistry projectRegistry2 ) {
        DataFrameRowSelectedListenerAdapter listener = new DataFrameRowSelectedListenerAdapter() {
            @Override
            public void handleDataFrameRowSelected( DataFrameRow selectedRow ) {
                delegateDataFrameRowSelectionToCurrentVideoTab( selectedRow );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameRowSelectedEvent, listener );
    }

    protected void delegateDataFrameRowSelectionToCurrentVideoTab( DataFrameRow selectedRow ) {
        CTabItem currentSelectedSingleVideoObject = videoObjectTabFolder.getSelection();
        if (currentSelectedSingleVideoObject == null) {
            return;
        }

        Control control = currentSelectedSingleVideoObject.getControl();
        if (control instanceof DataFrameRowSelectedHandler) {
            ((DataFrameRowSelectedHandler) control).handleDataFrameRowSelected( selectedRow );
        }
    }

    private void registerVideoObjectCreatedEvent( ProjectRegistry projectRegistry ) {
        VideoObjectEventListenerAdapter listener = new VideoObjectEventListenerAdapter() {
            @Override
            public void handleVdideoObject( VideoAnnotatorVideoObject videoObject ) {
                addVideoObjectTab( videoObject );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.VideoAnnotationVideoObjectCreated, listener );
    }

    private void registerVideoObjectSelectRequestEvent( ProjectRegistry projectRegistry ) {
        UUIDRequestEventListenerAdapter listener = new UUIDRequestEventListenerAdapter() {
            @Override
            public void handleUUIDRequest( UUID requestedUUID ) {
                requestVideoObjectSelection( requestedUUID );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.VideoObjectRequestSelectEvent, listener );
    }

    private void addVideoObjectTab( VideoAnnotatorVideoObject videoObject ) {
        CTabItem item = addTabItem( this.videoObjectTabFolder, videoObject );
        videoObjectTabFolder.setSelection( item );
    }

    private CTabItem addTabItem( CTabFolder tabFolder, VideoAnnotatorVideoObject videoObject ) {
        CTabItem tbtmNewItem = new CTabItem( tabFolder, SWT.NONE );
        tbtmNewItem.setShowClose( true );
        tbtmNewItem.setText( videoObject.getSimpleName() );
        tbtmNewItem.setToolTipText( videoObject.getVideoObjectPath().toString() );

        BFVideoAnnotationSingleVideoViewComposite composite = new BFVideoAnnotationSingleVideoViewComposite( tabFolder, SWT.NONE );

        // the other mechanism won't finish the registration process... That trigger would have to be done manually...
        ((ProjectRegistryParticipant) composite).setProjectRegistry( projectRegistry );

        composite.setVideoObject( videoObject );
        tbtmNewItem.setControl( composite );

        return tbtmNewItem;
    }

    private void requestVideoObjectSelection( UUID requestedUUID ) {
        if (requestedUUID == null) {
            return;
        }

        CTabItem[] items = videoObjectTabFolder.getItems();
        if (items == null) {
            return;
        }

        for (int i = 0; i < items.length; i++) {
            if (selectIfRequestedUuidMatches( requestedUUID, items[i] )) {
                return;
            }
        }
    }

    private boolean selectIfRequestedUuidMatches( UUID requestedUUID, CTabItem cTabItem ) {
        Control currentControl = cTabItem.getControl();

        if (!(currentControl instanceof BFVideoAnnotationSingleVideoViewComposite)) {
            return false;
        }

        BFVideoAnnotationSingleVideoViewComposite singleVideoViewComposite = (BFVideoAnnotationSingleVideoViewComposite) currentControl;

        if (requestedUUID.equals( singleVideoViewComposite.getVideoObjectUUID() )) {
            videoObjectTabFolder.setSelection( cTabItem );
            return true;
        }

        return false;
    }

    public void executeIntentAddVideo( Shell shell ) {
        BrightFluxFileDialogs.openRegularFileAndConsumePath( shell, "Select Video", FileDescriptions.ANY, path -> {
            addVideoToProject( path );
        } );
    }

    private void addVideoToProject( Path path ) {
        // we need to add processing on how long this video is - maybe we have to ask vlc or ffmpeg for this...
        VideoObjectInformation videoObjectInformation = VideoAnnotatorUtils.getVideoObjectInformation( videoAnnotatorService.getFFProbePath(), path );

        // This will create a new special video configuration for the current selected (most parent) file.... / and for each video configuration there is 
        // send some command to the annotation component... / with the annotation component

        // TODO: find, whether there is a special annotation file in parallel to the video.
        dispatchCommand( VideoAnnotatorCommandFactory.loadVideoForAnnotation( path, videoObjectInformation ) );
    }

    public void executeIntentLoadVideoAnnotation( Shell shell ) {
        BrightFluxFileDialogs.openRegularFileAndConsumePath( shell, "Select Video Annotation", FileDescriptions.BF_VIDEO_ANNOTATION, path -> {
            dispatchCommand( VideoAnnotatorCommandFactory.loadVideoAnnotationFromFile( path ) );
        } );
    }

    public void executeIntentCopyToClipboardVideoAnnotationReport( Shell shell ) {
        String report = videoAnnotatorService.createFullReport( "Jira", 0 );

        dispatchCommand( UICommandFactory.copyToClipboard( shell, report ) );
    }

    public void executeIntentSaveVideoAnnotation( Shell shell ) {
        BrightFluxFileDialogs.saveRegularFileAndConsumePath( shell, "Save File...", FileDescriptions.BF_VIDEO_ANNOTATION, path -> {
            // get the videoObject data from current selected Frame
            Control tabitem = videoObjectTabFolder.getSelection().getControl();
            if (tabitem instanceof BFVideoAnnotationSingleVideoViewComposite) {
                VideoAnnotatorVideoObject videoObject = ((BFVideoAnnotationSingleVideoViewComposite) tabitem).getVideoObject();

                saveVideoAnnotation( path, videoObject );
            }
        } );
    }

    private void saveVideoAnnotation( Path path, VideoAnnotatorVideoObject videoObject ) {
        dispatchCommand( VideoAnnotatorCommandFactory.saveVideoAnnotationToFile( videoObject, path ) );
    }

    private void dispatchCommand( BFCommand command ) {
        if (this.projectRegistry != null) {
            this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
