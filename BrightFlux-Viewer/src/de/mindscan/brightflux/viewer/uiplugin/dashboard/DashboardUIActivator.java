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
package de.mindscan.brightflux.viewer.uiplugin.dashboard;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;
import de.mindscan.brightflux.system.services.StartupParticipant;
import de.mindscan.brightflux.system.services.SystemServices;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.persistence.DashboardUIPersistenceModule;
import de.mindscan.brightflux.viewer.uiplugin.dashboard.persistence.DashboardUIPersistenceModuleImpl;

/**
 * 
 */
public class DashboardUIActivator implements StartupParticipant {

    private static final String DASHBOARD_UI_PLUGIN = "dashboard-ui-plugin";

    /** 
     * {@inheritDoc}
     */
    @Override
    public void start( SystemServices systemservices ) {
        BasePersistenceModule dashboardUIBasePersistenceModule = systemservices.getBasePersistenceModule( DASHBOARD_UI_PLUGIN );
        DashboardUIPersistenceModule persistenceModule = new DashboardUIPersistenceModuleImpl( dashboardUIBasePersistenceModule );

        DashboardUIProxyComponent dashboardUIComponent = new DashboardUIProxyComponent();
        dashboardUIComponent.setPersistenceModule( persistenceModule );

        systemservices.registerService( dashboardUIComponent, DashboardUIProxyComponent.class );

        ProjectRegistry projectRegistry = systemservices.getProjectRegistry();
        if (projectRegistry != null) {
            // problem...
            // projectRegistry.registerParticipant( dashboardUIComponent );

            dashboardUIComponent.setProjectRegistry( projectRegistry );
        }
    }

}
