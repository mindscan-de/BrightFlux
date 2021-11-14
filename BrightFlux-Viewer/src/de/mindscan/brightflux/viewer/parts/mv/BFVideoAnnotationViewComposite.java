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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.system.videoannotator.BFVideoObjectEvent;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorComponent;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorUtils;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorVideoObject;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uicommands.UICommandFactory;
import de.mindscan.brightflux.viewer.uievents.UUIDRequestEvent;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class BFVideoAnnotationViewComposite extends Composite implements ProjectRegistryParticipant {

    private static final String ANNOTATION_COLUMN_NAME = VideoAnnotatorComponent.ANNOTATION_COLUMN_NAME;
    private static final String TIMESTAMP_COLUMN_NAME = VideoAnnotatorComponent.TIMESTAMP_COLUMN_NAME;

    private ProjectRegistry projectRegistry;
    private CTabFolder videoObjectTabFolder;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFVideoAnnotationViewComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new BorderLayout( 0, 0 ) );

        Composite composite = new Composite( this, SWT.NONE );
        composite.setLayoutData( BorderLayout.NORTH );
        composite.setLayout( new GridLayout( 5, false ) );

        Button addVideoButton = new Button( composite, SWT.NONE );
        addVideoButton.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( parent.getShell(), "Select Video", FileDescriptions.ANY, path -> {
                    addVideoToProject( path );
                } );
            }
        } );
        addVideoButton.setText( "Add Video ..." );
        addVideoButton.setBounds( 0, 0, 85, 23 );

        Button btnGenerateAndCopy = new Button( composite, SWT.NONE );
        btnGenerateAndCopy.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                VideoAnnotatorComponent videoAnnotatorService = SystemServices.getInstance().getVideoAnnotatorService();
                List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects = videoAnnotatorService.getVideoAnnotationVideoObjects();

                String report = buildFullVideoAnnoationReport( videoAnnotationVideoObjects );

                projectRegistry.getCommandDispatcher().dispatchCommand( UICommandFactory.copyToClipboard( getShell(), report ) );
            }
        } );
        btnGenerateAndCopy.setText( "Generate Report" );

        Label label = new Label( composite, SWT.NONE );
        label.setText( "...." );

        Button btnLoadVideoAnnotations = new Button( composite, SWT.NONE );
        btnLoadVideoAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( parent.getShell(), "Select Video Annotation", FileDescriptions.BF_VIDEO_ANNOTATION,
                                path -> {
                                    loadVideoAnnotationsToProject( path );
                                } );

            }
        } );
        btnLoadVideoAnnotations.setText( "Load Video Annotations ..." );
        btnLoadVideoAnnotations.setBounds( 0, 0, 120, 23 );

        Button btnSaveVideoAnnotations = new Button( composite, SWT.NONE );
        btnSaveVideoAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( parent.getShell(), "Save File...", FileDescriptions.BF_VIDEO_ANNOTATION, path -> {
                    // get the videoObject data from current selected Frame
                    Control tabitem = videoObjectTabFolder.getSelection().getControl();
                    if (tabitem instanceof BFVideoAnnotationSingleVideoViewComposite) {
                        VideoAnnotatorVideoObject videoObject = ((BFVideoAnnotationSingleVideoViewComposite) tabitem).getVideoObject();

                        saveVideoAnnotation( path, videoObject );
                    }
                } );

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
    }

    private void registerVideoObjectCreatedEvent( ProjectRegistry projectRegistry ) {
        BFEventListener listener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFVideoObjectEvent) {
                    VideoAnnotatorVideoObject videoObject = ((BFVideoObjectEvent) event).getVideoObject();

                    addVideoObjectTab( videoObject );
                }
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.VideoAnnotationVideoObjectCreated, listener );
    }

    private void registerVideoObjectSelectRequestEvent( ProjectRegistry projectRegistry ) {
        BFEventListener listener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                UUID requestedUUID = ((UUIDRequestEvent) event).getRequestedUUID();
                requestVideoObjectSelection( requestedUUID );
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.VideoObjectRequestSelectEvent, listener );
    }

    private void loadVideoAnnotationsToProject( Path path ) {
        BFCommand command = DataFrameCommandFactory.loadVideoAnnotationFromFile( path );
        dispatchCommand( command );
    }

    private void addVideoToProject( Path path ) {
        // TODO we need to add processing on how long this video is - maybe we have to ask vlc or ffmpeg for this...
        // This will create a new special video configuration for the current selected (most parent) file.... / and for each video configuration there is 
        // send some command to the annotation component... / with the annotation component

        // TODO: find, whether there is a special annotation file in parallel to the video.
        BFCommand command = DataFrameCommandFactory.loadVideoForAnnotation( path );
        dispatchCommand( command );
    }

    private void saveVideoAnnotation( Path path, VideoAnnotatorVideoObject videoObject ) {
        BFCommand command = DataFrameCommandFactory.saveVideoAnnotationToFile( videoObject, path );
        dispatchCommand( command );
    }

    private void dispatchCommand( BFCommand command ) {
        if (this.projectRegistry != null) {
            this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
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

    // TODO: refactor and extract this to system
    private String buildFullVideoAnnoationReport( List<VideoAnnotatorVideoObject> videoAnnotationVideoObjects ) {
        if (videoAnnotationVideoObjects == null || videoAnnotationVideoObjects.isEmpty()) {
            return "";
        }

        // TODO: use the generator and a template.
        // ReportGeneratorImpl generator = new ReportGeneratorImpl();

        StringBuilder sb = new StringBuilder();

        // build another report from smaller reports.
        for (VideoAnnotatorVideoObject videoAnnotatorVideoObject : videoAnnotationVideoObjects) {
            String partialReport = buildSingleVideoAnnotationReport( videoAnnotatorVideoObject );

            // Use a different template for this...
            // TODO: use the generator
            if (!partialReport.isBlank()) {
                sb.append( "h5. Analysis of [^" ).append( videoAnnotatorVideoObject.getSimpleName() ).append( "]\n\n" );
                sb.append( "The video length is:\n" );
                sb.append( partialReport );
                sb.append( "\n" );
            }
        }

        return sb.toString();
    }

    // TODO: refactor and extract this to system
    private String buildSingleVideoAnnotationReport( VideoAnnotatorVideoObject videoObject ) {
        // TODO: use the generator and a template / template block to build the real report...
        // ReportGeneratorImpl generator = new ReportGeneratorImpl();
        DataFrame currentSelectedDF = videoObject.getVideoAnnotationDataFrame();

        StringBuilder sb = new StringBuilder();

        Iterator<DataFrameRow> currentDFRowsIterator = currentSelectedDF.rowIterator();
        while (currentDFRowsIterator.hasNext()) {
            DataFrameRow dataFrameRow = (DataFrameRow) currentDFRowsIterator.next();

            int timestampInSeconds = (Integer) dataFrameRow.get( TIMESTAMP_COLUMN_NAME );
            String timstamp = VideoAnnotatorUtils.convertSecondsToTimeString( timestampInSeconds );
            String annotationContent = String.valueOf( dataFrameRow.get( ANNOTATION_COLUMN_NAME ) );
            // TODO: we need also the referenced data-rows, so we can align the timestamps to a dataframe, containing different timstamps 

            // TODO: use a named template here instead, also use the report generator
            sb.append( "* " ).append( timstamp ).append( " (video time): " ).append( annotationContent ).append( "\n" );
        }

        return sb.toString();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
