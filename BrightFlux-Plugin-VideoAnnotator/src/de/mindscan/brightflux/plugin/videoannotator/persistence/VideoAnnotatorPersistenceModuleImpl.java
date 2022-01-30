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
package de.mindscan.brightflux.plugin.videoannotator.persistence;

import de.mindscan.brightflux.system.earlypersistence.BasePersistenceModule;

/**
 * 
 */
public class VideoAnnotatorPersistenceModuleImpl implements VideoAnnotatorPersistenceModule {

    public static final String VIDEO_ANNOTATOR_PLUGIN_SNIPPETS_KEY = "videoannotator.snippets";
    public static final String VIDEO_ANNOTATOR_PLUGIN_REPORT_NAMES_KEY = "videoannotator.report.names";
    public static final String VIDEO_ANNOTATOR_PLUGIN_REPORT_TEMPLATES_KEY = "videoannotator.report.templates";

    private BasePersistenceModule persistenceModule;

    /**
     * @param videoannotatorBsaePersistenceModule
     */
    public VideoAnnotatorPersistenceModuleImpl( BasePersistenceModule persistenceModule ) {
        this.persistenceModule = persistenceModule;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String[] getVideoAnnotationSnippets() {
        String[] result = persistenceModule.getStringArrayValue( VIDEO_ANNOTATOR_PLUGIN_SNIPPETS_KEY );
        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String[] getAvailableReportNames() {
        String[] result = persistenceModule.getStringArrayValue( VIDEO_ANNOTATOR_PLUGIN_REPORT_NAMES_KEY );
        return result;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String[] getAvailableReportTemplateNames() {
        String[] result = persistenceModule.getStringArrayValue( VIDEO_ANNOTATOR_PLUGIN_REPORT_TEMPLATES_KEY );
        return result;
    }

}
