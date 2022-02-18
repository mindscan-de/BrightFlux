/**
 * 
 * MIT License
 *
 * Copyright (c) 2022 Maxim Gansert, Mindscan
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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.ui;

import java.util.function.Consumer;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * 
 */
public class DashboardWindowConfigurationComposite extends Composite {

    private Consumer<TimeStampsConfigurationEnum> setter;
    private Combo combo;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DashboardWindowConfigurationComposite( Composite parent, int style ) {
        super( parent, style );
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Group grpDashboardConfiguration = new Group( this, SWT.NONE );
        grpDashboardConfiguration.setText( "Dashboard Configuration" );
        grpDashboardConfiguration.setLayout( new GridLayout( 2, false ) );

        ComboViewer comboViewer = new ComboViewer( grpDashboardConfiguration, SWT.NONE );
        combo = comboViewer.getCombo();
        combo.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                if (setter != null) {
                    TimeStampsConfigurationEnum[] values = TimeStampsConfigurationEnum.values();
                    TimeStampsConfigurationEnum foo = values[combo.getSelectionIndex()];
                    setter.accept( foo );
                }
            }
        } );
        combo.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
        comboViewer.setContentProvider( new ArrayContentProvider() );
        comboViewer.setInput( TimeStampsConfigurationEnum.values() );
        combo.select( 0 );

        Combo combo_1 = new Combo( grpDashboardConfiguration, SWT.NONE );
        combo_1.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
        new Label( grpDashboardConfiguration, SWT.NONE );

        Button btnFoooo = new Button( grpDashboardConfiguration, SWT.NONE );
        btnFoooo.setText( "Foooo" );

    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    /**
     * @param object
     */
    public void setTimestampEnumCallback( Consumer<TimeStampsConfigurationEnum> setter ) {
        this.setter = setter;
    }
}
