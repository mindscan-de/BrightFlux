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

import de.mindscan.brightflux.exceptions.NotYetImplemetedException;
import de.mindscan.brightflux.plugin.highlighter.HighlighterActivator;
import de.mindscan.brightflux.plugin.videoannotator.VideoAnnotatorActivator;
import de.mindscan.brightflux.system.annotator.AnnotatorActivator;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchyActivator;
import de.mindscan.brightflux.system.earlypersistence.EarlyPersistenceActivator;
import de.mindscan.brightflux.system.favrecipes.FavRecipesActivator;
import de.mindscan.brightflux.system.projectregistry.ProjectRegistryActivator;
import de.mindscan.brightflux.system.reportgenerator.ReportGeneratorActivator;
import de.mindscan.brightflux.system.services.SystemServices;

/**
 * 
 */
public class BrightFluxViewerStartup {

    /**
     * TODO: 
     * 
     * refactor this, to retrieve the startup configuration from early Persistence and then execute the 
     * startup configuration. But for now this is quite good enough. Also I want to registerServices and
     * getServices using Interfaces instead of specialized setters and getters in the systemServices to 
     * be able to have a plugin system. 
     *   
     */
    public void start() {
        // STARTUP : System Services
        SystemServices systemServices = SystemServices.getInstance();

        // STARTUP : Early Persistence data (avoid hard coded dependencies)
        EarlyPersistenceActivator earlyPersistenceActivator = new EarlyPersistenceActivator();
        earlyPersistenceActivator.start( systemServices );

        // TODO: get early startup configuration from early persistence configuration 
        // TODO: start these configured components as individual bundles

        // STARTUP : Startup the Project registry 
        ProjectRegistryActivator projectRegistryActivator = new ProjectRegistryActivator();
        projectRegistryActivator.start( systemServices );

        // STARTUP : register Dataframe Hierarchy 
        DataFrameHierarchyActivator dataFrameHierarchyActivator = new DataFrameHierarchyActivator();
        dataFrameHierarchyActivator.start( systemServices );

        // STARTUP : Register Favorite Recipes Service + Collect Favorite Recipes
        FavRecipesActivator favrecipesActivator = new FavRecipesActivator();
        favrecipesActivator.start( systemServices );

        // STARTUP : Register Annotator Service
        AnnotatorActivator annotatorActivator = new AnnotatorActivator();
        annotatorActivator.start( systemServices );

        // STARTUP : Register Video Annotation Service
        VideoAnnotatorActivator videoAnnotatorActivator = new VideoAnnotatorActivator();
        videoAnnotatorActivator.start( systemServices );

        // STARTUP : Register Highlighter Annototor
        HighlighterActivator highlighterActivator = new HighlighterActivator();
        highlighterActivator.start( systemServices );

        // STARTUP : Register Report Generator Service
        ReportGeneratorActivator reportGeneratorActivator = new ReportGeneratorActivator();
        reportGeneratorActivator.start( systemServices );

        // TODO: the configuration can be distributed with configuration events -> or with a 

        // STARTUP : Complete Project Participant registration
        finishProjectParticipantRegistration( systemServices );
    }

    public void finishProjectParticipantRegistration( SystemServices systemServices ) {
        // Complete the Registration of the Startup-Items Startup-Steps
        if (systemServices.getProjectRegistry() != null) {
            systemServices.getProjectRegistry().completeParticipantRegistration();
        }
        else {
            throw new NotYetImplemetedException( "Startup could not complete, because project registry is not available." );
        }
    }

}
