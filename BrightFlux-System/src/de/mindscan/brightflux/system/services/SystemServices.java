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
package de.mindscan.brightflux.system.services;

import java.util.HashMap;
import java.util.Map;

import de.mindscan.brightflux.framework.registry.ProjectRegistry;
import de.mindscan.brightflux.system.annotator.AnnotatorComponent;
import de.mindscan.brightflux.system.earlypersistence.EarlyPersistenceComponent;
import de.mindscan.brightflux.system.favrecipes.FavRecipesComponent;
import de.mindscan.brightflux.system.reportgenerator.ReportGeneratorComponent;
import de.mindscan.brightflux.system.videoannotator.VideoAnnotatorComponent;

/**
 * As much as I dislike these Singletons, these are actually mockable and configurable objects in 
 * terms of unit tests speaking.
 */
public class SystemServices {

    private Map<Class<?>, Object> registeredServices = new HashMap<>();

    private static class SystemServicesHolder {
        private static SystemServices instance = new SystemServices();
    }

    public static SystemServices getInstance() {
        return SystemServicesHolder.instance;
    }

    private SystemServices() {
    }

    public FavRecipesComponent getFavRecipesServices() {
        return getService( FavRecipesComponent.class );
    }

    public void setVideoAnnotationService( VideoAnnotatorComponent videoAnnotatorService ) {
        registerService( videoAnnotatorService, VideoAnnotatorComponent.class );
    }

    public VideoAnnotatorComponent getVideoAnnotatorService() {
        return getService( VideoAnnotatorComponent.class );
    }

    public void setReportGeneratorService( ReportGeneratorComponent reportGeneratorService ) {
        registerService( reportGeneratorService, ReportGeneratorComponent.class );
    }

    public ReportGeneratorComponent getReportGeneratorService() {
        return getService( ReportGeneratorComponent.class );
    }

    public void setEarlyPersistence( EarlyPersistenceComponent earlyPersistence ) {
        registerService( earlyPersistence, EarlyPersistenceComponent.class );
    }

    public EarlyPersistenceComponent getEarlyPersistence() {
        return getService( EarlyPersistenceComponent.class );
    }

    public void setAnnotatorService( AnnotatorComponent annotatorComponent ) {
        registerService( annotatorComponent, AnnotatorComponent.class );
    }

    public AnnotatorComponent getAnnotatorService() {
        return getService( AnnotatorComponent.class );
    }

    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        registerService( projectRegistry, ProjectRegistry.class );
    }

    public ProjectRegistry getProjectRegistry() {
        return getService( ProjectRegistry.class );
    }

    // replacement implementation  for the direct setters.

    @SuppressWarnings( "unchecked" )
    private <T> T getService( Class<T> clazz ) {
        // TODO: check whether this is assignable...
        return (T) registeredServices.get( clazz );
    }

    public void registerService( Object serviceInstance, Class<?> serviceClazz ) {
        // TODO: some checks
        registeredServices.put( serviceClazz, serviceInstance );
    }

    // MarkerService
    // we want a marker service

}
