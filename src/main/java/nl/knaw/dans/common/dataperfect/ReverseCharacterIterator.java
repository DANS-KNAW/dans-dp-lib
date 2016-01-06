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

import java.text.CharacterIterator;

/**
 * Decorates CharacterIterator to reverse the direction of iteration.
 *
 * @author Martin Braaksma
 */
final class ReverseCharacterIterator
    implements CharacterIterator
{
    final CharacterIterator maskIterator;

    ReverseCharacterIterator(final CharacterIterator maskIterator)
    {
        this.maskIterator = maskIterator;
    }

    public char current()
    {
        return maskIterator.current();
    }

    public char first()
    {
        return maskIterator.last();
    }

    public int getBeginIndex()
    {
        return maskIterator.getEndIndex();
    }

    public int getEndIndex()
    {
        return maskIterator.getBeginIndex();
    }

    public int getIndex()
    {
        return maskIterator.getIndex();
    }

    public char last()
    {
        return maskIterator.first();
    }

    public char next()
    {
        return maskIterator.previous();
    }

    public char previous()
    {
        return maskIterator.next();
    }

    public char setIndex(int position)
    {
        return maskIterator.setIndex(position);
    }

    /*
     * Not used but necessary to avoid compiler error.
     */
    public Object clone()
    {
        return null;
    }
}
