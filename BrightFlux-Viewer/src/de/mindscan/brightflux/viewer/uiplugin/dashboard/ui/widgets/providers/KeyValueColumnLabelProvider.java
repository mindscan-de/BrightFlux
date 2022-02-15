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
package de.mindscan.brightflux.viewer.uiplugin.dashboard.ui.widgets.providers;

import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * 
 */
public class KeyValueColumnLabelProvider extends ColumnLabelProvider {

    private final String columnname;
    private Map<String, String> map;

    /**
     * 
     */
    public KeyValueColumnLabelProvider( String columnname, Map<String, String> map ) {
        this.columnname = columnname;
        this.map = map;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getText( Object element ) {
        if (element == null) {
            return "";
        }

        String key = (String) element;

        if ("Key".equals( columnname )) {
            return key;
        }

        if ("Value".equals( columnname )) {
            if (map.containsKey( key )) {
                return map.get( key );
            }
            return "N/A";
        }

        return super.getText( element );
    }

}
