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
package de.mindscan.brightflux.plugin.videoannotator;

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.plugin.videoannotator.persistence.VideoAnnotatorPersistenceModule;
import de.mindscan.brightflux.plugin.videoannotator.persistence.VideoAnnotatorPersistenceModuleImpl;
import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;
import de.mindscan.brightflux.system.services.StartupParticipant;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class VideoAnnotatorActivator implements StartupParticipant {

    private static final String VIDEO_ANNOTATOR_PLUGIN_PERSISTENCE_NAMESPACE = "videoannotator-plugin";

    // TODO refactor constant this to the right place
    public static final String FFPROBE_PATH_KEY = "ffprobe.path";

    /**
     * @param systemServices
     */
    @Override
    public void start( SystemServices systemServices ) {
        BasePersistenceModule videoannotatorBasePersistenceModule = systemServices.getBasePersistenceModule( VIDEO_ANNOTATOR_PLUGIN_PERSISTENCE_NAMESPACE );
        VideoAnnotatorPersistenceModule persistenceModule = new VideoAnnotatorPersistenceModuleImpl( videoannotatorBasePersistenceModule );

        VideoAnnotatorComponent videoAnnotatorComponent = new VideoAnnotatorComponent();
        // TODO 
        // - set a fully configured persistence module instead, where the component can do everything in its own
        // - The component can also register listeners if it wants to be informed about changes (e.g. config pages)   
        videoAnnotatorComponent.setFFProbePath( systemServices.getEarlyPersistence().getPropertyAsPath( FFPROBE_PATH_KEY ) );
        systemServices.registerService( videoAnnotatorComponent, VideoAnnotatorComponent.class );

        ProjectRegistry projectRegistry = systemServices.getProjectRegistry();
        if (projectRegistry != null) {
            projectRegistry.registerParticipant( videoAnnotatorComponent );
        }
        else {
            throw new NotYetImplemetedException( "VideoAnnotatorActivator precondiftion not staisfied." );
        }
    }

}
