
import java.util.Objects;

public class Regio implements Comparable<Regio> {
    final private int _id;
    final private char _codi;
    private Jugador _seguidor = null;
    final private boolean _escut = false;
    private Peça _peça;
    private Construccio _pertany = null;

    public Regio(int _id, Peça peça, char codi) {
        this._id = _id;
        this._peça = peça;
        this._codi = codi;
    }
    
    public Regio(int _id, boolean escut, Peça peça, char codi) {
        this._id = _id;
        this._peça = peça;
        this._codi = codi;
    }

    @Override
    public int compareTo(Regio other){
        return new Integer(this.hashCode()).compareTo(other.hashCode());
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Regio other = (Regio) obj;
        if (this._id != other._id) {
            return false;
        }
        if (this._escut != other._escut) {
            return false;
        }
        if (!Objects.equals(this._seguidor, other._seguidor)) {
            return false;
        }
        if (!Objects.equals(this._peça, other._peça)) {
            return false;
        }
        return true;
    }
    
    public void setSeguidor(Jugador seguidor){
        _seguidor = seguidor;
    }
    
    public boolean hiHaEscut(){
        return _escut;
    }
    
    public Peça get_peça(){
        return _peça;
    }
    
    public char get_codi(){
        return _codi;
    }
    
    public void set_pertany(Construccio c){
        _pertany = c;
    }
    
    public Construccio get_pertany(){
        return _pertany;
    }
}
