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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class providing methods common to the DateFormatter and TimeFormatter classes.
 *
 * @author Martin Braaksma
 */
abstract class OrderedFieldFormatter
    extends AbstractFormatter
{
    private static final String MODE_INDICATORS = "((?:::|;;).*)";
    private final Pattern patternPicture;
    private final String indicatorSubgroup;
    private final String[] separators = new String[4];
    private final int[] fieldLengths = new int[3];
    private final int[] fields = new int[3];
    private String fieldOrder;
    private int dataLength;

    public OrderedFieldFormatter(final String indicatorSubgroup, final String fieldOrderSubgroup,
                                 final String separatorSubgroup, final String numberSubgroup, final String picture,
                                 final String defaultFieldOrder)
    {
        super(picture);

        this.indicatorSubgroup = indicatorSubgroup;

        patternPicture = Pattern.compile(indicatorSubgroup + fieldOrderSubgroup + separatorSubgroup + "?"
                                         + numberSubgroup + "?" + separatorSubgroup + "?" + numberSubgroup + "?"
                                         + separatorSubgroup + "?" + numberSubgroup + "?" + MODE_INDICATORS + "?");

        getPictureValues(strippedPicture, defaultFieldOrder);
    }

    abstract void setFields(final Object object, final int[] fields);

    private void getPictureValues(final String picture, final String defaultFieldOrder)
    {
        final Matcher pictureMatcher = patternPicture.matcher(picture);

        if (! pictureMatcher.matches())
        {
            throw new IllegalArgumentException("Picture does not match expected pattern for type " + indicatorSubgroup
                                               + ": " + picture);
        }

        fieldOrder = pictureMatcher.group(2);

        if ("".equals(fieldOrder))
        {
            fieldOrder = defaultFieldOrder;

            /*
             * Subtract 1 character that identifies the field type
             */
            dataLength = picture.length() - 1;
        }
        else
        {
            dataLength = picture.length() - 1 - fieldOrder.length();
        }

        separators[0] = pictureMatcher.group(3);
        fieldLengths[0] = getFieldLength(pictureMatcher.group(4));
        separators[1] = pictureMatcher.group(5);
        fieldLengths[1] = getFieldLength(pictureMatcher.group(6));
        separators[2] = pictureMatcher.group(7);
        fieldLengths[2] = getFieldLength(pictureMatcher.group(8));
    }

    private static int getFieldLength(final String picturePart)
    {
        if (picturePart == null)
        {
            return -1;
        }

        return picturePart.length();
    }

    protected void setField(final int[] fields, final char fieldLetter, final int fieldValue)
    {
        int index = fieldOrder.indexOf(fieldLetter);

        if (index != -1)
        {
            fields[index] = fieldValue;
        }
    }

    private static String formatField(int fieldLength, int fieldValue)
    {
        if (fieldLength == 0)
        {
            return "";
        }

        String result = String.format("%0" + fieldLength + "d", fieldValue);

        return result.substring(result.length() - fieldLength);
    }

    public String format(final Object object)
    {
        if (! (object instanceof Number))
        {
            throw new IllegalArgumentException("Object to format must be a java.lang.Number");
        }

        setFields(object, fields);

        final StringBuilder buffer = new StringBuilder();
        buffer.append(separators[0] == null ? "" : separators[0]);
        buffer.append(formatField(fieldLengths[0], fields[0]));
        buffer.append(separators[1] == null ? "" : separators[1]);
        buffer.append(fieldLengths[1] == -1 ? "" : formatField(fieldLengths[1], fields[1]));
        buffer.append(separators[2] == null ? "" : separators[2]);
        buffer.append(fieldLengths[2] == -1 ? "" : formatField(fieldLengths[2], fields[2]));

        return buffer.toString();
    }

    public int getDataLength()
    {
        return dataLength;
    }
}
