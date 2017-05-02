package scan;

/**
 * Created by daiweifan on 2017-05-01.
 */
class CharsIDStart implements CharSet {
    public boolean contains(char newC) {
        return Character.isJavaIdentifierStart(newC);
    }
}
