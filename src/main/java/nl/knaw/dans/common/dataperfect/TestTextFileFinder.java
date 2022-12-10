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

public class TestTextFileFinder
{
    private TextFileFinder textFileFinder;

    @Test
    public void testStandardCase()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/StandardTextFileExtension"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals("TextFile.TXX",
                     textFileFinder.find().file.getName());
    }

    @Test
    public void testNonStandardExtension()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/NonStandardTextFileExtension"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals("TextFile.tXx",
                     textFileFinder.find().file.getName());
    }

    @Test
    public void testFileLengthTooShort()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/ExtensionTooShort"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals(null,
                     textFileFinder.find());
    }

    @Test
    public void testFileLengthTooLong()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/ExtensionTooLong"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals(null,
                     textFileFinder.find());
    }

    @Test
    public void testFileDoesNotExist()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals(null,
                     textFileFinder.find());
    }
}
