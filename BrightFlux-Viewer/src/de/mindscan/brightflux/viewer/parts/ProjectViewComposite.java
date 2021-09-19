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
import org.eclipse.swt.widgets.Display;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.viewer.parts.pv.DataFrameHierarchyViewComposite;
import de.mindscan.brightflux.viewer.parts.pv.LogAnalysisProjectViewComposite;

/**
 * 
 */
public class ProjectViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;
    private LogAnalysisProjectViewComposite logAnalysisProjectProjectView;
    private DataFrameHierarchyViewComposite dataframeHierarchyProjectView;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public ProjectViewComposite( Composite parent, int style ) {
        super( parent, style );

        buildLayout();

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        // register and initialize sub components
        if (logAnalysisProjectProjectView instanceof ProjectRegistryParticipant) {
            ((ProjectRegistryParticipant) logAnalysisProjectProjectView).setProjectRegistry( this.projectRegistry );
        }
        if (dataframeHierarchyProjectView instanceof ProjectRegistryParticipant) {
            ((ProjectRegistryParticipant) dataframeHierarchyProjectView).setProjectRegistry( this.projectRegistry );
        }
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        CTabFolder tabFolder = new CTabFolder( this, SWT.BORDER );
        tabFolder.setUnselectedCloseVisible( false );
        tabFolder.setSimple( false );
        tabFolder.setSelectionBackground( Display.getCurrent().getSystemColor( SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT ) );

        CTabItem tbtmProject = new CTabItem( tabFolder, SWT.NONE );
        tbtmProject.setText( "Project" );
        logAnalysisProjectProjectView = new LogAnalysisProjectViewComposite( tabFolder, SWT.NONE );
        tbtmProject.setControl( logAnalysisProjectProjectView );

        CTabItem tbtmDataframeHierarchy = new CTabItem( tabFolder, SWT.NONE );
        tbtmDataframeHierarchy.setText( "Frame Hierarchy" );
        dataframeHierarchyProjectView = new DataFrameHierarchyViewComposite( tabFolder, SWT.NONE );
        tbtmDataframeHierarchy.setControl( dataframeHierarchyProjectView );

        tabFolder.setSelection( tbtmDataframeHierarchy );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
