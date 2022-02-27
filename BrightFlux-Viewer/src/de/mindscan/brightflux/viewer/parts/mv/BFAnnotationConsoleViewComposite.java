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

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.annotator.AnnotatorComponent;
import de.mindscan.brightflux.plugin.annotator.commands.AnnotatorCommandFactory;
import de.mindscan.brightflux.system.events.DataFrameEventListenerAdapter;
import de.mindscan.brightflux.system.filedescription.FileDescriptions;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.parts.ui.BrightFluxFileDialogs;
import de.mindscan.brightflux.viewer.uicommands.UICommandFactory;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedListenerAdapter;

/**
 * 
 */
public class BFAnnotationConsoleViewComposite extends Composite implements ProjectRegistryParticipant {

    private static final String ANNOTATION_COLUMN_NAME = AnnotatorComponent.ANNOTATION_COLUMN_NAME;

    private Table table;

    private DataFrame currentSelectedDataFrame = null;
    private DataFrameRow currentSelectedDataFrameRow = null;

    private StyledText annotatedStyledText;

    private ProjectRegistry projectRegistry;

    private Button btnClearAnnotations;

    private Shell shell;

    private AnnotatorComponent annotatorService = SystemServices.getInstance().getService( AnnotatorComponent.class );

    private Combo reportSelectionCombo;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFAnnotationConsoleViewComposite( Composite parent, int style ) {
        super( parent, style );

        shell = parent.getShell();

        createContent();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        registerAnnotationDataFrameCreatedEvent( projectRegistry );
        registerDataFrameSelectedEvent( projectRegistry );
        registerDataFrameRowSelectionEvent( projectRegistry );
    }

