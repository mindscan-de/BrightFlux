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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.framework.events.BFEventListener;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.events.dataframe.BFAbstractDataFrameEvent;
import de.mindscan.brightflux.viewer.parts.SystemEvents;
import de.mindscan.brightflux.viewer.parts.UIEvents;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedEvent;

/**
 * 
 */
public class BFAnnotationConsoleViewComposite extends Composite implements ProjectRegistryParticipant {

    private static final String ANNOTATION_COLUMN_NAME = "annotation";

    private Table table;

    private DataFrame logAnalysisFrame = null;
    private int previouslySelectedIndex = -1;
    private Object previouslySelectedItem = null;

    private StyledText annotatedStyledText;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFAnnotationConsoleViewComposite( Composite parent, int style ) {
        super( parent, style );
        createContent();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        BFEventListener annotationDfCreatedListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                if (event instanceof BFAbstractDataFrameEvent) {
                    DataFrame frameToAnnotate = ((BFAbstractDataFrameEvent) event).getDataFrame();
                    if ("logAnalysisFrame".equals( frameToAnnotate.getName() )) {
                        // copy reference for the loganalysis frame
                        BFAnnotationConsoleViewComposite.this.logAnalysisFrame = frameToAnnotate;
                    }
                }
            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( SystemEvents.AnnotationDataFrameCreated, annotationDfCreatedListener );

        BFEventListener dataFrameRowSelectionListener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                DataFrameRowSelectedEvent x = (DataFrameRowSelectedEvent) event;

                int rowIndex = x.getSelectedIndex();
                Object rowItem = x.getSelectedItem();

                if ((rowIndex != previouslySelectedIndex) || (rowItem != previouslySelectedItem)) {
                    // we want to update the content of the annotations by reading the text field
                    savePreviousState( previouslySelectedIndex, previouslySelectedItem );

                    // we update the text field with the previously written annotation. 
                    prepareNewState( rowIndex, rowItem );

                    previouslySelectedIndex = rowIndex;
                    previouslySelectedItem = rowItem;
                }

            }
        };
        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameRowSelectedEvent, dataFrameRowSelectionListener );

    }

    private void createContent() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( this, SWT.VERTICAL );

        SashForm sashForm_1 = new SashForm( sashForm, SWT.NONE );

        Composite composite = new Composite( sashForm_1, SWT.NONE );

        Composite composite_1 = new Composite( sashForm_1, SWT.NONE );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        annotatedStyledText = new StyledText( composite_1, SWT.BORDER );
        sashForm_1.setWeights( new int[] { 1, 1 } );

        Composite composite_2 = new Composite( sashForm, SWT.NONE );
        composite_2.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Composite composite_3 = new Composite( composite_2, SWT.NONE );
        composite_3.setLayout( new TableColumnLayout() );

        table = new Table( composite_3, SWT.BORDER | SWT.FULL_SELECTION );
        table.setHeaderVisible( true );
        table.setLinesVisible( true );
        sashForm.setWeights( new int[] { 1, 1 } );
    }

    protected void savePreviousState( int previousRow, Object previousItem ) {
        // TODO: This is rather lowlevel code which should be handled via command and event stuff.
        // TODO: This is just proof of concept code right now
        // TODO: use some of the information of the previous Item
        if (isDataFrameValid()) {
            if (previousRow != -1) {

                String newText = annotatedStyledText.getText();

                // TODO: if the string is empty, the rawValue should be set to "N/A" (not available instead)
                if (newText.isBlank()) {
                    // null is internal representation of N/A
                    // TODO we must introduce setting a NA value instead.
                    logAnalysisFrame.setRawValue( ANNOTATION_COLUMN_NAME, previousRow, null );
                }
                else {
                    logAnalysisFrame.setRawValue( ANNOTATION_COLUMN_NAME, previousRow, newText );
                }
                System.out.println( "annotation saved." );
            }
        }
    }

    /**
     * @param rowItem 
     * 
     */
    protected void prepareNewState( int newRowIndex, Object rowItem ) {
        if (isDataFrameValid()) {
            if (logAnalysisFrame.isPresent( ANNOTATION_COLUMN_NAME, newRowIndex )) {
                String previousAnnotation = (String) logAnalysisFrame.getRawValue( ANNOTATION_COLUMN_NAME, newRowIndex );
                annotatedStyledText.setText( previousAnnotation );
            }
            else {
                annotatedStyledText.setText( "" );
            }
        }
    }

    private boolean isDataFrameValid() {
        return logAnalysisFrame != null;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components

    }

}
