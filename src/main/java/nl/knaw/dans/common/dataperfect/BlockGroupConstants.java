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
