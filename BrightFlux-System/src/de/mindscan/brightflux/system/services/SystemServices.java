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

    // --------------------------------------------------------------------------
    // TODO get rid of the direct getters and setters, this is too much coupling.
    //      this was/is useful for rapid prototyping the whole thing. Now that we
    //      can see how this thing is used, we can do he proper abstractions from
    //      working code.
    // --------------------------------------------------------------------------    

    public FavRecipesComponent getFavRecipesServices() {
        return getService( FavRecipesComponent.class );
    }

    public void setEarlyPersistence( EarlyPersistenceComponent earlyPersistence ) {
        registerService( earlyPersistence, EarlyPersistenceComponent.class );
    }

    public EarlyPersistenceComponent getEarlyPersistence() {
        return getService( EarlyPersistenceComponent.class );
    }

    // TODO this is currently unused but acts as a reminder to do the correct init on components using AnnotatorComponent. 
    public AnnotatorComponent getAnnotatorService() {
        return getService( AnnotatorComponent.class );
    }

    public void setProjectRegistry( ProjectRegistry projectRegistry ) {
        registerService( projectRegistry, ProjectRegistry.class );
    }

    public ProjectRegistry getProjectRegistry() {
        return getService( ProjectRegistry.class );
    }

    // ---------------------------------------------------------------
    // replacement implementation  for the direct setters and getters.
    // ---------------------------------------------------------------    

    @SuppressWarnings( "unchecked" )
    public <T> T getService( Class<T> serviceClazz ) {
        // TODO: check whether this is assignable...
        return (T) registeredServices.get( serviceClazz );
    }

    public <T> void registerService( Object serviceInstance, Class<T> serviceClazz ) {
        // TODO: some checks
        registeredServices.put( serviceClazz, serviceInstance );
    }

    public <T> boolean isServiceAvailable( Class<T> serviceClazz ) {
        return registeredServices.containsKey( serviceClazz );
    }

    public <T> boolean isServiceValid( Class<T> serviceClazz ) {
        return registeredServices.containsKey( serviceClazz ) && (registeredServices.get( serviceClazz ) != null);
    }

    // MarkerService
    // we want a marker service

}
