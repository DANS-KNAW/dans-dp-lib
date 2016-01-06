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
