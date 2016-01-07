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
            case AU:
            case UA:
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

                if (format.substring(1).contains("U"))
                {
                    return Type.AU;
                }

                return Type.A;

            case 'U':

                if (format.substring(1).contains("U"))
                {
                    return Type.UU;
                }

                if (format.substring(1).contains("A"))
                {
                    return Type.UA;
                }

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
     * Returns the field's y-coordinate on the panel. This is a visual property.
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
     * Returns a boolean indicating whether the field is a computed field.
     *
     * @return the boolean
     */
    public boolean isComputedField()
    {
        if (this.getFormat() != null && this.getFormat().contains("::C")) // NOPMD
        {
            return true;
        }

        return false;
    }

    /**
     * @return the initialization value
     */
    public String getInitialization()
    {
        return properties.getInitialization();
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
