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
package de.mindscan.brightflux.dataframes.dfquery.runtime;

import de.mindscan.brightflux.dataframes.dfquery.ast.DFQLNode;

/**
 * 
 * This is more of a runtime node type produced by the compiler. It is still an AST node but 
 * contains type information. The original AST produced by the parser doesn't know anything
 * about data frames or columns, but those are essential for the runtime, that it knows its
 * implicit types. And a typed tree is then easier to evaluate or to compile further.
 *
 */
public interface TypedDFQLNode extends DFQLNode {

    // TODO: implement the return type calculation and add an interface for the returntype (setters and getters)

    // TODO: also a node should report its own type e.g. a dataframe columnnode is a dataframe column 
    //       node but may return strings. So there is a clear difference between these two information
    //       let's see how that works out.

}
