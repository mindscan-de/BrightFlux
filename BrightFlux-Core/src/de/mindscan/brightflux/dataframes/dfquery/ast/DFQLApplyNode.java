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
package de.mindscan.brightflux.dataframes.dfquery.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * This node will only apply the arguments on a Node, but the function may be
 * a selector node on a dataFrameColumnNode, where the selector might be a 
 * method name like startsWith, endsWith, or contains. This node will only 
 * contain the arguments.
 * 
 * e.g. df.'myColumn'.startswith('0x666');
 * 
 * has the function: "df.'myColumn'.startsWith" (represented as the tree)
 * and the single only argument: '0x666'
 */
public class DFQLApplyNode implements DFQLNode {

    private DFQLNode function;
    private List<DFQLNode> arguments;

    public DFQLApplyNode( DFQLNode function ) {
        this( function, new ArrayList<DFQLNode>() );
    }

    public DFQLApplyNode( DFQLNode function, List<DFQLNode> arguments ) {
        this.function = function;
        this.arguments = arguments;
    }

    public DFQLNode getFunction() {
        return function;
    }

    public List<DFQLNode> getArguments() {
        return arguments;
    }
}
