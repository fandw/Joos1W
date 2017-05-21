package grammar;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class RHSElement {
    public final boolean optional;
    public String rhs;
    public boolean print;

    public RHSElement(String rhs) {
        this.rhs = rhs;
        this.optional = false;
    }

    public RHSElement(String rhs, boolean optional) {
        this.rhs = rhs;
        this.optional = optional;
    }

    public boolean equals(String t) {
        return rhs.equals(t);
    }

    public String get_RHS() {
        return rhs;
    }

    public void set_RHS(String rhs) {
        this.rhs = rhs;
    }
}
