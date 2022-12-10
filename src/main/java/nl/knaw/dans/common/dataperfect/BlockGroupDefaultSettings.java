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
 * Reads the block group containing the default settings.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 */
final class BlockGroupDefaultSettings
    extends AbstractBlockGroup
{
    /*
     * 15 bytes are allocated for the default settings, out of which 13 are actually used.
     */
    final short color1;
    final short color2;
    final short color3;
    final short color4;
    final short menuEditColor;
    final short period;
    final short comma;
    final short ymd;
    final short hms;
    final short line0;
    final short date0Type;
    final short autoHelp;
    final short reportDisplayOnOff;

    BlockGroupDefaultSettings(final int blockNumber, final StructureFile structureFile)
                       throws IOException
    {
        super(blockNumber, structureFile);

        color1 = structureFile.readByte();
        color2 = structureFile.readByte();
        color3 = structureFile.readByte();
        color4 = structureFile.readByte();
        menuEditColor = structureFile.readByte();
        period = structureFile.readByte();
        comma = structureFile.readByte();
        ymd = structureFile.readByte();
        hms = structureFile.readByte();
        line0 = structureFile.readByte();
        date0Type = structureFile.readByte();
        autoHelp = structureFile.readByte();
        reportDisplayOnOff = structureFile.readByte();
    }
}
