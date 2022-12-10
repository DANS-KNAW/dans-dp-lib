/*
 * Copyright 2009 Data Archiving and Networked Services (DANS), Netherlands.
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
 * Base class for all blocks groups. Takes care of jumping to the correct position in the structure
 * file and the reading the block group header.
 *
 * @author Jan van Mansum
 */
abstract class AbstractBlockGroup
{
    final int blockNumber;
    final int nrBlockPreviousGroup;
    final int nrBytesAfterHeader;

    /**
     * Initializes an abstract block group.
     *
     * @param blockNumber the block number at which the group starts (zero-based)
     * @param structureFile the structure file from which to read
     * @throws IOException if structure file cannot be read
     */
    AbstractBlockGroup(final int blockNumber, final StructureFile structureFile)
                throws IOException
    {
        this.blockNumber = blockNumber;
        structureFile.jumpToBlockAt(blockNumber);

        nrBlockPreviousGroup = structureFile.readUnsignedShort();
        nrBytesAfterHeader = structureFile.readUnsignedShort();
    }
}
