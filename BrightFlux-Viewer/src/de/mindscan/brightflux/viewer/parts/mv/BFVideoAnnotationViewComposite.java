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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class BFVideoAnnotationViewComposite extends Composite implements ProjectRegistryParticipant {

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
        composite.setLayout( new GridLayout( 2, false ) );

        Button addVideoButton = new Button( composite, SWT.NONE );
        addVideoButton.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
            }
        } );
        addVideoButton.setText( "Add Video ..." );
        addVideoButton.setBounds( 0, 0, 85, 23 );

        Button btnLoadVideoAnnotations = new Button( composite, SWT.NONE );
        btnLoadVideoAnnotations.setText( "Load Video Annotations ..." );
        btnLoadVideoAnnotations.setBounds( 0, 0, 120, 23 );
        new Label( composite, SWT.NONE );

        Button btnSaveVideoAnnotations = new Button( composite, SWT.NONE );
        btnSaveVideoAnnotations.setText( "Save Video Annotations ..." );
        btnSaveVideoAnnotations.setBounds( 0, 0, 121, 23 );

        Composite composite_1 = new Composite( this, SWT.NONE );
        composite_1.setLayoutData( BorderLayout.CENTER );
        composite_1.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        CTabFolder tabFolder = new CTabFolder( composite_1, SWT.BORDER );
        tabFolder.setTabPosition( SWT.BOTTOM );
        tabFolder.setSelectionBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT ) );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
