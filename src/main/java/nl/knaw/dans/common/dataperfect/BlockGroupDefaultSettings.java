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
 * Reads the block group containing the default settings.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 */
final class BlockGroupDefaultSettings
    extends AbstractBlockGroup
{
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