    private void registerAnnotationDataFrameCreatedEvent( ProjectRegistry projectRegistry ) {
        DataFrameEventListenerAdapter annotationDfCreatedListener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame frameToAnnotate ) {
                BFAnnotationConsoleViewComposite.this.currentSelectedDataFrameRow = null;
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.AnnotationDataFrameLoaded, annotationDfCreatedListener );
    }

    private void registerDataFrameSelectedEvent( ProjectRegistry projectRegistry ) {
        // current Selected Dataframe, not nice yet, but for proof of concept
        DataFrameEventListenerAdapter listener = new DataFrameEventListenerAdapter() {
            @Override
            public void handleDataFrame( DataFrame dataFrame ) {
                currentSelectedDataFrame = dataFrame;
                currentSelectedDataFrameRow = null;

                // TODO: calculcate row selection if any...
                DataFrameRow calculatedSelectedRow = currentSelectedDataFrameRow;

                prepareCurrentAnnotation( calculatedSelectedRow );

                currentSelectedDataFrameRow = calculatedSelectedRow;
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, listener );
    }

    private void registerDataFrameRowSelectionEvent( ProjectRegistry projectRegistry ) {
        // row selection...
        DataFrameRowSelectedListenerAdapter dataFrameRowSelectionListener = new DataFrameRowSelectedListenerAdapter() {
            @Override
            public void handleDataFrameRowSelected( DataFrameRow selectedRow ) {
                // prevent the modify handler to issue a write command
                currentSelectedDataFrameRow = null;
                prepareCurrentAnnotation( selectedRow );
                currentSelectedDataFrameRow = (DataFrameRow) selectedRow;
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameRowSelectedEvent, dataFrameRowSelectionListener );

    }

    private void createContent() {
        String[] availableReportNames = annotatorService != null ? annotatorService.getPersistenceModule().getAvailableReportNames() : new String[0];
        String[] frameAnnotationSnippets = annotatorService != null ? annotatorService.getPersistenceModule().getFrameAnnotationSnippets() : new String[0];

        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( this, SWT.VERTICAL );

        SashForm sashForm_1 = new SashForm( sashForm, SWT.NONE );

        Composite composite = new Composite( sashForm_1, SWT.NONE );
        composite.setLayout( new GridLayout( 3, false ) );

        btnClearAnnotations = new Button( composite, SWT.NONE );
        btnClearAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                clearAnnotationDataFrame();
            }
        } );
        btnClearAnnotations.setText( "Clear Annotations" );

        Button btnGenerateReport = new Button( composite, SWT.NONE );
        btnGenerateReport.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {

                buildReport( currentSelectedDataFrame );
                // Someone pressed the button.
                // We know the analysis frame and the currentFrame, so we can actually generate a report from it...
                // well building the report should be done elsewhere?
                // This is just a proof of concept for now.
                // and an idea, how this can be achieved....
                // we should be able to go though the annotations, lookup the items at the positions, and then 
                // generate some text from it, basically.
            }
        } );
        btnGenerateReport.setText( "Generate and Copy Report..." );

        ComboViewer comboViewer = new ComboViewer( composite, SWT.READ_ONLY );
        comboViewer.setContentProvider( new ArrayContentProvider() );
        comboViewer.setInput( availableReportNames );
        reportSelectionCombo = comboViewer.getCombo();
        GridData gd_combo = new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 );
        gd_combo.widthHint = 132;
        reportSelectionCombo.setLayoutData( gd_combo );
        reportSelectionCombo.select( 0 );

        Button btnLoadAnnotations = new Button( composite, SWT.NONE );
        btnLoadAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String header = "Load Annotation File.";
                BrightFluxFileDialogs.openRegularFileAndConsumePath( shell, header, FileDescriptions.BF_ANNOTATION, p -> {
                    BFCommand command = AnnotatorCommandFactory.loadAnnotationDataFrame( currentSelectedDataFrame, p );
                    dispatchCommand( command );
                } );

            }
        } );
        btnLoadAnnotations.setText( "Load Annotations ..." );
        new Label( composite, SWT.NONE );
        new Label( composite, SWT.NONE );

        Button btnSaveAnnotations = new Button( composite, SWT.NONE );
        btnSaveAnnotations.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                String header = "Save Annotation File.";
                BrightFluxFileDialogs.saveRegularFileAndConsumePath( shell, header, FileDescriptions.BF_ANNOTATION, p -> {
                    BFCommand command = AnnotatorCommandFactory.saveAnnotationDataFrame( annotatorService.getLogAnalysisFrame( currentSelectedDataFrame ), p );
                    dispatchCommand( command );
                } );

            }
        } );
        btnSaveAnnotations.setText( "Save Annotations ..." );
        new Label( composite, SWT.NONE );
        new Label( composite, SWT.NONE );

        Composite composite_1 = new Composite( sashForm_1, SWT.NONE );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        ListViewer listViewer = new ListViewer( composite_1, SWT.BORDER | SWT.V_SCROLL );
        List list = listViewer.getList();
        list.setFont( SWTResourceManager.getFont( "Courier New", 9, SWT.NORMAL ) );
        list.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseDoubleClick( MouseEvent e ) {
                String[] selection = ((List) e.getSource()).getSelection();
                if (selection != null && selection.length > 0) {
                    appendAnnotatedTextField( selection[0] );
                }
            }
        } );

        listViewer.setContentProvider( ArrayContentProvider.getInstance() );
        list.setItems( frameAnnotationSnippets );
        sashForm_1.setWeights( new int[] { 1, 1 } );

        SashForm sashForm_2 = new SashForm( sashForm, SWT.NONE );

        Composite composite_2 = new Composite( sashForm_2, SWT.NONE );
        composite_2.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Composite composite_3 = new Composite( composite_2, SWT.NONE );
        composite_3.setLayout( new TableColumnLayout() );

        table = new Table( composite_3, SWT.BORDER | SWT.FULL_SELECTION );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );

        Composite composite_4 = new Composite( sashForm_2, SWT.NONE );
        composite_4.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        annotatedStyledText = new StyledText( composite_4, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
        annotatedStyledText.addModifyListener( new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                updateModifiedAnnotationForDataFrame();
            }
        } );
        annotatedStyledText.setLeftMargin( 3 );
        annotatedStyledText.setBottomMargin( 3 );
        annotatedStyledText.setTopMargin( 3 );
        annotatedStyledText.setRightMargin( 3 );
        annotatedStyledText.setFont( SWTResourceManager.getFont( "Arial", 12, SWT.NORMAL ) );

        sashForm_2.setWeights( new int[] { 1, 1 } );
        sashForm.setWeights( new int[] { 1, 1 } );
    }

    /**
     * @param string
     */
    private void appendAnnotatedTextField( String annotation ) {
        String newText = annotatedStyledText.getText() + annotation;
        annotatedStyledText.setText( newText );
    }

    private void updateModifiedAnnotationForDataFrame() {
        if (isCurrentDataFrameRowValid()) {
            String newText = annotatedStyledText.getText();

            /**
            * TODO: We will use the current SelectedDataFrame as a reference for the annotations. It 
            * is the responsibility of the annotator backend to figure out, which dataframe to store 
            * these annotations in and to pick the correct annotation dataframe according the the 
            * current selectedDataFrame.
            */

            BFCommand command = AnnotatorCommandFactory.annotateRow( currentSelectedDataFrame, currentSelectedDataFrameRow.getOriginalRowIndex(), newText );
            dispatchCommand( command );
        }
    }

    private void prepareCurrentAnnotation( Object rowItem ) {
        if (rowItem != null) {
            int originalRowIndex = ((DataFrameRow) rowItem).getOriginalRowIndex();
            DataFrame logAnalysisFrame = annotatorService.getLogAnalysisFrame( currentSelectedDataFrame );

            if (logAnalysisFrame.isPresent( ANNOTATION_COLUMN_NAME, originalRowIndex )) {
                String previousAnnotation = (String) logAnalysisFrame.getAt( ANNOTATION_COLUMN_NAME, originalRowIndex );
                annotatedStyledText.setText( previousAnnotation );
            }
            else {
                annotatedStyledText.setText( "" );
            }
        }
        else {
            annotatedStyledText.setText( "" );
        }
    }

    private boolean isCurrentDataFrameRowValid() {
        return currentSelectedDataFrameRow != null;
    }

    private void buildReport( DataFrame currentSelectedDF ) {
        String report = annotatorService.createFullReport( reportSelectionCombo.getText(), reportSelectionCombo.getSelectionIndex(), currentSelectedDF );

        dispatchCommand( UICommandFactory.copyToClipboard( this.getShell(), report ) );
    }

    private void clearAnnotationDataFrame() {
        // TODO: we actually want to clear the dataframe for the selected Dataframe
        // dispatchCommand( AnnotatorCommandFactory.createSparseDataFrame() );
    }

    private void dispatchCommand( BFCommand command ) {
        if (projectRegistry != null) {
            projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components

    }

}
