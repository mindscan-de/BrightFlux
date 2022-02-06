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
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardWindow;

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
        shellDashboadWindow = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE );
        shellDashboadWindow.setSize( 450, 300 );
        shellDashboadWindow.setText( "Dashboard" );

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // TODO Auto-generated method stub

    }

}
