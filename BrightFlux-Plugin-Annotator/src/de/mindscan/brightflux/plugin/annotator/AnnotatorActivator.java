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
package de.mindscan.brightflux.plugin.annotator;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.plugin.annotator.persistence.AnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.annotator.persistence.AnnotatorPersistenceModuleImpl;
import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyComponent;
import de.mindscan.brightflux.plugin.reports.ReportGeneratorComponent;
import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;
import de.mindscan.brightflux.system.services.StartupParticipant;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class AnnotatorActivator implements StartupParticipant {

    private static final String ANNOTATOR_PLUGIN_PERSISTENCE_NAMESPACE = "annotator-plugin";

    /**
     * @param systemServices
     */
    @Override
    public void start( SystemServices systemServices ) {
        BasePersistenceModule annotatorBasePersistenceModule = systemServices.getBasePersistenceModule( ANNOTATOR_PLUGIN_PERSISTENCE_NAMESPACE );
        AnnotatorPersistenceModule persistenceModule = new AnnotatorPersistenceModuleImpl( annotatorBasePersistenceModule );

        AnnotatorComponent annotator = new AnnotatorComponent();
        annotator.setPersistenceModule( persistenceModule );

        ReportGeneratorComponent reportGeneratorComponent = systemServices.getService( ReportGeneratorComponent.class );
        annotator.setReportGeneratorComponent( reportGeneratorComponent );

        DataFrameHierarchyComponent dataFrameHierarchyComponent = systemServices.getService( DataFrameHierarchyComponent.class );
        annotator.setDataframeHierarchyComponent( dataFrameHierarchyComponent );

        systemServices.registerService( annotator, AnnotatorComponent.class );

        ProjectRegistry projectRegistry = systemServices.getProjectRegistry();
        if (projectRegistry != null) {
            projectRegistry.registerParticipant( annotator );
        }
        else {
            throw new NotYetImplemetedException( "AnnotatorActivator precondition not staisfied." );
        }
    }

}
