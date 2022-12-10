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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a number format picture. This class checks that the picture adheres to the syntax for
 * number format pictures and provides an iterator to iterate over the mask characters. The mask
 * characters in the picture are those that actually represent the position of a resulting
 * character.
 *
 * @author Martin Braaksma
 */
class NumberPicture
{
    private static final String FIELD_TYPE = "([NGH])";
    private static final String DECIMAL_THOUSANDS_SEPARATORS = "(\\.,|,\\.)?";
    private static final String CURRENCY_SYMBOL = "(\\p{Sc})?";
    private static final String SIGN = "([-+()]?)";
    private static final String LITERAL = "(.*)";
    private static final String NUMBER = "((?:[Z*9]+)(?:[.,][Z*9]+)*(?:[.,][Z*9]+)?)";
    private static final String REGEX =
        FIELD_TYPE + DECIMAL_THOUSANDS_SEPARATORS + LITERAL + SIGN + CURRENCY_SYMBOL + NUMBER + SIGN + LITERAL;
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final int DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP = 2;
    private final String numberPicture;
    private Matcher matcher = null;
    private Type type = null;
    private String decimalThousandsSeparatorsFormat = null;
    private String mask = null;
    private int dataLength;

    NumberPicture(final String numberPicture)
    {
        this.numberPicture = numberPicture;
        matcher = PATTERN.matcher(numberPicture);
        checkPicture();
        analyzePicture();
    }

    private void checkPicture()
    {
        if (! matcher.matches())
        {
            throw new IllegalArgumentException("Picture does not match expected pattern: " + numberPicture);
        }
    }

    private void analyzePicture()
    {
        setType(numberPicture.charAt(0));
        setDecimalThousandsSeparatorsSpecifier();
        setMask();
        setDataLength();
    }

    private void setDataLength()
    {
        /*
         * Subtract 1 character that identifies the field type
         */
        dataLength = numberPicture.length() - 1;

        if (hasDecimalThousandsSeparators())
        {
            dataLength -= matcher.group(DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP).length();
        }
    }

    private boolean hasDecimalThousandsSeparators()
    {
        return matcher.group(DECIMAL_THOUSANDS_SEPARATOR_SUBGROUP) != null;
    }

    /*
     * A picture starts with a mandatory type indicator N, G, or H.
     */
    private void setType(final char type)
    {
        switch (type)
        {
            case 'N':
                this.type = Type.N;

                break;

            case 'G':
                this.type = Type.G;

                break;

            case 'H':
                this.type = Type.H;
        }
    }

    /*
     * A type indicator is followed by an optional set of decimal and thousands separators, i.e.
     * '.,' or ',.'.
     */
    private void setDecimalThousandsSeparatorsSpecifier()
    {
        if ((type == Type.G || type == Type.H)
                && (numberPicture.substring(1).startsWith(".,") || numberPicture.substring(1).startsWith(",.")))
        {
            decimalThousandsSeparatorsFormat = numberPicture.substring(1, 3);
        }
    }

    /*
     * The mask equals the picture with its type and separator indicators stripped.
     */
    private void setMask()
    {
        if (decimalThousandsSeparatorsFormat == null)
        {
            mask = numberPicture.substring(1);
        }
        else
        {
            mask = numberPicture.substring(3);
        }
    }

    CharacterIterator maskIterator()
    {
        return new StringCharacterIterator(mask);
    }

    int getMaskSize()
    {
        return mask.length();
    }

    Type getType()
    {
        return type;
    }

    int digitsInMask()
    {
        int digitCounter = 0;

        for (int i = 0; i < mask.length(); ++i)
        {
            if (mask.charAt(i) == '9' || mask.charAt(i) == 'Z' || mask.charAt(i) == '*')
            {
                ++digitCounter;
            }
        }

        return digitCounter;
    }

    public int getDataLength()
    {
        return dataLength;
    }
}
