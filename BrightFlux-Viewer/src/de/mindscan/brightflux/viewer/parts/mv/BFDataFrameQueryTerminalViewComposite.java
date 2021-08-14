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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.framework.command.BFCommand;
import de.mindscan.brightflux.framework.events.BFEvent;
import de.mindscan.brightflux.system.commands.DataFrameCommandFactory;
import de.mindscan.brightflux.system.events.BFDataFrameEvent;
import de.mindscan.brightflux.system.events.BFEventListenerAdapter;
import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.UIEvents;

/**
 * 
 */
public class BFDataFrameQueryTerminalViewComposite extends Composite implements ProjectRegistryParticipant {

    private DataFrame currentSelectedDataFrame = null;
    private ProjectRegistry projectRegistry;
    private StyledText queryHistoryTextArea;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public BFDataFrameQueryTerminalViewComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        SashForm sashForm = new SashForm( this, SWT.VERTICAL );

        Composite upperComposite = new Composite( sashForm, SWT.NONE );
        upperComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        CCombo combo = new CCombo( upperComposite, SWT.BORDER );
        combo.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased( KeyEvent e ) {
                if (e.character == SWT.CR) {
                    String theQuery = combo.getText();

                    addToHistory( theQuery );
                    combo.setText( "" );

                    executeQuery( theQuery );
                }
            }
        } );
        combo.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );

        Composite lowerComposite = new Composite( sashForm, SWT.NONE );
        lowerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        queryHistoryTextArea = new StyledText( lowerComposite, SWT.BORDER );
        queryHistoryTextArea.setFont( SWTResourceManager.getFont( "Courier New", 11, SWT.NORMAL ) );
        sashForm.setWeights( new int[] { 60, 237 } );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        BFEventListenerAdapter listener = new BFEventListenerAdapter() {
            @Override
            public void handleEvent( BFEvent event ) {
                BFDataFrameEvent dataFrameEvent = ((BFDataFrameEvent) event);
                currentSelectedDataFrame = dataFrameEvent.getDataFrame();

                // Update View? Show name of selected DataFrame, 
                // because the Terminal will be sensitive to the selected frame in the MainProjectComposite
            }
        };

        projectRegistry.getEventDispatcher().registerEventListener( UIEvents.DataFrameSelectedEvent, listener );
    }

    public void executeQuery( String theQuery ) {
        if (currentSelectedDataFrame != null) {
            BFCommand command = DataFrameCommandFactory.queryDataFrame( currentSelectedDataFrame, theQuery );
            this.projectRegistry.getCommandDispatcher().dispatchCommand( command );
        }
    }

    public void addToHistory( String theQuery ) {
        // Attention: Instead of using the text area as the main storage, we might be using an LRU Cache and rebuild the text on each Change or duplicate
        // Also we might want to persist this History for the next start.
        String newCurrentText = theQuery + System.lineSeparator() + queryHistoryTextArea.getText();
        queryHistoryTextArea.setText( newCurrentText );
    }

}
