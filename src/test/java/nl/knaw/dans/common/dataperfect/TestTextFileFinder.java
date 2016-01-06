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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.File;

public class TestTextFileFinder
{
    private TextFileFinder textFileFinder;

    @Test
    public void testStandardCase()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/StandardTextFileExtension/TextFile.TXX"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals("TextFile.TXX",
                     textFileFinder.find().file.getName());
    }

    @Test
    public void testNonStandardExtension()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/NonStandardTextFileExtension/TextFile.TXX"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals("TextFile.tXx",
                     textFileFinder.find().file.getName());
    }

    @Test
    public void testFileLengthTooShort()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/TextFileExtensionTooShort/TextFile.tx"),
                                            DefaultTestDatabaseSettings.INSTANCE);
        assertEquals(null,
                     textFileFinder.find());
    }

    @Test
    public void testFileLengthTooLong()
    {
        textFileFinder = new TextFileFinder(new File("src/test/resources/DP26G/TEXTFILES/TextFileExtensionTooLong/TextFile.TXXX"),
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
