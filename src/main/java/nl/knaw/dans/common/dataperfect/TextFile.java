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
 * Represents the database's "text file." That is the file that stores variable length data.
 *
 * @author Jan van Mansum
 */
final class TextFile
    extends DataPerfectFile
{
    private static final int OFFSET_LENGTH_DATA = 2;
    private static final int NEWLINE_CODE = 0xff;

    /*
     * Not exactly clear what this does, but we have to skip some bytes when we encounter it.
     */
    private static final int MARKUP_CODE = 0xfe;

    TextFile(final File file, final DatabaseSettings databaseSettings)
    {
        super(file, databaseSettings);
    }

    String readTextAt(final int blockNumber)
               throws IOException
    {
        jumpToBlockAt(blockNumber);
        skipBytes(OFFSET_LENGTH_DATA);

        final int totalLength = readUnsignedShort();
        final long endPosition = totalLength + raFile.getFilePointer();
        final byte[] buffer = new byte[totalLength];
        final StringBuilder stringBuilder = new StringBuilder(totalLength);

        while (raFile.getFilePointer() < endPosition)
        {
            int subBlockStartByte = 0xff & readByte();

            while (subBlockStartByte == NEWLINE_CODE || subBlockStartByte == MARKUP_CODE)
            {
                if (subBlockStartByte == MARKUP_CODE)
                {
                    skipBytes(6);
                }
                else if (subBlockStartByte == NEWLINE_CODE)
                {
                    stringBuilder.append('\n');
                }

                subBlockStartByte = readByte();
            }

            /*
             * If it is not a special code, the start code is the length of the subblock.
             */
            raFile.read(buffer, 0, subBlockStartByte);

            final String bufferString = new String(buffer,
                                                   0,
                                                   subBlockStartByte,
                                                   databaseSettings.getCharsetName());
            stringBuilder.append(replaceNonPrintableCharacters(bufferString));
        }

        return stringBuilder.toString();
    }
}
