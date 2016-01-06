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

import java.io.File;
import java.io.IOException;

/**
 * Represents the database's structure file.
 *
 * @author Jan van Mansum
 */
final class StructureFile
    extends DataPerfectFile
{
    StructureFile(final File file, final DatabaseSettings databaseSettings)
    {
        super(file, databaseSettings);
    }

    void jumpToOffset(final int blockNumber, final int offset)
               throws IOException
    {
        raFile.seek(BLOCK_SIZE * blockNumber + offset);
    }
}
