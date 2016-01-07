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

import java.util.Calendar;

/**
 * Formats the numeric representation of a date used in the panel file (i.e. the number of days
 * since 1 March 1900) as a <tt>String</tt> value using the picture provided in the constructor.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 */
final class DateFormatter
    extends OrderedFieldFormatter
{
    /**
     * Creates a <tt>DateFormatter</tt> object. The picture is a format string for the date.
     * DataPerfect does not seem to do a whole lot of validation on the picture so DANS DataPerfect
     * Library does not do that, either. The following are the assumptions about what constitutes a
     * valid format and what type of formatting it results in.
     * <p>
     * The date picture consists of:
     * <ol>
     * <li>a capital letter D
     * <li>optionally the three letters: D, M and Y in any order (default: "MDY")
     * <li>one ore more groups of two or more 9 characters, separated by by non-9 characters
     * </ol>
     * The first D indicates that the type of data formatted is a date. The D, M and Y specify the
     * order of the subsequent day month and year fields. The groups of 9's indicate where the
     * numbers go.
     * <p>
     * Examples. The following examples use the date 10 December 1948.
     * <table border="1">
     * <tr>
     * <th align="left">Format</th>
     * <th align="left">Result</th>
     * </tr>
     * <tr>
     * <td>D99/99/99</td>
     * <td>12/10/48</td>
     * </tr>
     * <tr>
     * <td>DDMY99-99-99</td>
     * <td>10-12-48</td>
     * </tr>
     * <tr>
     * <td>DYMD9999/99</td>
     * <td>1948/12</td>
     * </tr>
     * <tr>
     * <td>DYMD9999/99/99</td>
     * <td>1948/12/10</td>
     * </tr>
     * </table>
     *
     * An excess of 9's will result in the number in case being padded from the left with zero's. A
     * shortage of 9's will result in the number being truncated from the left.
     *
     * @param picture the picture to use when formatting a numeric date representation
     * @param defaultDateOrder the date order to use if none was provided in the picture
     */
    public DateFormatter(final String picture, final String defaultDateOrder)
    {
        super("(D)", "([DMY]{0,3})", "([^9]+)", "(9+)", picture, defaultDateOrder);
    }

    protected void setFields(final Object object, final int[] fields)
    {
        final Number daysSince1March1900 = (Number) object;

        final Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 2, 1);
        calendar.add(Calendar.DAY_OF_MONTH,
                     daysSince1March1900.intValue());

        setField(fields,
                 'Y',
                 calendar.get(Calendar.YEAR));
        setField(fields, 'M', calendar.get(Calendar.MONTH) + 1);
        setField(fields,
                 'D',
                 calendar.get(Calendar.DAY_OF_MONTH));
    }
}
