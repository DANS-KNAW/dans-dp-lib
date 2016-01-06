/*
 * Copyright 2009-2016 Data Archiving and Networked Services (DANS), Netherlands.
 *
 * This file is part of DANS DataPerfect Library.
 *
 * DANS DataPerfect Library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * DANS DataPerfect Library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DANS DataPerfect
 * Library. If not, see <http://www.gnu.org/licenses/>.
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
