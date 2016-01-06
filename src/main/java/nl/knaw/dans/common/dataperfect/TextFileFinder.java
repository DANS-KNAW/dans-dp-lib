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

import java.io.File;
import java.io.FilenameFilter;

/**
 * Helper class that looks for the text file in a directory and returns a new {@link TextFile}
 * instance if found.
 *
 * @author Martin Braaksma
 *
 */
final class TextFileFinder
{
    private final File directory;
    private final String structureFileBaseName;
    private final DatabaseSettings databaseSettings;

    TextFileFinder(final File structureFile, final DatabaseSettings databaseSettings)
    {
        directory = structureFile.getParentFile();
        structureFileBaseName = structureFile.getName().substring(0, structureFile.getName().length() - ".STR".length());
        this.databaseSettings = databaseSettings;
    }

    TextFile find()
    {
        final String textFileName = structureFileBaseName + ".TXX";

        /*
         * Filter to search case insensitively.
         */
        final FilenameFilter filter =
            new FilenameFilter()
            {
                public boolean accept(final File directory, final String fileName)
                {
                    if (fileName.length() > ".TXX".length())
                    {
                        return fileName.equalsIgnoreCase(textFileName);
                    }

                    return false;
                }
            };

        final String[] files = directory.list(filter);

        if (files != null && files.length > 0)
        {
            return new TextFile(new File(directory, files[0]),
                                databaseSettings);
        }

        return null;
    }
}
