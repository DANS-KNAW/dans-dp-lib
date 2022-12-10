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
        byte[] iso88591buffer = new byte[totalLength]; //TODO

        int position = 0; 

        while (raFile.getFilePointer() < endPosition)
        {
            int subBlockLength = 0xff & readByte();

            while (subBlockLength == 0xff)
            {
                buffer[position++] = '\n';
                subBlockLength = 0xff & readByte();
            }

            raFile.read(buffer, position, subBlockLength);
            iso88591buffer = new String (buffer).getBytes("UTF-8");   //hola!!! das geht nicht!

            //valueUtf8 = new String(buffer, "ISO-8859-1").getBytes("UTF-8");
            //valueUtf8 = new String(buffer, "Windows-1252").getBytes("UTF-8");
           position += subBlockLength;
        }

        return new String(buffer, 0, position, "Windows-1252"); // hiermit sind schon mal alle Umlaute eindeutig!
    }
}
