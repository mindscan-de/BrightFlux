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
package de.mindscan.brightflux.viewer.parts;

import de.mindscan.brightflux.framework.events.CommandExecutionExceptionEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionFinishedEvent;
import de.mindscan.brightflux.framework.events.CommandExecutionStartedEvent;
import de.mindscan.brightflux.system.annotator.events.AnnotationDataFrameCreatedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameClosedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameCreatedEvent;
import de.mindscan.brightflux.system.events.dataframe.DataFrameLoadedEvent;
import de.mindscan.brightflux.system.events.recipes.RecipeSaveResultEvent;
import de.mindscan.brightflux.system.videoannotator.events.VideoAnnotationVideoObjectCreatedEvent;

/**
 * This class acts as an anti corruption layer, in case of larger refactorings, of 
 * the events then only this class gets modified.
 * 
 * TODO: maybe refactor to Strings instead of classes in future. / but then isAssignable is more difficult, 
 *       when we want to use to use interfaces/ base classes for events as well, so we can subscribe to base/super 
 *       classes.
 */
public class SystemEvents {

    public final static Class<?> DataFrameLoaded = DataFrameLoadedEvent.class;
    public final static Class<?> DataFrameCreated = DataFrameCreatedEvent.class;
    public final static Class<?> DataFrameClosed = DataFrameClosedEvent.class;
    public final static Class<?> AnnotationDataFrameCreated = AnnotationDataFrameCreatedEvent.class;

    public final static Class<?> CommandExecutionException = CommandExecutionExceptionEvent.class;
    public final static Class<?> CommandExecutionFinished = CommandExecutionFinishedEvent.class;
    public final static Class<?> CommandExecutionStarted = CommandExecutionStartedEvent.class;

    public final static Class<?> RecipeSaveResult = RecipeSaveResultEvent.class;

    public final static Class<?> VideoAnnotationVideoObjectCreated = VideoAnnotationVideoObjectCreatedEvent.class;
}
