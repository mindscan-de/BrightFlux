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
package de.mindscan.brightflux.plugin.search.persistence;

import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;

/**
 * 
 */
public class SearchPersistenceModuleImpl implements SearchPersistenceModule {

    private static final String FURIOUS_IRON_REST_CONTENT_URL_KEY = "furious.iron.rest.content.url";
    private static final String FURIOUS_IRON_REST_QUERY_URL_KEY = "furious.iron.rest.query.url";

    private BasePersistenceModule basePersistenceModule;

    /**
     * @param searchBasePersistenceModule
     */
    public SearchPersistenceModuleImpl( BasePersistenceModule searchBasePersistenceModule ) {
        this.basePersistenceModule = searchBasePersistenceModule;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getFuriousIronQueryURL() {
        return basePersistenceModule.getStringValue( FURIOUS_IRON_REST_QUERY_URL_KEY );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getFuriousIronContentURL() {
        return basePersistenceModule.getStringValue( FURIOUS_IRON_REST_CONTENT_URL_KEY );
    }

}
