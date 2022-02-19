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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.dataframes.DataFrameColumn;
import de.mindscan.brightflux.dataframes.DataFrameRow;
import de.mindscan.brightflux.dataframes.DataFrameRowQueryCallback;
import de.mindscan.brightflux.dataframes.columns.NumberAggregateFunctions;
import de.mindscan.brightflux.dataframes.dfquery.DataFrameQueryLanguageEngine;
import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.framework.registry.ProjectRegistryParticipant;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyComponent;
import de.mindscan.brightflux.system.recipes.BFRecipe;
import de.mindscan.brightflux.system.recipes.BFRecipeIO;
import de.mindscan.brightflux.system.recipes.RecipeUtils;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardUIProxyComponent;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.DashboardWindow;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.transform.ETVColumnTransformer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.visualizer.SimpleContentWidgetVisualizer;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets.CpuUsageDashboardWidget;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets.RamUsageDashboardWidget;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets.SelectedTimestampDashboardWidget;

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
    private DataFrame activeRootDataframe;
    private CpuUsageDashboardWidget cpuUsageWidget;
    private RamUsageDashboardWidget ramUsageWidget;
    private RamUsageDashboardWidget statsWidget;
    private DashboardWindowConfigurationComposite compositeTopRight;
    private Map<String, ETVColumnTransformer[]> registeredTransformations;

    private TimeStampsConfigurationEnum currentTimestampEnum = TimeStampsConfigurationEnum.IDENTITY;
    private SelectedTimestampDashboardWidget selectedTimeWidget;

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
        initializeTransformations();

        shellDashboadWindow = new Shell( getParent(), SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE );
        shellDashboadWindow.setSize( 886, 474 );
        shellDashboadWindow.setText( "Dashboard" );
        shellDashboadWindow.setLayout( new GridLayout( 2, false ) );

        selectedTimeWidget = new SelectedTimestampDashboardWidget( shellDashboadWindow, SWT.NONE );
        selectedTimeWidget.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 ) );

        compositeTopRight = new DashboardWindowConfigurationComposite( shellDashboadWindow, SWT.NONE );
        compositeTopRight.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false, 1, 1 ) );
        compositeTopRight.setTimestampEnumCallback( this::setCurrentTimestampEnum );

        cpuUsageWidget = new CpuUsageDashboardWidget( shellDashboadWindow, SWT.NONE );
        cpuUsageWidget.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 ) );
        cpuUsageWidget.setSize( 436, 47 );
        new Label( shellDashboadWindow, SWT.NONE );

        ramUsageWidget = new RamUsageDashboardWidget( shellDashboadWindow, SWT.NONE );
        GridData gd_ramUsageWidget = new GridData( SWT.FILL, SWT.TOP, false, false, 1, 1 );
        gd_ramUsageWidget.heightHint = 225;
        ramUsageWidget.setLayoutData( gd_ramUsageWidget );

        statsWidget = new RamUsageDashboardWidget( shellDashboadWindow, SWT.NONE );
        GridData gd_statsWidget = new GridData( SWT.LEFT, SWT.TOP, false, false, 1, 1 );
        gd_statsWidget.widthHint = 414;
        gd_statsWidget.heightHint = 225;
        statsWidget.setLayoutData( gd_statsWidget );

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
     * 
     * TODO: some of the calculation should not be done in the UI, but rather in a different component.
     * 
     * Especially the next code should be refactored, to have a clear separation of model/data and view.
     */

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dataFrameSelected( DataFrame selectedDataFrame ) {
        if (dataFrameHierarchyComponent == null) {
            return;
        }

        DataFrame rootForSelectedDataFrame = dataFrameHierarchyComponent.getRootDataFrame( selectedDataFrame );

        // only update the dashboard index, if the root data frame is a different than the current active root index.
        if (!rootForSelectedDataFrame.getUuid().equals( activeRootIndexUuid )) {
            updateDashboardIndex( rootForSelectedDataFrame );
        }
    }

    private void updateDashboardIndex( DataFrame rootDataFrame ) {
        String[] dashboardRecipesNames = dashboardProxyComponent.getPersistenceModule().getDashboardRecipesNames();

        HashMap<String, DataFrameRowQueryCallback> emptyCallbacks = new HashMap<String, DataFrameRowQueryCallback>();

        BFRecipe extractIndexRecipe = BFRecipeIO.loadFromFile( dashboardProxyComponent.getPersistenceModule().getDashboardIndexExtractorRecipePath() );

        Map<String, DataFrame> cache = new LinkedHashMap<>();
        for (int i = 0; i < dashboardRecipesNames.length; i++) {
            BFRecipe recipe = BFRecipeIO.loadFromFile( dashboardProxyComponent.getPersistenceModule().getDashboardRecipe( i ) );
            DataFrame dataframe = RecipeUtils.applyRecipeToDataFrame( recipe, rootDataFrame, emptyCallbacks );
            DataFrame indexDataframe = RecipeUtils.applyRecipeToDataFrame( extractIndexRecipe, dataframe, emptyCallbacks );
            cache.put( dashboardRecipesNames[i], indexDataframe );
        }

        activeIndexCacheByName = cache;
        activeRootIndexUuid = rootDataFrame.getUuid();
        activeRootDataframe = rootDataFrame;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void dataFrameRowSelected( DataFrameRow selectedRow ) {
        Integer org_idx = (Integer) selectedRow.get( "__org_idx" );

        DataFrameQueryLanguageEngine engine = new DataFrameQueryLanguageEngine();

        // run a prepared statement according to the selected row and update these values
        // i think the query should be compiled into a rowfilterpredicate, which can be reused.
        String preparedStatement = "SELECT * FROM df WHERE (df.'__org_idx'<= :SelectedOrgIdx )";
        String preparedQuery = preparedStatement.replace( ":SelectedOrgIdx", org_idx.toString() );

        System.out.println( "preparedQuery: " + preparedQuery );

        // - go through all acttiveIndexCachesByName
        for (Entry<String, DataFrame> entry : activeIndexCacheByName.entrySet()) {
            String name = entry.getKey();
            DataFrame cachedDataFrameForName = entry.getValue();

            // - foreach cached dataframe, we apply the preparedQuery
            DataFrame prefiltered = engine.executeDFQuery( cachedDataFrameForName, preparedQuery );

            // - after this on the datafreme.getColumn('__org_idx').max(); 
            DataFrameColumn<?> column = prefiltered.getColumn( "__org_idx" );
            if (column instanceof NumberAggregateFunctions) {
                Number computedOrgIndexInRootFrame = ((NumberAggregateFunctions<?>) column).max();
                updateDashboardData( name, computedOrgIndexInRootFrame.intValue(), selectedRow );
            }
        }
    }

    /**
     */
    private void updateDashboardData( String name, int computedOrgIndex, DataFrameRow selectedRow ) {
        if (computedOrgIndex < 0) {
            clearDashboardWidgetData( name );
            return;
        }

        if (activeRootDataframe == null) {
            return;
        }

        // - look up the __org_idx and ETL on the root dataframe of the current root dataframe
        // - according to the configurated ETL for this name we want to extract data from the row.
        // depending on the name we want to extract, transform and visualize ....
        // - update the data
        // - notify the UI, that these values changed...
        extractTransformVisualize( name, activeRootDataframe.getRow( computedOrgIndex ), selectedRow );
    }

    private void clearDashboardWidgetData( String name ) {
        Object widgetByName = getWidgetByConfigName( name );

        if (widgetByName instanceof SimpleContentWidgetVisualizer) {
            ((SimpleContentWidgetVisualizer) widgetByName).setNA();
        }
        else {
            System.out.println( "name: " + name + ", has no clear operation" );
        }

    }

    private void extractTransformVisualize( String name, DataFrameRow computedRow, DataFrameRow selectedRow ) {
        System.out.println( "[" + name + "]: " + computedRow.get( "h2.msg" ) );

        // apply configured generic transformations
        if (registeredTransformations.containsKey( name )) {
            // Okay we have some ETV transformations
            ETVColumnTransformer[] etvColumnTransformers = registeredTransformations.get( name );
            for (ETVColumnTransformer etvColumnTransformer : etvColumnTransformers) {
                // run every known extract, transform, visualizer 
                etvColumnTransformer.execute( computedRow, selectedRow, this::getWidgetByInstanceName );
            }
        }

        // TODO: this should only be done once at instantiating the widgets.
        Object widgetByConfigName = getWidgetByConfigName( name );
        if (widgetByConfigName instanceof SimpleContentWidgetVisualizer) {
            ((SimpleContentWidgetVisualizer) widgetByConfigName).setName( name );
        }

        // apply missing transformers, which are too complex right now
        switch (name) {
            case "RamUsage": {
                // extract from row:
                String message = String.valueOf( computedRow.get( "h2.msg" ) );

                // extract from message
                message = message.substring( message.indexOf( "usage" ) );
                String[] split = message.split( "," );

                // visualization
                for (String keyValuePair : split) {
                    ramUsageWidget.getKeyValueVisualizer().setPair( keyValuePair.split( "=", 2 ) );
                }

                break;
            }

            case "HXX Stats": {
                // extract from row:
                String message = String.valueOf( computedRow.get( "h2.msg" ) );

                // extract from message
                message = message.substring( message.indexOf( "software" ) );
                String[] split = message.split( ";;" );

                // visualization
                for (String keyValuePair : split) {
                    statsWidget.getKeyValueVisualizer().setPair( keyValuePair.split( ":=", 2 ) );
                }

                break;
            }

            default:
                break;
        }
    }

    // TODO: read/build these Transformer configurations from a special configuration file 
    private void initializeTransformations() {
        registeredTransformations = new HashMap<>();

        // actually the "unique" widgetname for he instance is local to the widget itself.
        // so if we connect the widget with the column transformers, we actually mean "this" instead of "cpuUsageWidget"
        ETVColumnTransformer[] cpuUsageTransformers = new ETVColumnTransformer[] { //
                        new ETVColumnTransformer( "h2.msg", "stringVisualizer", "cpuUsageWidget", message -> {
                            // TODO: implement these kind of transformations as some kind of configuration.
                            message = message.substring( message.indexOf( "CPU" ) );
                            message = message.substring( message.indexOf( "=" ) );
                            String usageValue = message.substring( 1, message.indexOf( "," ) ).trim();

                            return usageValue;
                        } ), // 
                        new ETVColumnTransformer( "h1.ts", "timestampVisualizer", "cpuUsageWidget", this::timestampTransformer ) };

        // 
        ETVColumnTransformer[] ramUsageTransformers = new ETVColumnTransformer[] { //
                        new ETVColumnTransformer( "h1.ts", "timestampVisualizer", "ramUsageWidget", this::timestampTransformer ) };

        ETVColumnTransformer[] statsTransformers = new ETVColumnTransformer[] { //
                        new ETVColumnTransformer( "h1.ts", "timestampVisualizer", "statsWidget", this::timestampTransformer ) };

        ETVColumnTransformer[] currentTimeTransformers = new ETVColumnTransformer[] { //
                        new ETVColumnTransformer( "h1.ts", "timestampVisualizer", "timeWidget", this::sameString, true ) };

        // register these transformations in a map
        registeredTransformations.put( "CpuUsage", cpuUsageTransformers );
        registeredTransformations.put( "RamUsage", ramUsageTransformers );
        registeredTransformations.put( "HXX Stats", statsTransformers );
        registeredTransformations.put( "Selected Timestamp", currentTimeTransformers );
    }

    // provides binding between widgetName and widgetInstance
    public Object getWidgetByInstanceName( String name ) {
        switch (name) {
            case "statsWidget":
                return statsWidget;
            case "ramUsageWidget":
                return ramUsageWidget;
            case "cpuUsageWidget":
                return cpuUsageWidget;
            case "timeWidget":
                return selectedTimeWidget;
            default:
                throw new NotYetImplemetedException( "Widget is not supported." );
        }
    }

    public Object getWidgetByConfigName( String name ) {
        switch (name) {
            case "HXX Stats":
                return statsWidget;
            case "RamUsage":
                return ramUsageWidget;
            case "CpuUsage":
                return cpuUsageWidget;
            case "Selected Timestamp":
                return selectedTimeWidget;
            default:
                return null;
        }
    }

    public String sameString( String string ) {
        return string;
    }

    // this is actually a conversion strategy... for the time, this may be extended and configured.
    public String timestampTransformer( String timestamp ) {
        return currentTimestampEnum.convertTimestamp( timestamp );
    }

    private void setCurrentTimestampEnum( TimeStampsConfigurationEnum currentTimestampEnum ) {
        this.currentTimestampEnum = currentTimestampEnum;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        // intentionally left blank for now
    }
}
