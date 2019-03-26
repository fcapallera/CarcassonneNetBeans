public class Regio implements Comparable<Regio> {
    private int _id;
    private String _seguidor = "";
    private boolean _escut = false;

    public Regio(int _id) {
        this._id = _id;
    }
    
    public Regio(int _id, boolean escut) {
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
    
    public void setSeguidor(String seguidor){
        _seguidor = seguidor;
    }
    
    public boolean hiHaEscut(){
        return _escut;
    }
}
