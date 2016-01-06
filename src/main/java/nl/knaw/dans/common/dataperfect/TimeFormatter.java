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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Formats number representing the number of seconds since midnight into a string representing that
 * time.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 */
final class TimeFormatter
    extends OrderedFieldFormatter
{
    /**
     * Constructs a <tt>TimeFormatter</tt> that uses <tt>aPicture</tt> to create a string
     * representation of a given time value.
     *
     * @param picture a DataPerfect time format picture
     *
     *            <p>
     *            The time picture consists of:
     *            <ol>
     *            <li>a capital letter T
     *            <li>optionally the three letters: H, M and S in any order (default: "HMS")
     *            <li>one ore more groups of two 9 characters, separated by a colon
     *            </ol>
     *            The T indicates that the type of data formatted is a time. The H, M and S specify
     *            the order of the subsequent hour minute and second fields. The groups of 9's
     *            indicate where the numbers go.
     *            <p>
     *            Examples. The following examples use the time 11:59:59.
     *            <table border="1">
     *            <tr>
     *            <th align="left">Format</th>
     *            <th align="left">Result</th>
     *            </tr>
     *            <tr>
     *            <td>THMS99:99:99</td>
     *            <td>11:59:59</td>
     *            </tr>
     *            <tr>
     *            <td>THM99:99</td>
     *            <td>11:59</td>
     *            </tr>
     *            <tr>
     *            <td>TMS99:99</td>
     *            <td>59:59</td>
     *            </tr>
     *            </table>
     *
     * @param defaultTimeOrder the default time order
     */
    TimeFormatter(final String picture, final String defaultTimeOrder)
    {
        super("(T)", "([HMS]{0,3})", "(:)", "([Z9]+)", picture, defaultTimeOrder);
    }

    void setFields(final Object object, final int[] fields)
    {
        final Number secondsSinceMidnight = (Number) object;
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(secondsSinceMidnight.longValue() * 1000);

        setField(fields,
                 'H',
                 calendar.get(Calendar.HOUR));
        setField(fields,
                 'M',
                 calendar.get(Calendar.MINUTE));
        setField(fields,
                 'S',
                 calendar.get(Calendar.SECOND));
    }
}
