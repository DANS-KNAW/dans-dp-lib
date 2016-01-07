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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Jan van Mansum
 */
public class TestDataFile
{
    @Test
    public void shouldReadPackedValue()
                               throws IOException
    {
        final DataFile dataFile =
            new DataFile(new File("src/test/resources/DP26G/PACKED/PACPAN"),
                         0 /*
            * Ignoring
            * record size in
            * this test
            */, DefaultTestDatabaseSettings.INSTANCE);

        try
        {
            dataFile.open();
            dataFile.skipBytes(32);
            assertEquals(12345678901L,
                         dataFile.readPackedNumber());
            assertEquals(123456789012L,
                         dataFile.readPackedNumber());
        }
        finally
        {
            dataFile.close();
        }
    }

    @Test
    public void shouldReadAllTypes()
                            throws IOException
    {
        final DataFile dataFile =
            new DataFile(new File("src/test/resources/DP26G/TYPES/PANEL"), 0 /*
            * Ignoring record
            * size in this
            * test
            */, DefaultTestDatabaseSettings.INSTANCE);

        try
        {
            dataFile.open();
            dataFile.skipBytes(32);
            assertEquals("String value             ",
                         dataFile.readString(25));

            // Block number of actual data in text file. Not tested here.
            assertEquals(3,
                         dataFile.readBlockNumber());
            assertEquals(1234,
                         dataFile.readInteger());
            assertEquals(123456789012L,
                         dataFile.readPackedNumber());
            assertEquals(-1234,
                         dataFile.readInteger());
            assertEquals(-123456789012L,
                         dataFile.readPackedNumber());
        }
        finally
        {
            dataFile.close();
        }
    }
}
