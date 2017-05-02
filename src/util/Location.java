package util;

/**
 * Created by daiweifan on 2017-05-01.
 */
public class Location {
    private int line;
    private int column;
    private String filePath;

    public Location(int line, int column, String filePath) {
        this.line = line;
        this.column = column;
        this.filePath = filePath;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getFilePath() {
        return filePath;
    }
}
