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

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.viewer.commands.IngestCommand;
import de.mindscan.brightflux.viewer.parts.MainProjectComposite;
import de.mindscan.brightflux.viewer.project.ProjectRegistry;
import de.mindscan.brightflux.viewer.project.ProjectRegistryParticipant;

/**
 * 
 */
public class BrightFluxViewerMainApp {

    protected Shell shlBFViewerMainApp;
    private Composite mainProjectComposite;
    private ProjectRegistry projectRegistry = new ProjectRegistry();

    /**
     * Launch the application.
     * @param args
     */
    public static void main( String[] args ) {
        try {
            // TODO: load some configuration from file
            // TODO: set the current configuration (e.g. the window size) 
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
        shlBFViewerMainApp.open();
        shlBFViewerMainApp.layout();
        while (!shlBFViewerMainApp.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shlBFViewerMainApp = new Shell();
        shlBFViewerMainApp.setSize( 450, 300 );
        shlBFViewerMainApp.setText( "BrightFlux-Viewer 0.0.0.M1" );
        shlBFViewerMainApp.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Menu menu = new Menu( shlBFViewerMainApp, SWT.BAR );
        shlBFViewerMainApp.setMenuBar( menu );

        MenuItem mntmFile = new MenuItem( menu, SWT.CASCADE );
        mntmFile.setText( "File" );

        Menu menu_1 = new Menu( mntmFile );
        mntmFile.setMenu( menu_1 );

        MenuItem mntmLoadFile = new MenuItem( menu_1, SWT.NONE );
        mntmLoadFile.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                // TODO: remove this hard coded value... and get it from the history or settings 
                Path dirPath = Paths
                                .get( "D:\\Projects\\SinglePageApplication\\Angular\\BrightFlux\\BrightFlux-Core\\test\\de\\mindscan\\brightflux\\ingest\\" );

                FileDialog fileDlg = new FileDialog( shlBFViewerMainApp );
                fileDlg.setText( "Select file" );
                fileDlg.setFilterExtensions( new String[] { "*.csv" } );
                fileDlg.setFilterNames( new String[] { "Comma Separated files (*.csv)" } );
                fileDlg.setFilterPath( dirPath.toString() );
                String fileToOPen = fileDlg.open();

                Path path = Paths.get( fileToOPen );
                if (Files.isRegularFile( path, LinkOption.NOFOLLOW_LINKS )) {
                    // TODO: give that to a factory later on.
                    IngestCommand ingestCommand = new IngestCommand( path );
                    projectRegistry.getCommandDispatcher().dispatchCommand( ingestCommand );
                }
            }
        } );
        mntmLoadFile.setText( "Load File" );

        new MenuItem( menu_1, SWT.SEPARATOR );

        MenuItem mntmExit = new MenuItem( menu_1, SWT.PUSH );
        mntmExit.addSelectionListener( new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent e ) {
                shlBFViewerMainApp.close();
            }
        } );
        mntmExit.setText( "Exit" );

        mainProjectComposite = new MainProjectComposite( shlBFViewerMainApp, SWT.NONE );
        mainProjectComposite.setLayout( new FillLayout( SWT.HORIZONTAL ) );

        // This is still not nice, but good enough for now
        // we might implement a patched classloader or some DependenyInjector mechanism, since the app is 
        // basically also a ProjectRegistryParticipant and should not provide the truth to everyone else top down.
        if (mainProjectComposite instanceof ProjectRegistryParticipant) {
            ((ProjectRegistryParticipant) mainProjectComposite).setProjectRegistry( projectRegistry );
        }
    }

}
