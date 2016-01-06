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
 * Represents the 4th block group (at position 3 in the file). This blockgroup is at the root of all
 * the data we are interested in. That is to say, it contains the pointers to the other interesting
 * blockgroups.
 *
 * @author Jan van Mansum
 */
final class BlockGroup3
    extends AbstractBlockGroup
{
    final int blkPrinterMaps;
    final int blkConstants;
    final int blkPanels;

    BlockGroup3(final StructureFile structureFile)
         throws IOException
    {
        super(3, structureFile);

        /*
         * We do not know what the next 4 bytes are used for.
         */
        structureFile.skipBytes(4);

        blkPrinterMaps = structureFile.readBlockNumber();
        blkConstants = structureFile.readBlockNumber();
        blkPanels = structureFile.readBlockNumber();

        // TODO: Read the rest of the group 3 fields.
    }
}
