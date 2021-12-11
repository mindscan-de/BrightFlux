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
package de.mindscan.brightflux.viewer.main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryImpl;
import de.mindscan.brightflux.system.annotator.AnnotatorComponent;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.earlypersistence.EarlyPersistenceComponent;
import de.mindscan.brightflux.system.favrecipes.FavRecipesActivator;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.highlighter.HighlighterCallbacks;
import de.mindscan.brightflux.system.reportgenerator.ReportGeneratorComponent;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorComponent;
import de.mindscan.brightflux.viewer.parts.MainProjectComposite;
import de.mindscan.brightflux.viewer.parts.MultiViewComposite;
import de.mindscan.brightflux.viewer.parts.OutlineViewComposite;
import de.mindscan.brightflux.viewer.parts.ProjectViewComposite;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;

/**
 * 
 */
public class BrightFluxViewerMainAppTwo {

    // TODO refactor constant this to the right place
    public static final String FFPROBE_PATH_KEY = "ffprobe.path";

    protected Shell shellBFViewerMainApp;
    private ProjectRegistry projectRegistry = new ProjectRegistryImpl();

    // Business Level code / component.
    private AnnotatorComponent annotatorComponent = new AnnotatorComponent();

    /**
     * Launch the application.
     * @param args
     */
    public static void main( String[] args ) {
        try {

            // STARTUP : System Services
            SystemServices systemServices = SystemServices.getInstance();

            // STARTUP : Early Persistence data (avoid hard coded dependencies)
            EarlyPersistenceComponent earlyPersistence = new EarlyPersistenceComponent();
            earlyPersistence.initEarlyPersistence();
            systemServices.setEarlyPersistence( earlyPersistence );

            // TODO: get early startup configuration from early persistence configuration, start components as bundles 

            // STARTUP : Register Favorite Recipes Service + Collect Favorite Recipes
            FavRecipesActivator favrecipesActivator = new FavRecipesActivator();
            favrecipesActivator.start( systemServices );

            // STARTUP : Register Video Annotation Service
            VideoAnnotatorComponent videoAnnotatorComponent = new VideoAnnotatorComponent();
            // TODO 
            // - set a fully configured persistence module instead, where the component can do everything in its own
            // - The component can also register listeners if it wants to be informed about changes (e.g. config pages)   
            videoAnnotatorComponent.setFFProbePath( earlyPersistence.getPropertyAsPath( FFPROBE_PATH_KEY ) );
            systemServices.setVideoAnnotationService( videoAnnotatorComponent );

            // STARTUP : Register Report Generator Service
            ReportGeneratorComponent reportGeneratorService = new ReportGeneratorComponent();
            systemServices.setReportGeneratorService( reportGeneratorService );

            // TODO: the configuration can be distributed with configuration events -> or with a 

            // TODO: Implement a startup component, such that some of the components can be initialized before
            //       first usage, (e.g. annotation, highlight, QueryCallbacks, project registry, system services,  ....)
            // 
            // initialize the favorite recipes and provide them in a tree structure, which is then used for the
            // dataframe view.

            BrightFluxViewerMainAppTwo window = new BrightFluxViewerMainAppTwo();
            window.open();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open() {
        Display display = Display.getDefault();
        createContents();
        shellBFViewerMainApp.open();
        shellBFViewerMainApp.layout();
        while (!shellBFViewerMainApp.isDisposed()) {
            try {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shellBFViewerMainApp = new Shell();
        shellBFViewerMainApp.setSize( 853, 642 );
        shellBFViewerMainApp.setText( "BrightFlux-Viewer 0.0.1.M1" );
        shellBFViewerMainApp.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        createMainMenu();

        Composite composite = new Composite( shellBFViewerMainApp, SWT.NONE );
        composite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm_1 = new SashForm( composite, SWT.NONE );

        SashForm sashForm_2 = new SashForm( sashForm_1, SWT.VERTICAL );

        ProjectViewComposite projectViewComposite = new ProjectViewComposite( sashForm_2, SWT.NONE );

        OutlineViewComposite outlineViewComposite = new OutlineViewComposite( sashForm_2, SWT.NONE );
        sashForm_2.setWeights( new int[] { 375, 218 } );

        SashForm sashForm = new SashForm( sashForm_1, SWT.VERTICAL );

        MainProjectComposite mainProjectComposite = new MainProjectComposite( sashForm, SWT.NONE );

        MultiViewComposite multiViewComposite = new MultiViewComposite( sashForm, SWT.NONE );
        sashForm.setWeights( new int[] { 432, 161 } );
        sashForm_1.setWeights( new int[] { 148, 694 } );

        // This is still not nice, but good enough for now
        // we might implement a patched classloader or some DependenyInjector mechanism, since the app is 
        // basically also a ProjectRegistryParticipant and should not provide the truth to everyone else top down.
        projectRegistry.registerParticipant( projectViewComposite );
        projectRegistry.registerParticipant( mainProjectComposite );
        projectRegistry.registerParticipant( multiViewComposite );
        projectRegistry.registerParticipant( outlineViewComposite );

        // init some business logic comes here too
        projectRegistry.registerParticipant( annotatorComponent );
        // Don't like it...
        projectRegistry.registerParticipant( SystemServices.getInstance().getVideoAnnotatorService() );

        HighlighterCallbacks.initializeWithProjectRegistry( projectRegistry );

        projectRegistry.completeParticipantRegistration();
    }

    private void createMainMenu() {
        Menu menu = new Menu( shellBFViewerMainApp, SWT.BAR );
        shellBFViewerMainApp.setMenuBar( menu );

        MenuItem mntmFile = new MenuItem( menu, SWT.CASCADE );
        mntmFile.setText( "File" );

        Menu menu_1 = new Menu( mntmFile );
        mntmFile.setMenu( menu_1 );

        MenuItem mntmLoadFile = new MenuItem( menu_1, SWT.NONE );
        mntmLoadFile.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( shellBFViewerMainApp, "Select file", //
                                FileDescriptions.CSV, //
                                path -> {
                                    BFCommand ingestCommand = DataFrameCommandFactory.ingestFile( path );
                                    projectRegistry.getCommandDispatcher().dispatchCommand( ingestCommand );
                                } );
            }
        } );
        mntmLoadFile.setText( "Load CSV File ..." );

        MenuItem mntmSpecialRawOption = new MenuItem( menu_1, SWT.NONE );
        mntmSpecialRawOption.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( shellBFViewerMainApp, "Select file", //
                                FileDescriptions.RAW_ANY, //
                                path -> {
                                    BFCommand ingestCommand = DataFrameCommandFactory.ingestSpecialRaw( path );
                                    projectRegistry.getCommandDispatcher().dispatchCommand( ingestCommand );
                                } );
            }
        } );
        mntmSpecialRawOption.setText( "Load Raw File ..." );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmGarbageCollector = new MenuItem( menu_1, SWT.NONE );
        mntmGarbageCollector.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // Don't like it... 
                System.gc();
            }
        } );
        mntmGarbageCollector.setText( "Garbage Collector (debug)" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmExit = new MenuItem( menu_1, SWT.PUSH );
        mntmExit.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                shellBFViewerMainApp.close();
            }
        } );
        mntmExit.setText( "Exit" );

        MenuItem mntmTools = new MenuItem( menu, SWT.CASCADE );
        mntmTools.setText( "Tools" );

        Menu menu_2 = new Menu( mntmTools );
        mntmTools.setMenu( menu_2 );

        MenuItem mntmExtract = new MenuItem( menu_2, SWT.NONE );
        mntmExtract.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                BrightFluxFileDialogs.openRegularFileAndConsumePath( shellBFViewerMainApp, "Select file", //
                                FileDescriptions.ANY, //
                                path -> {
                                    BFCommand expandCommand = DataFrameCommandFactory.expandFile( path );
                                    projectRegistry.getCommandDispatcher().dispatchCommand( expandCommand );
                                } );
            }
        } );
        mntmExtract.setText( "Extract..." );
    }
}
