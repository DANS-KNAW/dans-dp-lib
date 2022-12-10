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

/**
 * Formats an integral number as specified by a DataPeferct number picture.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 */
final class NumberFormatter
    extends AbstractFormatter
{
    private final NumberPicture picture;

    NumberFormatter(final String picture)
    {
        super(picture);
        this.picture = new NumberPicture(strippedPicture);
    }

    public String format(final Object object)
    {
        if (! (object instanceof Integer || object instanceof Long))
        {
            throw new IllegalArgumentException("Object to format must be an integral number");
        }

        final long number = ((Number) object).longValue();

        if (Long.toString(Math.abs(number)).length() > picture.digitsInMask())
        {
            throw new IllegalArgumentException("Number of digits exceeds the mask size.");
        }

        final char[] result = new char[picture.getMaskSize()];

        /*
         * N-formats only consist of digits and literals.
         *
         * The difference between G and H formats only plays a role in DataPerfect reports. For the
         * purpose of this library they will be considered equal.
         */
        fillInDigits(number, result);

        if (picture.getType() == Type.G || picture.getType() == Type.H)
        {
            replacePlaceholderZerosWithSpacesOrAsterisks(result);
            fillInThousandsSeparators(result);
            fillInCurrencySymbol(result);
            fillInSign(number, result);
        }

        fillInLiterals(result);

        return new String(result);
    }

    /*
     * Fills the result array with the number digits one by one. If the number of digits in the
     * picture exceeds the number of number digits, the result is padded with zero characters in the
     * corresponding slots.
     */
    private void fillInDigits(final long number, final char[] result)
    {
        char[] digits = String.valueOf(Math.abs(number)).toCharArray();
        CharacterIterator maskIterator = picture.maskIterator();

        if (picture.getType() == Type.G || picture.getType() == Type.H)
        {
            maskIterator = new ReverseCharacterIterator(maskIterator);

            /*
             * Reverse the order of the digits to simulate the DataPerfect behavior of filling
             * G/H-type cells from the right.
             */
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(digits);
            stringBuilder = stringBuilder.reverse();
            digits = stringBuilder.toString().toCharArray();
        }

        int index = 0;

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (maskIterator.current() == '9' || maskIterator.current() == 'Z' || maskIterator.current() == '*')
            {
                if (index < digits.length)
                {
                    result[maskIterator.getIndex()] = digits[index];
                    index++;
                }
                else
                {
                    result[maskIterator.getIndex()] = '0';
                }
            }
        }
    }

    /*
     * Replaces leading zeros with spaces or asterisks if the picture value is 'Z' or '*'.
     */
    private void replacePlaceholderZerosWithSpacesOrAsterisks(final char[] result)
    {
        final CharacterIterator maskIterator = picture.maskIterator();

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (Character.isDigit(result[maskIterator.getIndex()]))
            {
                if (result[maskIterator.getIndex()] == '0')
                {
                    if (maskIterator.current() == 'Z')
                    {
                        result[maskIterator.getIndex()] = ' ';
                    }
                    else if (maskIterator.current() == '*')
                    {
                        result[maskIterator.getIndex()] = '*';
                    }
                }

                /*
                 * As they are preceded by a digit > 0 all following zeros in the result will be
                 * shown as zeros regardless their mask.
                 */
                else
                {
                    break;
                }
            }
        }
    }

    /*
     * Shows the thousands separator when it is followed by a digit with a '9' mask or when the
     * number part before the separator represents a value > 0.
     *
     *
     * For example:
     *
     * ZZ,999 will always display the thousands separator. ZZ,Z99 will display 111 as "   111" and
     * 1111 as " 1,111".
     *
     * In all other cases the separator is replaced by a space or an asterisk, depending on the mask
     * value, i.e. 'Z' or '*'.
     *
     * For example:
     *
     * **,Z99 will display 11 as "***111
     */
    private void fillInThousandsSeparators(final char[] result)
    {
        CharacterIterator maskIterator = picture.maskIterator();
        boolean hasThousand = false;

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (Character.isDigit(result[maskIterator.getIndex()]))
            {
                if (result[maskIterator.getIndex()] != '0')
                {
                    hasThousand = true;

                    break;
                }
            }
            else if (maskIterator.current() == '.' || maskIterator.current() == ',')
            {
                break;
            }
        }

        maskIterator = picture.maskIterator();

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (maskIterator.current() == '.' || maskIterator.current() == ',')
            {
                if (hasThousand == true)
                {
                    result[maskIterator.getIndex()] = maskIterator.current();
                }
                else if (maskIterator.next() == '9')
                {
                    maskIterator.previous();
                    result[maskIterator.getIndex()] = maskIterator.current();
                }
                else
                {
                    maskIterator.previous();

                    if (maskIterator.next() == '*')
                    {
                        maskIterator.previous();
                        result[maskIterator.getIndex()] = '*';
                    }
                    else
                    {
                        maskIterator.previous();
                        result[maskIterator.getIndex()] = ' ';
                    }
                }
            }
        }
    }

    /*
     * Adds a currency symbol to the result.
     */
    private void fillInCurrencySymbol(final char[] result)
    {
        final CharacterIterator maskIterator = picture.maskIterator();

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (maskIterator.current() == '$')
            {
                result[maskIterator.getIndex()] = maskIterator.current();
            }
        }

        shiftSignOrCurrencySymbol(result);
    }

    /*
     * Adds a sign to the result.
     */
    private void fillInSign(final long number, final char[] result)
    {
        final CharacterIterator maskIterator = picture.maskIterator();

        for (char maskCharacter = maskIterator.last(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.previous())
        {
            switch (maskIterator.current())
            {
                case '-':

                    if (number < 0)
                    {
                        result[maskIterator.getIndex()] = '-';
                    }
                    else
                    {
                        result[maskIterator.getIndex()] = ' ';
                    }

                    break;

                case '+':

                    if (number < 0)
                    {
                        result[maskIterator.getIndex()] = '-';
                    }
                    else
                    {
                        result[maskIterator.getIndex()] = '+';
                    }

                    break;

                case ')':

                    if (number < 0)
                    {
                        result[maskIterator.getIndex()] = ')';
                    }
                    else
                    {
                        result[maskIterator.getIndex()] = ' ';
                    }

                    break;

                case '(':

                    if (number < 0)
                    {
                        result[maskIterator.getIndex()] = '(';
                    }
                    else
                    {
                        result[maskIterator.getIndex()] = ' ';
                    }
            }
        }

        shiftSignOrCurrencySymbol(result);
    }

    /*
     * Completes the result with the remaining picture characters which are all considered literals.
     */
    private void fillInLiterals(final char[] result)
    {
        final CharacterIterator maskIterator = picture.maskIterator();

        for (char maskCharacter = maskIterator.first(); maskCharacter != CharacterIterator.DONE;
                 maskCharacter = maskIterator.next())
        {
            if (result[maskIterator.getIndex()] == '\0')
            {
                result[maskIterator.getIndex()] = maskIterator.current();
            }
        }
    }

    /*
     * Shifts sign towards the next digit or literal if there are spaces in between.
     */
    private void shiftSignOrCurrencySymbol(final char[] result)
    {
        int i = 0;
        int spaceCounter = 0;

        for (i = 0; i < result.length; i++)
        {
            if (result[i] == '-' || result[i] == '+' || result[i] == '$')
            {
                for (int j = i + 1; j < result.length; j++)
                {
                    if (result[j] == ' ')
                    {
                        spaceCounter += 1;
                    }
                    else
                    {
                        break;
                    }
                }

                break;
            }
        }

        if (spaceCounter > 0)
        {
            result[i + spaceCounter] = result[i];
            result[i] = ' ';
        }
    }

    public int getDataLength()
    {
        return picture.getDataLength();
    }
}
