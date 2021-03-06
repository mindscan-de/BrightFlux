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
package de.mindscan.brightflux.plugin.annotator.events;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.system.events.dataframe.BFAbstractDataFrameEvent;

/**
 * 
 */
public class AnnotationDataFrameLoadedEvent extends BFAbstractDataFrameEvent {

    private DataFrame referenceDataFrame;

    /**
     * @param referenceDataFrame
     * @param annotationDataFrame
     */
    public AnnotationDataFrameLoadedEvent( DataFrame referenceDataFrame, DataFrame annotationDataFrame ) {
        super( annotationDataFrame );
        this.referenceDataFrame = referenceDataFrame;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getBFEventMessage() {
        return "Annotation Dataframe loaded";
    }

    /**
     * @return the referenceDataFrame
     */
    public DataFrame getReferenceDataFrame() {
        return referenceDataFrame;
    }

    /**
     * @return the selectedDataFrame
     */
    public DataFrame getAnnotationDataFrame() {
        return getDataFrame();
    }

}
