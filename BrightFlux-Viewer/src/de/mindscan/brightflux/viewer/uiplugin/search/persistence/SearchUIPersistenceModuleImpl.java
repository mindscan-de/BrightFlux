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
package de.mindscan.brightflux.viewer.uiplugin.search.persistence;

import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;

/**
 * 
 */
public class SearchUIPersistenceModuleImpl implements SearchUIPersistenceModule {

    private static final String SEARCH_UI_PROFILE_NAMES_KEY = "search.ui.profile.names";
    private static final String SEARCH_UI_PROFILE_NAME_SELECTED_KEY = "search.ui.profile.name.selected.id";

    private BasePersistenceModule persistenceModule;

    /**
     * @param searchUIBasePersistenceModule
     */
    public SearchUIPersistenceModuleImpl( BasePersistenceModule searchUIBasePersistenceModule ) {
        this.persistenceModule = searchUIBasePersistenceModule;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String[] getSearchProfileNames() {
        return persistenceModule.getStringArrayValue( SEARCH_UI_PROFILE_NAMES_KEY );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public int getSearchProfileNameSelected() {
        return persistenceModule.getIntValue( SEARCH_UI_PROFILE_NAME_SELECTED_KEY );
    }
}
