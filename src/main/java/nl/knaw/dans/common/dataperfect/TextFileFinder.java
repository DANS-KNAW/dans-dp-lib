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
    private final DatabaseSettings databaseSettings;

    TextFileFinder(final File directory, final DatabaseSettings databaseSettings)
    {
        this.directory = directory;
        this.databaseSettings = databaseSettings;
    }

    TextFile find()
    {
        final FilenameFilter filter =
            new FilenameFilter()
            {
                public boolean accept(final File directory, final String fileName)
                {
                    if (fileName.length() > ".TXX".length())
                    {
                        return fileName.substring(fileName.length() - ".TXX".length()).equalsIgnoreCase(".TXX");
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
