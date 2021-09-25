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
package de.mindscan.brightflux.system.dataframehierarchy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import de.mindscan.brightflux.dataframes.DataFrame;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchy;
import de.mindscan.brightflux.system.dataframehierarchy.DataFrameHierarchyNode;

/**
 * 
 */
public class DataFrameHierarchyImpl implements DataFrameHierarchy {

    private List<DataFrameHierarchyNode> rootNodes = new ArrayList<>();

    private Map<UUID, DataFrameHierarchyNode> nodes = new HashMap<>();

    /** 
     * {@inheritDoc}
     */
    @Override
    public void addLeafNode( DataFrame dataFrame, UUID parentDataFrameUUID ) {
        //
        DataFrameHierarchyNode parentNode = nodes.get( parentDataFrameUUID );
        DataFrameHierarchNodeImpl newLeafNode = new DataFrameHierarchNodeImpl( dataFrame.getName(), dataFrame.getUuid(), parentNode );

        nodes.put( dataFrame.getUuid(), newLeafNode );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void addRootNode( DataFrame dataFrame ) {
        DataFrameHierarchNodeImpl newRootNode = new DataFrameHierarchNodeImpl( dataFrame.getName(), dataFrame.getUuid(), null );
        nodes.put( dataFrame.getUuid(), newRootNode );
        rootNodes.add( newRootNode );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public void removeNode( DataFrame dataFrame ) {
        UUID uuid = dataFrame.getUuid();

        // TODO: check if it is a root node.
        rootNodes = rootNodes.stream().filter( n -> !n.getDataFrameUUID().equals( uuid ) ).collect( Collectors.toList() );

        // TODO: (disable intermediate dataframes, remove leaf dataframes)
        System.out.println( "disable node" );
        // remove the node finally from the table.
        nodes.remove( uuid );
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<DataFrameHierarchyNode> getRootNodes() {
        return rootNodes;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public List<DataFrameHierarchyNode> getChildren( DataFrameHierarchyNode parentElement ) {
        UUID parentUUID = parentElement.getDataFrameUUID();
        if (parentUUID != null) {
            return nodes.values().stream().filter( node -> parentUUID.equals( node.getParentDataFrameUUID() ) ).collect( Collectors.toList() );
        }

        return new ArrayList<>();
    }

    @Override
    public boolean hasChildren( DataFrameHierarchyNode parentElement ) {
        UUID parentUUID = parentElement.getDataFrameUUID();
        if (parentUUID != null) {
            return nodes.values().stream().filter( node -> parentUUID.equals( node.getParentDataFrameUUID() ) ).collect( Collectors.toList() )
                            .isEmpty() == false;
        }

        return false;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public DataFrameHierarchyNode getParent( DataFrameHierarchyNode element ) {
        if (element == null) {
            return null;
        }

        UUID parentUUID = element.getParentDataFrameUUID();

        if (parentUUID != null) {
            return nodes.get( parentUUID );
        }

        return null;
    }
}
