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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardUIProxyComponent;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardWindow;
import swing2swt.layout.BorderLayout;

/**
 * 
 */
public class DashboardWindowDialog extends Dialog implements DashboardWindow, ProjectRegistryParticipant {

    protected Object result;
    protected Shell shellDashboadWindow;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public DashboardWindowDialog( Shell parent, int style ) {
        super( parent, style );
        setText( "SWT Dialog" );
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public Object open() {
        createContents();
        shellDashboadWindow.open();
        shellDashboadWindow.layout();
        Display display = getParent().getDisplay();
        while (!shellDashboadWindow.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        registerAtDashboardUIProxyComponent();

        shellDashboadWindow = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE );
        shellDashboadWindow.setSize( 450, 300 );
        shellDashboadWindow.setText( "Dashboard" );
        shellDashboadWindow.setLayout( new BorderLayout( 0, 0 ) );

        Composite upperComposite = new Composite( shellDashboadWindow, SWT.NONE );
        upperComposite.setLayoutData( BorderLayout.NORTH );
        upperComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Group grpTest = new Group( upperComposite, SWT.NONE );
        grpTest.setText( "Test" );

        Composite lowerComposite = new Composite( shellDashboadWindow, SWT.NONE );
        lowerComposite.setLayoutData( BorderLayout.SOUTH );
        lowerComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Group grpTest_1 = new Group( lowerComposite, SWT.NONE );
        grpTest_1.setText( "Test2" );

        Composite middleComposite = new Composite( shellDashboadWindow, SWT.NONE );
        middleComposite.setLayoutData( BorderLayout.CENTER );

        shellDashboadWindow.addListener( SWT.Traverse, new Listener() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( Event event ) {
                // don't close on escape key.
                if (event.character == SWT.ESC) {
                    event.doit = false;
                }
            }
        } );

        shellDashboadWindow.addListener( SWT.Close, new Listener() {
            /** 
             * {@inheritDoc}
             */
            @Override
            public void handleEvent( Event event ) {
                unregisterAtdashboardUIProxyComponent();
            }

        } );

    }

    private void registerAtDashboardUIProxyComponent() {
        SystemServices systemServices = SystemServices.getInstance();
        if (systemServices == null) {
            return;
        }

        if (systemServices.isServiceAvailable( DashboardUIProxyComponent.class )) {
            DashboardUIProxyComponent service = systemServices.getService( DashboardUIProxyComponent.class );
            service.registerCurrentActiveDashboardWindow( this );
        }
        else {

        }
    }

    private void unregisterAtdashboardUIProxyComponent() {
        SystemServices systemServices = SystemServices.getInstance();
        if (systemServices == null) {
            return;
        }

        if (systemServices.isServiceAvailable( DashboardUIProxyComponent.class )) {
            DashboardUIProxyComponent service = systemServices.getService( DashboardUIProxyComponent.class );
            service.unregisterCurrentActiveDashboardWindow();
        }
        else {

        }
    }

    @Override
    public void bringToTop() {
        shellDashboadWindow.setFocus();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dataFrameRowSelected( DataFrameRow selectedRow ) {
        // TODO Auto-generated method stub

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // TODO Auto-generated method stub

    }
}
