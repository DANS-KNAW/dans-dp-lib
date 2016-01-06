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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;

/**
 * Represents a data file (file where the actual data of the panel resides).
 *
 * @author Jan van Mansum
 */
final class DataFile
    extends DataPerfectFile
{
    private static final int OFFSET_NR_OF_RECORDS_IN_FILE = 28;
    private static final int OFFSET_START_OF_RECORDS = 32;
    private final int recordSize;

    DataFile(final File file, final int recordSize, final DatabaseSettings databaseSettings)
    {
        super(file, databaseSettings);
        this.recordSize = recordSize;
    }

    int readNumberOfRecords()
                     throws IOException
    {
        raFile.seek(OFFSET_NR_OF_RECORDS_IN_FILE);

        try
        {
            return readInteger();
        }
        catch (EOFException e)
        {
            return 0;
        }
    }

    void jumpToRecordField(final int recordIndex, final int offsetInRecord)
                    throws IOException
    {
        raFile.seek(OFFSET_START_OF_RECORDS + recordIndex * recordSize + offsetInRecord);
    }
}
