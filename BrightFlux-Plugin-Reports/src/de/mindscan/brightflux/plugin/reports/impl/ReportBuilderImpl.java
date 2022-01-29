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
package de.mindscan.brightflux.plugin.reports.impl;

import java.util.Map;

import de.mindscan.brightflux.plugin.reports.ReportBuilder;
import de.mindscan.brightflux.plugin.reports.engine.BFTemplateContentProvider;
import de.mindscan.brightflux.plugin.reports.engine.BFTemplateImpl;

/**
 * 
 */
public class ReportBuilderImpl implements ReportBuilder {

    private final String templateInfo;
    private final BFTemplateImpl currentTemplateInstance;
    private final boolean contentProviderMode;

    /**
     * 
     */
    public ReportBuilderImpl( String templateName, BFTemplateContentProvider templateCcontentProvider ) {
        this.templateInfo = templateName;
        this.contentProviderMode = true;
        this.currentTemplateInstance = new BFTemplateImpl( templateCcontentProvider );
    }

    public ReportBuilderImpl( String template ) {
        this.templateInfo = template;
        this.contentProviderMode = false;
        this.currentTemplateInstance = new BFTemplateImpl();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void block( String blockName, Map<String, String> templateData ) {
        currentTemplateInstance.block( blockName, templateData );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String render( Map<String, String> templateData ) {
        if (contentProviderMode) {
            return currentTemplateInstance.renderTemplateFromContentProvider( templateInfo, templateData );
        }

        return currentTemplateInstance.renderTemplate( templateInfo, templateData );
    }

}
