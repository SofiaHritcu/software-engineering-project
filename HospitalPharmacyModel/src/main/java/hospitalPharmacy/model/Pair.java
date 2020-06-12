package hospitalPharmacy.model;

import java.io.Serializable;
import java.util.Objects;


public class Pair<A, B> implements Serializable {
    public final A fst;
    public final B snd;

    public Pair(A fst, B snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public String toString() {
        return "Pair[" + this.fst + "," + this.snd + "]";
    }

    public boolean equals(Object other) {
        return other instanceof Pair && Objects.equals(this.fst, ((Pair)other).fst) && Objects.equals(this.snd, ((Pair)other).snd);
    }

    public int hashCode() {
        if (this.fst == null) {
            return this.snd == null ? 0 : this.snd.hashCode() + 1;
        } else {
            return this.snd == null ? this.fst.hashCode() + 2 : this.fst.hashCode() * 17 + this.snd.hashCode();
        }
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair(a, b);
    }
}
