package scan;

/**
 * Created by daiweifan on 2017-05-01.
 */
class Chars implements CharSet {
    private String chars;

    Chars(String chars) {
        this.chars = chars;
    }

    public boolean contains(char newC) {
        return chars.indexOf(newC) >= 0;
    }
}
