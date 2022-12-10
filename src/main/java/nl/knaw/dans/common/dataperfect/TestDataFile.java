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
