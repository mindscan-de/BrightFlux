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
package de.mindscan.brightflux.viewer.parts.pv;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;

/**
 * 
 */
public class DataFrameHierarchyViewComposite extends Composite implements ProjectRegistryParticipant {

    private ProjectRegistry projectRegistry;

    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public DataFrameHierarchyViewComposite( Composite parent, int style ) {
        super( parent, style );

        buildLayout();

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        this.projectRegistry = projectRegistry;

        // Register to dataframe create / close events 
        // register to dataframe hierarchy events
    }

    private void buildLayout() {
        setLayout( new FillLayout( SWT.HORIZONTAL ) );

        Tree tree = new Tree( this, SWT.BORDER );
        tree.setLinesVisible( true );
        tree.setHeaderVisible( true );

        TreeColumn trclmnNewColumn = new TreeColumn( tree, SWT.NONE );
        trclmnNewColumn.setWidth( 100 );
        trclmnNewColumn.setText( "New Column" );

        TreeColumn trclmnNewColumn_1 = new TreeColumn( tree, SWT.NONE );
        trclmnNewColumn_1.setWidth( 100 );
        trclmnNewColumn_1.setText( "New Column" );

        TreeColumn trclmnNewColumn_2 = new TreeColumn( tree, SWT.NONE );
        trclmnNewColumn_2.setWidth( 100 );
        trclmnNewColumn_2.setText( "New Column" );
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
