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
