/**
 * Copyright (C) 2009 DANS (info@dans.knaw.nl)
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
