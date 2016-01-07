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
