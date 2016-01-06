package nl.knaw.dans.common.dataperfect;

import java.util.HashMap;
import java.util.Map;

class NonPrintableCharacterMap
{
    private static final Map<Character, Character> MAP = new HashMap<Character, Character>();

    private NonPrintableCharacterMap()
    {
        /*
         * Disallow instantiation.
         */
    }

    static
    {
        MAP.put('\u0001', '\u263a'); // ☺ (WHITE SMILING FACE)
        MAP.put('\u0002', '\u263b'); // ☻ (BLACK SMILING FACE)
        MAP.put('\u0003', '\u2665'); // ♥ (BLACK HEART SUIT)
        MAP.put('\u0004', '\u2666'); // ♦ (BLACK DIAMOND SUIT)
        MAP.put('\u0005', '\u2663'); // ♣ (BLACK CLUB SUIT)
        MAP.put('\u0006', '\u2660'); // ♠ (BLACK SPADE SUIT)
        MAP.put('\u0007', '\u2022'); // • (BULLET)
        MAP.put('\u0008', '\u25d8'); // ◘ (INVERSE BULLET)
        MAP.put('\u0009', '\u25cb'); // ○ (WHITE CIRCLE)
        MAP.put('\r', '\u25d9'); // ◙ (INVERSE WHITE CIRCLE)
        MAP.put('\u000b', '\u2642'); // ♂ (MALE SIGN)
        MAP.put('\u000c', '\u2640'); // ♀ (FEMALE SIGN)
        MAP.put('\n', '\u266a'); // ♪ (EIGHTH NOTE)
        MAP.put('\u000e', '\u266b'); // ♫ (BEAMED EIGHTH NOTES)
        MAP.put('\u000f', '\u263c'); // ☼ (WHITE SUN WITH RAYS)
        MAP.put('\u0010', '\u25ba'); // ► (BLACK RIGHT-POINTING POINTER)
        MAP.put('\u0011', '\u25c4'); // ◄ (BLACK LEFT-POINTING POINTER)
        MAP.put('\u0012', '\u2195'); // ↕ (UP DOWN ARROW)
        MAP.put('\u0013', '\u203c'); // ‼ (DOUBLE EXCLAMATION MARK)
        MAP.put('\u0014', '\u00b6'); // ¶ (PILCROW SIGN)
        MAP.put('\u0015', '\u00a7'); // § (SECTION SIGN)
        MAP.put('\u0016', '\u25ac'); // ▬ (BLACK RECTANGLE)
        MAP.put('\u0017', '\u21a8'); // ↨ (UP DOWN ARROW WITH BASE)
        MAP.put('\u0018', '\u2191'); // ↑ (UPWARDS ARROW)
        MAP.put('\u0019', '\u2193'); // ↓ (DOWNWARDSMAP.put( ARROW)
        MAP.put('\u001a', '\u2192'); // → (RIGHTWARDS ARROW)
        MAP.put('\u001b', '\u2190'); // ← (LEFTWARDS ARROW)
        MAP.put('\u001c', '\u221f'); // ∟ (RIGHT ANGLE)
        MAP.put('\u001d', '\u2194'); // ↔ (LEFT RIGHT ARROW)
        MAP.put('\u001e', '\u25b2'); // ▲ (BLACK UP-POINTING TRIANGLE)
        MAP.put('\u001f', '\u25bc'); // ▼ (BLACK DOWN-POINTING TRIANGLE)
        MAP.put('\u007f', '\u2302'); // ⌂ (HOUSE)
    }

    static Character get(char nonPrintableChar)
    {
        return MAP.get(nonPrintableChar);
    }
}
