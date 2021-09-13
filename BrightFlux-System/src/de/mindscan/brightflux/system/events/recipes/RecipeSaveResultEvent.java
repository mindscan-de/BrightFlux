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
package de.mindscan.brightflux.system.events.recipes;

import java.nio.file.Path;

import de.mindscan.brightflux.system.events.BFAbstractEvent;

/**
 * 
 */
public class RecipeSaveResultEvent extends BFAbstractEvent {

    private Path targetFile;
    private boolean success;

    /**
     * @param targetFile 
     * @param success
     */
    public RecipeSaveResultEvent( Path targetFile, boolean success ) {
        this.targetFile = targetFile;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailed() {
        return !success;
    }

    public String getPath() {
        return targetFile.toString();
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public String getBFEventMessage() {
        if (success) {
            return String.format( "Recipe saved as: '%s'", getPath() );
        }
        return String.format( "Recipe could not be saved: '%s'", getPath() );
    }
}