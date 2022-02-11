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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyComponent;
import de.mindscan.brightflux.recipe.BFRecipe;
import de.mindscan.brightflux.recipe.BFRecipeIO;
import de.mindscan.brightflux.system.recipes.RecipeUtils;
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
    private DashboardUIProxyComponent dashboardProxyComponent;
    private DataFrameHierarchyComponent dataFrameHierarchyComponent;
    private UUID activeRootIndexUuid;
    private Map<String, DataFrame> activeIndexCacheByName = new HashMap<>();

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
            dashboardProxyComponent = systemServices.getService( DashboardUIProxyComponent.class );
            dashboardProxyComponent.registerCurrentActiveDashboardWindow( this );
        }
        else {
            System.out.println( "[DashboardWindowDialog] DashboardProxyComponent not initailized." );
            //throw new NotYetImplemetedException( "[DashboardWindowDialog] DashboardProxyComponent not initailized." );
        }

        if (systemServices.isServiceAvailable( DataFrameHierarchyComponent.class )) {
            dataFrameHierarchyComponent = systemServices.getService( DataFrameHierarchyComponent.class );
        }
        else {
            System.out.println( "[DashboardWindowDialog] DataFrameHierarchyComponent not initailized." );
            // throw new NotYetImplemetedException( "[DashboardWindowDialog] DataFrameHierarchyComponent not initailized." );
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
    public void dataFrameSelected( DataFrame selectedDataFrame ) {
        if (dataFrameHierarchyComponent == null) {
            return;
        }

        //dataFrameHierarchyComponent.getDataframeHierarchy().getRoot( selectedDataFrame );
        DataFrame rootForSelectedDataFrame = selectedDataFrame;

        // only update the dashboard index, if the root data frame is a different than the current active root index.
        if (!rootForSelectedDataFrame.getUuid().equals( activeRootIndexUuid )) {
            updateDashboardIndex( rootForSelectedDataFrame );
        }
    }

    private void updateDashboardIndex( DataFrame rootDataFrame ) {
        String[] dashboardRecipesNames = dashboardProxyComponent.getPersistenceModule().getDashboardRecipesNames();

        HashMap<String, DataFrameRowQueryCallback> emptyCallbacks = new HashMap<String, DataFrameRowQueryCallback>();

        BFRecipe extractIndexRecipe = BFRecipeIO.loadFromFile( dashboardProxyComponent.getPersistenceModule().getDashboardIndexExtractorRecipePath() );

        Map<String, DataFrame> cache = new HashMap<>();
        for (int i = 0; i < dashboardRecipesNames.length; i++) {
            BFRecipe recipe = BFRecipeIO.loadFromFile( dashboardProxyComponent.getPersistenceModule().getDashboardRecipe( i ) );
            DataFrame dataframe = RecipeUtils.applyRecipeToDataFrame( recipe, rootDataFrame, emptyCallbacks );
            DataFrame indexDataframe = RecipeUtils.applyRecipeToDataFrame( extractIndexRecipe, dataframe, emptyCallbacks );
            cache.put( dashboardRecipesNames[i], indexDataframe );
        }

        activeIndexCacheByName = cache;
        activeRootIndexUuid = rootDataFrame.getUuid();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dataFrameRowSelected( DataFrameRow selectedRow ) {
        Integer org_idx = (Integer) selectedRow.get( "__org_idx" );

        // run a prepared statement according to the selected row and update these values
        String preparedStatement = "SELECT * FROM df WHERE (df.'__org_idx'<= :SelectedOrgIdx )";
        String preparedQuery = preparedStatement.replace( ":SelectedOrdIdx", org_idx.toString() );

        // TODO: next steps 
        // - go through all acttiveIndexCachesByName
        // - foreach cached dataframe, we apply the preparedQuery
        // - after this on the datafreme.getColumn('__org_idx').max(); 
        // - look up the __org_idx and ETL on the real root dataframe.
        // - update the data
        // TODO: notify the UI, that these values changed...
    }

    private void updateDashboadData() {

        /**
         * according to the dashboard configuration, we want to extract multiple latest events from the logs.
         * We do that by either querying the current frame or the root frame, depending on the configuration
         * 
         * root : select * from df where
         * 
         * ---   ((df.'h2.msg'.contains('cpu usage')) && (df.'__org_idx' <= :selectedRow.'__org_idx')) -> max(__org_idx)
         * ---   --> extract --> transform --> visualize : cpu_usage_data
         * ---   ((df.'h2.msg'.contains('ram usage')) && (df.'__org_idx' <= :selectedRow.'__org_idx')) -> max(__org_idx)
         * ---   --> extract --> transform --> visualize : ram_usage_data
         * 
         * 
         * * actually the second selection can be done after the first.
         *    ---   (df.'__org_idx' <= :selectedRow.'__org_idx')
         *    
         * * if i don't keep the data, it will just become a cheap index for every kind of thing we look for.
         *    ---  select id, __org_idx, h1.ts, 
         *    
         * * then we need to figure out, where to get the data from. (maybe this data was extra processed)
         *  
         */
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // intentionally left blank for now
    }
}
