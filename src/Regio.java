public class Regio implements Comparable<Regio> {
    private int _id;
    private boolean ocupada = false;

    public Regio(int _id) {
        this._id = _id;
    }

    @Override
    public int compareTo(Regio other){
        return new Integer(this.hashCode()).compareTo(new Integer(other.hashCode()));
    }

    @Override
    public int hashCode() {
        return _id;
    }
}
