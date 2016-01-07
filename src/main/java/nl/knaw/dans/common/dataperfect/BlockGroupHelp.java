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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class BlockGroupHelp
    extends AbstractBlockGroup
{
    private static final int COLOR_REFERENCE_VALUE = 254;
    private static final int NEW_LINE_VALUE = 255;
    private final List<Object> helpTextParts = new ArrayList<Object>();

    final class Text
    {
        short length;
        String text;

        Text(final short value, final StructureFile structureFile)
            throws IOException
        {
            length = value;
            text = structureFile.readString(length);
        }

        public String toString()
        {
            return text;
        }
    }

    static final class NewLine
    {
        private static final NewLine INSTANCE = new NewLine();

        public String toString()
        {
            return "\n";
        }
    }

    BlockGroupHelp(final int blockNumber, final StructureFile structureFile)
            throws IOException
    {
        super(blockNumber, structureFile);

        short value = structureFile.readByte();

        while (value != 0)
        {
            switch (value)
            {
                case COLOR_REFERENCE_VALUE:

                    final short length = structureFile.readByte();
                    /*
                     * Skip the text color bytes
                     */
                    structureFile.skipBytes(length);

                    break;

                case NEW_LINE_VALUE:
                    helpTextParts.add(NewLine.INSTANCE);

                    break;

                default:
                    helpTextParts.add(new Text(value, structureFile));
            }

            value = structureFile.readByte();
        }
    }

    /*
      * @return the help text as a string
      */
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final Object helpStringPart : helpTextParts)
        {
            stringBuilder.append(helpStringPart);
        }

        return stringBuilder.toString();
    }
}
