public class Regio implements Comparable<Regio> {
    private int _id;
    private String _seguidor = "";
    private boolean _escut = false;
    private Peça _peça;

    public Regio(int _id, Peça peça) {
        this._id = _id;
        this._peça = peça;
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
    
    public Peça get_peça(){
        return _peça;
    }
}
