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

import de.mindscan.brightflux.viewer.uievents.DataFrameRequestSelectEvent;
import de.mindscan.brightflux.viewer.uievents.DataFrameRowSelectedEvent;
import de.mindscan.brightflux.viewer.uievents.DataFrameSelectedEvent;
import de.mindscan.brightflux.viewer.uievents.LocatePredictedTimestampAllDataframesRequestedEvent;
import de.mindscan.brightflux.viewer.uievents.LocatePredictedTimestampRequestedEvent;
import de.mindscan.brightflux.viewer.uievents.LocatePredictedTimestampSelectedDataframeRequestedEvent;
import de.mindscan.brightflux.viewer.uievents.VideoObjectRequestSelectEvent;

/**
 * 
 */
public class UIEvents {

    // Broadcast event, that a Frame was selected in the UI
    public final static Class<?> DataFrameSelectedEvent = DataFrameSelectedEvent.class;

    // Broadcast event, that a row was selected in the UI
    public final static Class<?> DataFrameRowSelectedEvent = DataFrameRowSelectedEvent.class;

    // Request event, for a data frame to be selected in the UI
    public final static Class<?> DataFrameRequestSelectEvent = DataFrameRequestSelectEvent.class;

    // Request event, for a video object to be selected in the UI
    public final static Class<?> VideoObjectRequestSelectEvent = VideoObjectRequestSelectEvent.class;

    // Locate Predicted Timestamp in current DataFrame in the UI
    public final static Class<?> LocatePredictedTimstampEvent = LocatePredictedTimestampRequestedEvent.class;

    // Locate Predicted Timestamp in current DataFrame in the UI
    public final static Class<?> LocatePredictedTimstampSelectedDataframeEvent = LocatePredictedTimestampSelectedDataframeRequestedEvent.class;

    // Locate Predicted Timestamp in current DataFrame in the UI
    public final static Class<?> LocatePredictedTimstampAllDataframesEvent = LocatePredictedTimestampAllDataframesRequestedEvent.class;

}
