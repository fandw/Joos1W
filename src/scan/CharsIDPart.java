package scan;

/**
 * Created by daiweifan on 2017-05-01.
 */
class CharsIDPart implements CharSet {
    public boolean contains(char newC) {
        return Character.isJavaIdentifierPart(newC);
    }
}
