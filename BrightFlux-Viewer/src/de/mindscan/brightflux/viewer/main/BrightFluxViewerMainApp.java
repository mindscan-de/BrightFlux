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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */
public class BrightFluxViewerMainApp {

    protected Shell shlBrightfluxviewerm;

    /**
     * Launch the application.
     * @param args
     */
    public static void main( String[] args ) {
        try {
            BrightFluxViewerMainApp window = new BrightFluxViewerMainApp();
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
        shlBrightfluxviewerm.open();
        shlBrightfluxviewerm.layout();
        while (!shlBrightfluxviewerm.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlBrightfluxviewerm = new Shell();
        shlBrightfluxviewerm.setSize( 450, 300 );
        shlBrightfluxviewerm.setText( "BrightFlux-Viewer 0.0.0.M1" );

        Menu menu = new Menu( shlBrightfluxviewerm, SWT.BAR );
        shlBrightfluxviewerm.setMenuBar( menu );

        MenuItem mntmFile = new MenuItem( menu, SWT.CASCADE );
        mntmFile.setText( "File" );

        Menu menu_1 = new Menu( mntmFile );
        mntmFile.setMenu( menu_1 );

        MenuItem mntmLoadFile = new MenuItem( menu_1, SWT.NONE );
        mntmLoadFile.setText( "Load File" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmExit = new MenuItem( menu_1, SWT.PUSH );
        mntmExit.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                shlBrightfluxviewerm.close();
            }
        } );
        mntmExit.setText( "Exit" );

    }

}
