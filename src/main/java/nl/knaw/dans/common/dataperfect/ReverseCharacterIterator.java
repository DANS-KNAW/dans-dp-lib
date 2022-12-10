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
