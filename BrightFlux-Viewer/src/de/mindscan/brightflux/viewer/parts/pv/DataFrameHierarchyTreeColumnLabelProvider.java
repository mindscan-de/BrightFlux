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
package de.mindscan.brightflux.viewer.parts.pv;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import de.mindscan.brightflux.plugin.dataframehierarchy.DataFrameHierarchyNode;

/**
 * 
 */
public class DataFrameHierarchyTreeColumnLabelProvider extends ColumnLabelProvider {

    private final String columnName;

    public final static String DF_NAME_HEADER = "Name";
    public final static String DF_UUID = "UUID";

    /**
     * 
     */
    public DataFrameHierarchyTreeColumnLabelProvider( String columnName ) {
        this.columnName = columnName;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getText( Object element ) {
        if (element == null) {
            return "";
        }

        DataFrameHierarchyNode node = (DataFrameHierarchyNode) element;

        switch (columnName) {
            case DF_NAME_HEADER:
                return node.getDataFrameName();

            case DF_UUID:
                return String.valueOf( node.getDataFrameUUID() );

            default:
                return super.getText( element );
        }
    }
}
