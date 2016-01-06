/**
 * Copyright (C) 2009 DANS (info@dans.knaw.nl)
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
