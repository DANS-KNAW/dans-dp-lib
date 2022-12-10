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

import java.nio.charset.Charset;

/**
 * Mock {@link DatabaseSettings} implementation.
 *
 * @author Jan van Mansum
 */
final class DefaultTestDatabaseSettings
    implements DatabaseSettings
{
    static final DefaultTestDatabaseSettings INSTANCE = new DefaultTestDatabaseSettings();

    private DefaultTestDatabaseSettings()
    {
        /*
         * Don't instantiate yourself, just use the INSTANCE singleton.
         */
    }

    public String getDefaultDateOrder()
    {
        return null;
    }

    public String getDefaultTimeOrder()
    {
        return null;
    }

    public String getExtAsciiCodeEndDelimiter()
    {
        return null;
    }

    public String getExtAsciiCodeStartDelimiter()
    {
        return null;
    }

    public void setCharset(final Charset charset)
    {
    }

    public void setExtAsciiCodeDelimiters(final String start, final String end)
    {
    }

    public String getCharsetName()
    {
        return null;
    }
}