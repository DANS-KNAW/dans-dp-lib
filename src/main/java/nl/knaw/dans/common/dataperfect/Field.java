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


/**
 * Represents a field in a panel. A field is a "column in a table" but also contains user interface
 * information. See also the description of {@link Panel}. Examples of abstract table properties are
 * the number/name and type. Examples of UI information are the x- and y-coordinates.
 *
 * @see Panel
 *
 * @author Jan van Mansum
 */
public class Field
{
    private final FieldProperties properties;
    private int numberOfDigitsInFormat;
    private Type type;
    private Formatter formatter;

    Field(final FieldProperties properties)
        throws InvalidFormatException
    {
        this.properties = properties;

        type = getTypeFromFormat(this.properties.getFormat());

        if (type == Type.NONE)
        {
            return;
        }

        numberOfDigitsInFormat = calculateNumberOfDigitsFromFormat(this.properties.getFormat());
        formatter =
            createFormatter(type,
                            this.properties.getFormat(),
                            this.properties.getDatabaseSettings());
    }

    private static int calculateNumberOfDigitsFromFormat(final String format)
    {
        int nrDigits = 0;
        final char[] formatCharacters = format.toCharArray();

        for (int i = 0; i < formatCharacters.length; i++)
        {
            if (formatCharacters[i] == '9' || formatCharacters[i] == 'Z' || formatCharacters[i] == '*')
            {
                nrDigits++;
            }
        }

        return nrDigits;
    }

    private static Formatter createFormatter(final Type type, final String picture,
                                             final DatabaseSettings databaseSettings)
    {
        switch (type)
        {
            case A:
            case AA:
            case U:
            case UU:
                return new StringFormatter(picture);

            case N:
            case G:
            case H:
                return new NumberFormatter(picture);

            case D:
                return new DateFormatter(picture,
                                         databaseSettings.getDefaultDateOrder());

            case T:
                return new TimeFormatter(picture,
                                         databaseSettings.getDefaultTimeOrder());

            default:
                assert false : "Not all types handled";
        }

        return null;
    }

    private static Type getTypeFromFormat(final String format)
                                   throws InvalidFormatException
    {
        if (format == null)
        {
            return Type.NONE;
        }

        switch (format.charAt(0))
        {
            case 'N':
                return Type.N;

            case 'A':

                if (format.substring(1).contains("A"))
                {
                    return Type.AA;
                }

                return Type.A;

            case 'U':
                return Type.U;

            case 'G':
                return Type.G;

            case 'H':
                return Type.H;

            case 'D':
                return Type.D;

            case 'T':
                return Type.T;

            default:
                throw new InvalidFormatException("Unrecognized format picture: '" + format + "'");
        }
    }

    /**
     * Returns the field number. Every field has a number. The field may also have a name, but that
     * is optional.
     *
     * @see #getName()
     *
     * @return the field number
     */
    public int getNumber()
    {
        return properties.getNumber();
    }

    /**
     * Returns the optional name of the field, or <tt>null</tt> if the field has no name.
     *
     * @see #getNumber()
     *
     * @return the field's name or <tt>null</tt>
     */
    public String getName()
    {
        return properties.getName();
    }

    /**
     * Returns the type of the field.
     *
     * @return the type of the field
     * @see Type
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Returns the field's x-coordinate on the panel. This is a visual property.
     *
     * @return the x-coordinate
     */
    public int getX()
    {
        return properties.getX();
    }

    /**
     * Returns the field's y-coordate on the panel. This is a visual property.
     *
     * @return the y-coordinate
     */
    public int getY()
    {
        return properties.getY();
    }

    /**
     * Returns the field's format string. The format string contains a description of the format
     * constraints on the field in DataPerfect's own picture language.
     *
     * @return the format string
     */
    public String getFormat()
    {
        return properties.getFormat();
    }

    /**
     * Returns the help string or <tt>null</tt> if there is none.
     *
     * @return the help string or <tt>null</tt>
     */
    public String getHelp()
    {
        return properties.getHelp();
    }

    /**
     * Returns a field's link.
     *
     * @return the link
     */
    public Link getLink()
    {
        return properties.getLink();
    }

    /**
     * Returns the length of the data, including separators (if any) but
     * excluding format type and order indicators.
     *
     * @return the length of the data
     */
    public int getLength()
    {
        return formatter.getDataLength();
    }

    /*
     * Implementation properties are package private.
     */
    int getOffsetInRecord()
    {
        return properties.getOffsetInRecord();
    }

    int getNumberOfDigitsInFormat()
    {
        return numberOfDigitsInFormat;
    }

    Formatter getFormatter()
    {
        return formatter;
    }

    int getTypeDependentLengthInfo()
    {
        return properties.getTypeDependentLengthInfo();
    }

    // TODO: implement other properties
}
