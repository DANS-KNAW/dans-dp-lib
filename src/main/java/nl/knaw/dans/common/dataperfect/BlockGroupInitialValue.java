/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.common.dataperfect;

import java.io.IOException;

/**
 * Reads a block group containing an initial field value.
 *
 * The value is returned as a string.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 *
 */
final class BlockGroupInitialValue
    extends AbstractBlockGroup
{
    private final String text;

    BlockGroupInitialValue(final int blockNumber, final StructureFile structureFile, final String picture)
                    throws IOException
    {
        super(blockNumber, structureFile);
        text = readInitialValue(structureFile, picture);
    }

    private String readInitialValue(final StructureFile structureFile, final String picture)
                             throws IOException
    {
        short length = structureFile.readByte();

        if (isNumeric(picture))
        {
            /*
             * Read and format the initial value depending on the number of digits.
             *
             * DataPerfect uses 4 bytes for 9 digits or less, and 8 bytes otherwise.
             */
            return (length == 4) ? new NumberFormatter(picture).format(structureFile.readInteger())
                                 : new NumberFormatter(picture).format(structureFile.readPackedNumber());
        }
        else
        {
            return structureFile.readString(length);
        }
    }

    private boolean isNumeric(final String picture)
    {
        return picture.startsWith("N") || picture.startsWith("G") || picture.startsWith("H");
    }

    /**
     * @return the initial value as a string.
     */
    public String toString()
    {
        return text;
    }
}
