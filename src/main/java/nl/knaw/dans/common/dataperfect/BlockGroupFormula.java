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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a block group containing a formula.
 *
 * The formula is returned as a string, not as a value.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 *
 */
final class BlockGroupFormula
    extends AbstractBlockGroup
{
    private static final int FIELD_REFERENCE_VALUE = 254;
    private static final int NEW_LINE_VALUE = 255;
    private static final int FORMULA_BYTES_READ = 3;
    private List<Object> formulaParts = new ArrayList<Object>();

    final class FieldReference
    {
        String reference;

        FieldReference(final StructureFile structureFile)
                throws IOException
        {
            short totalBlockLength = structureFile.readByte();
            structureFile.skipBytes(1);

            int formulaLength = structureFile.readUnsignedShort();
            int bytesBeforeFormula = totalBlockLength - formulaLength - FORMULA_BYTES_READ;
            structureFile.skipBytes(bytesBeforeFormula);

            reference = structureFile.readString(formulaLength);
        }

        public String toString()
        {
            return reference;
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

    BlockGroupFormula(final int blockNumber, final StructureFile structureFile)
               throws IOException
    {
        super(blockNumber, structureFile);
        readFormula(structureFile);
    }

    private void readFormula(final StructureFile structureFile)
                      throws IOException
    {
        short value = structureFile.readByte();

        while (value != 0)
        {
            switch (value)
            {
                case FIELD_REFERENCE_VALUE:
                    formulaParts.add(new FieldReference(structureFile));

                    break;

                case NEW_LINE_VALUE:
                    formulaParts.add(NewLine.INSTANCE);

                    break;

                default:
                    formulaParts.add(new Text(value, structureFile));
            }

            value = structureFile.readByte();
        }
    }

    /**
     * @return the formula as a string
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Object formulaPart : formulaParts)
        {
            stringBuilder.append(formulaPart);
        }

        return stringBuilder.toString();
    }
}
