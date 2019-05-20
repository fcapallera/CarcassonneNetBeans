/** @file Regio.java
    @brief Classe Regio
*/
import java.util.Objects;

/** @class Regio
    @brief Classe que representa una Regio d'una Peça
    @author 
*/
public class Regio implements Comparable<Regio> {
    final private int _id;///< Id de la Regio
    final private char _codi;///< Codi(Lletra) de la Regio
    private Jugador _seguidor = null;///< Jugador al que pertany el seguidor de la Regio
    private boolean _escut = false;///< Cert si la Regio té escut
    private Peça _peça;///< Peça a la qual pertany la Regio
    private Construccio _pertany = null;///< Regio a la qual pertany la Regio
    
    /** @brief Constructor de Regio
	@pre --
	@post Inicialitza la Regio amb els valors passats per paràmetres */
    public Regio(int _id, Peça peça, char codi) {
        this._id = _id;
        this._peça = peça;
        this._codi = codi;
    }
    
    /** @brief Constructor de Regio
	@pre --
	@post Inicialitza la Regio amb els valors passats per paràmetres */
    public Regio(int _id, boolean escut, Peça peça, char codi) {
        this._id = _id;
        this._peça = peça;
        this._codi = codi;
        _escut = escut;
    }
    
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        
        if(!(other instanceof Regio)) return false;
        
        Regio r = (Regio)other;
        
        return this._id == r.get_id();
    }
    
    /** @brief Es sobreescriu el mètode compareTo que ens compara els codis de les Regions
	@pre 
	@post  */
    @Override
    public int compareTo(Regio other){
        return new Integer(this.hashCode()).compareTo(other.hashCode());
    }
    
    /** @brief Retorna la Id de la Regio
	@pre --
	@post retorna _id */
    @Override
    public int hashCode() {
        return _id;
    }
    
    
    /** @brief Retorna la Id de la Regio
	@pre --
	@post retorna _id */
    public int get_id() {
        return _id;
    }
    
    
    /** @brief Canvia el jugador propietari del seguidor de la Regio
	@pre --
	@post Set _seguidor = seguidor */
    public void setSeguidor(Jugador seguidor){
        _seguidor = seguidor;
        _pertany.addSeguidor(seguidor);
    }
    
    /** @brief Esborra el seguidor de la Regio
	@pre --
	@post _seguidor = null */
    public void esborrarSeguidor(){
        _seguidor = null;
    }
    
    /** @brief Ens diu si a la Regio hi ha escut
	@pre --
	@post Retorna _escut */
    public boolean hiHaEscut(){
        return _escut;
    }
    
    /** @brief Ens diu si hi ha seguidor a la Regio
	@pre --
	@post Retorna _seguidor != null */
    public boolean hiHaSeguidor(){
        return _seguidor != null;
    }
    
    /** @brief Retorna la Peça a la qual pertany la Regio
	@pre --
	@post Retorna _peça */
    public Peça get_peça(){
        return _peça;
    }
    
    /** @brief Retorna el codi de la Regio
	@pre --
	@post Retorna _codi */
    public char get_codi(){
        return _codi;
    }
    
    /** @brief Canvia la construcció a la qual pertany la Regio
	@pre --
	@post Set _pertany = c */
    public void set_pertany(Construccio c){
        _pertany = c;
    }
    
    /** @brief Retorna la construccio a la qual pertany la Regio
	@pre --
	@post Retorna _pertany */
    public Construccio get_pertany(){
        return _pertany;
    }
    
    /** @brief Retorna el Jugador propietari del seguidor de la Regio
	@pre --
	@post Retorna _seguidor. Retorna null si no hi ha seguidor a la Regio */
    public Jugador getJugador(){
        return _seguidor;
    }
}
