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
package de.mindscan.brightflux.viewer.uievents;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.system.events.dataframe.BFAbstractDataFrameEvent;

/**
 * 
 */
public class DataFrameRowSelectedEvent extends BFAbstractDataFrameEvent implements UIBFEvent {

    private int selectedIndex;
    private Object selectedItem;

    /**
     * 
     */
    public DataFrameRowSelectedEvent( DataFrame dataFrame, int selectedIndex, Object selectedItem ) {
        super( dataFrame );
        this.selectedIndex = selectedIndex;
        this.selectedItem = selectedItem;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getBFEventMessage() {
        return String.format( "DataFrame '%s' Rows XYZ selected.", getDataFrame().getName() );
    }

    /**
     * @return the selectedIndex
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * @return the selectedItem
     */
    public Object getSelectedItem() {
        return selectedItem;
    }
}
