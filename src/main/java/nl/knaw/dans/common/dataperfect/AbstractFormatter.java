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


/**
 * Base class for formatters. Truncates mode indicators from the picture.
 *
 * @author Jan van Mansum
 * @author Martin Braaksma
 *
 */
abstract class AbstractFormatter
    implements Formatter
{
    protected final String strippedPicture;

    AbstractFormatter(final String picture)
    {
        assert picture != null : "Picture must not be null";

        String regex = "::|;;";
        String[] splitPicture = picture.split(regex);
        strippedPicture = splitPicture[0];
    }
}
