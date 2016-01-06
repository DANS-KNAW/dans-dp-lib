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
