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
 * Reads the block group containing the constants.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 */
final class BlockGroupConstants
    extends AbstractBlockGroup
{
    final int blkSortTab;
    final int blkUCaseTab;
    final int blkAltMap;
    final int blkCtrlMap;
    final int blkMonthAbbreviations;
    final int blkDefaultSettings;
    final short userIDPanelNumber;

    BlockGroupConstants(final int blockNumber, final StructureFile structureFile)
                 throws IOException
    {
        super(blockNumber, structureFile);

        blkSortTab = structureFile.readBlockNumber();
        blkUCaseTab = structureFile.readBlockNumber();
        blkAltMap = structureFile.readBlockNumber();
        blkCtrlMap = structureFile.readBlockNumber();
        blkMonthAbbreviations = structureFile.readBlockNumber();
        blkDefaultSettings = structureFile.readBlockNumber();
        userIDPanelNumber = structureFile.readByte();
    }
}
