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
    private Table table;

    private DataFrame logAnalysisFrame = null;

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
                annotatedStyledText.setText( String.format( " %d : %s", rowIndex, rowItem ) );

                // ok someone selected a row - we should do something....
                // we might want too update the content of a textfield
                // we might want to save a previous content of a textfield if changed.
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

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components

    }

}
