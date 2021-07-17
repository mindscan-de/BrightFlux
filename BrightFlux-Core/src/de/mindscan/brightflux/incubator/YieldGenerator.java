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
package de.mindscan.brightflux.incubator;

import java.util.Iterator;

/**
 * I like the idea of python generators, this idea seems a bit inefficient
 */
public class YieldGenerator<T> implements Iterable<T> {

    // TODO: we have a producer thread, which we will put to sleep after a few yield calls / one yield call
    // TODO: The producer/Generator should get a lambda or so it can be detected if it finished.
    // TODO: What about infinite generators, how to stop them?

    /** 
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public T next() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    void yield( T element ) {
        // Put an element into a queue / into a one element queue
        // a one element queue will have a lot of threadswitches for each yielded element, 
        // so it is important to not use this excessively not for each char, but tokens are fine, 
        // as long as we reduce the number of items. 
    }

}
