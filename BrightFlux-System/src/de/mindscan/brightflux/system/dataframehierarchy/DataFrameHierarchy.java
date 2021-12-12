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
package de.mindscan.brightflux.system.dataframehierarchy;

import java.util.List;
import java.util.UUID;

import de.mindscan.brightflux.dataframes.DataFrame;

/**
 * 
 */
public interface DataFrameHierarchy {

    /**
     * 
     */
    List<DataFrameHierarchyNode> getRootNodes();

    /**
     * @param parentElement
     */
    List<DataFrameHierarchyNode> getChildren( DataFrameHierarchyNode parentElement );

    /**
     * @param parentElement
     * @return
     */
    boolean hasChildren( DataFrameHierarchyNode parentElement );

    /**
     * @param element
     * @return
     */
    DataFrameHierarchyNode getParent( DataFrameHierarchyNode element );

    /**
     * @param currentSelectedID
     */
    DataFrameHierarchyNode getNodeByUUID( UUID currentSelectedID );

    // --------
    // OBSOLETE
    // --------    

    // TODO: remove this from the Interface - Interface after refactoring exposes too much
    /**
     * @param dataFrame
     * @param parentUUID 
     */
    void addLeafNode( DataFrame dataFrame, UUID parentUUID );

    // TODO: remove this from the Interface - Interface after refactoring exposes too much
    /**
     * @param dataFrame
     */
    void addRootNode( DataFrame dataFrame );

    // TODO: remove this from the Interface - Interface after refactoring exposes too much
    /**
     * @param dataFrame
     */
    void removeNode( DataFrame dataFrame );

}
