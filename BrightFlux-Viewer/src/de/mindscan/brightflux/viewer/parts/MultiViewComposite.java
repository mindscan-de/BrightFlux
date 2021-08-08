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
package de.mindscan.brightflux.viewer.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import de.mindscan.brightflux.system.registry.ProjectRegistry;
import de.mindscan.brightflux.system.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.mv.BFAppConsoleViewComposite;
import de.mindscan.brightflux.viewer.parts.mv.BFAppLogViewComposite;

/**
 * 
 */
public class MultiViewComposite extends Composite implements ProjectRegistryParticipant {
    private ProjectRegistry projectRegistry;
    private BFAppLogViewComposite appLog;
    private BFAppConsoleViewComposite appConsole;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public MultiViewComposite( Composite parent, int style ) {
        super( parent, style );

        buildLayout();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        // TODO: register for events.... 

        if (appConsole instanceof ProjectRegistryParticipant) {
            ((ProjectRegistryParticipant) appConsole).setProjectRegistry( projectRegistry );
        }

        if (appLog instanceof ProjectRegistryParticipant) {
            ((ProjectRegistryParticipant) appLog).setProjectRegistry( projectRegistry );
        }
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        CTabFolder tabFolder = new CTabFolder( this, SWT.BORDER );

        CTabItem tbtmConsole = new CTabItem( tabFolder, SWT.NONE );
        tbtmConsole.setText( "Console" );
        appConsole = new BFAppConsoleViewComposite( tbtmConsole.getParent(), SWT.NONE );
        tbtmConsole.setControl( appConsole );

        CTabItem tbtmApplicationLog = new CTabItem( tabFolder, SWT.NONE );
        tbtmApplicationLog.setText( "Application Log" );
        appLog = new BFAppLogViewComposite( tabFolder, SWT.NONE );
        tbtmApplicationLog.setControl( appLog );

        tabFolder.setSelection( tbtmApplicationLog );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
