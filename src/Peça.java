/** @file Peça.java
    @brief Classe Peça
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** @class Peça
    @brief Classe que representa una Peça del Tauler
    @author 
*/
public class Peça implements Comparable<Peça>{
    int _x;///< Coordenada X
    int _y;///< Coordenada Y
    private String _codi;///< Codi de la peça (x*100+1)
    private int _nRotacions = 0;///< Nombre de rotacions que se li ha donat a la peça en el moment de col·locar-la
    private List<Peça> _adjacents = new ArrayList<>(Arrays.asList(null,null,null,null));///< Llista de Peces adjacents
    private List<Regio> _regions = new ArrayList<>(Arrays.asList(null,null,null,null,null));///< Llista de Regions de la Peça
    
    /** @brief Es sobreescriu el mètode compareTo que ens compara el hashCode de les Peces
	@pre 
	@post  */
    @Override
    public int compareTo(Peça other){
        return new Integer(this.hashCode()).compareTo(other.hashCode());
    }
    
    /** @brief Ens retorna el hashCode de la Peça que s'obté a partir de la x i la y
	@pre 
	@post  */
    @Override
    public int hashCode() {
        return _x*100+_y;
    }

    /** @brief 
	@pre 
	@post  */
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
        final Peça other = (Peça) obj;
        if (this._x != other._x) {
            return false;
        }
        if (this._y != other._y) {
            return false;
        }
        return Objects.equals(this._codi, other._codi);
    }

    /** @brief 
	@pre 
	@post  */
    public Peça(String _codi) {
        this._codi = _codi;
    }
    
    /** @brief 
	@pre 
	@post  */
    public Peça(int _x, int _y, String _codi) {
        this._x = _x;
        this._y = _y;
        this._codi = _codi;
    }
    
    /** @brief Aplica una rotació de -90 graus a la Peça, canviant les seves Regions
	@pre --
	@post _nRotacions++ i regions canviades girant 90º la peça */
    public void rotarClockWise(){
        //String nouCodi = ""+_codi.charAt(0)+_codi.charAt(4)+_codi.charAt(1)+_codi.charAt(2)+_codi.charAt(3);
        //_codi = nouCodi;
        List<Regio> transf = new ArrayList<>();
        transf.add(_regions.get(0)); transf.add(_regions.get(4)); 
        transf.add(_regions.get(1)); transf.add(_regions.get(2));
        transf.add(_regions.get(3));
        _regions = transf;
        _nRotacions = (_nRotacions+1)%4;
    }
    
    
    public void rotarFins(int pos){
        while(_nRotacions != pos) rotarClockWise();
    }
    
    /** @brief Ens mira si la peça actual és compatible, en l'orientació passada per paràmetres, amb la Peça també passada per paràmetres
	@pre --
	@post Retorna cert si l'orientació de la Peça actual i l'orientació oposada de la Peça passada per peràmetres són iguals */
    public boolean esCompatible(Peça peça, int orientacio){
        return _regions.get(orientacio+1).get_codi() == peça.getRegio((orientacio+2)%4).get_codi();
    }
    
    /** @brief Retorna la Peça adjacent a la Peça actual per el costat "a" {NESW=1234}
	@pre --
	@post Retorna la Peça _adjacent[a] */
    public Peça getPeçaAdjacent(int a){
        if(a < 0 || a > 4) throw new IndexOutOfBoundsException("Index "+a+" is out of bounds");
        else return _adjacents.get(a);
    }
    
    /** @brief Retorna la Regió de la Peça actual amb índex a {CNESW=01234}
	@pre --
	@post Retorna la Regio _regions[a] */
    public Regio getRegio(int r){
        if(r < -1 || r > 4) throw new IndexOutOfBoundsException("Index "+r+" is out of bounds");
        else return _regions.get(r+1);
    }
    
    /** @brief Afegeix un seguidor a la Regió indicada de la Peça, indicant quin Jugador l'ha inserit
	@pre --
	@post S'ha inserit el Jugador al vector de Seguidors de la Regio _regions[i] de la Peça actual */
    public void afegirSeguidor(int i, Jugador seguidor){
        _regions.get(i).setSeguidor(seguidor);
    }
    
    /** @brief 
	@pre 
	@post  */
    public int afegirRegions(int nReg){
        int n = 0;
        char c = centre();
        if(c == 'V' || c == 'E'){
            Regio v = new Regio(nReg+n,c=='E',this,'V');
            _regions.set(0, v);
            n++;
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V') _regions.set(i, v);
            }
        }
        else{
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V'){
                    Regio v = new Regio(nReg+n,this,'V');
                    n++;
                    _regions.set(i, v);
                }
            }
        }
        if(c == 'X'){
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='C'){
                    Regio p = new Regio(nReg+n,this,'C');
                    n++;
                    _regions.set(i, p);
                }
            }
        }
        else{
            if(_codi.indexOf('C')>(-1)){
                Regio p = new Regio(nReg+n,this,'C');
                if(c=='C') _regions.set(0,p);
                n++;
                for(int i=1;i<5;i++){
                    if(_codi.charAt(i)=='C') _regions.set(i, p);
                }
            }
        }
        if(c == 'F'){
            Regio f = new Regio(nReg+n,this,'F');
            _regions.set(0, f);
            n++;
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='F') _regions.set(i, f);
            }
        }
        else{
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='F'){
                    Regio f = new Regio(nReg+n,this,'F');
                    n++;
                    _regions.set(i, f);
                }
            }
        }
        if(c == 'M'){
            Regio m = new Regio(nReg+n,this,'M');
            n++;
            _regions.set(0, m);
        }
        return n;
    }
    
    /** @brief Afegeix una Peça al vector de Peces adjacents pel costat indicat
	@pre --
	@post S'ha afegit la Peça a _adjacents[orientacio] */
    public void setPeçaAdjacent(Peça peça, int orientacio){
        _adjacents.set(orientacio,peça);
    }
    
    
    /** @brief Mètode per comprovar si a la ciutat hi ha escut
	@pre --
	@post Retorna cert si a la Peça hi ha un escut */
    public boolean hiHaEscut(){
        return _codi.indexOf('E') > 0;
    }
    
    
    /** @brief Retorna el char de la Regio CENTRE
	@pre --
	@post Retorna el char de la Regio centre */
    public char centre(){ return _codi.charAt(0);}
    
    /** @brief Retorna una llista de Peces adjacents a la Peça actual
	@pre --
	@post Retorna una llista de Peces adjacents a la Peça actual */
    public List<Peça> get_adjacents() {
        return _adjacents;
    }
    
    /** @brief Retorna una llista de les Regions de la Peça 
	@pre --
	@post Retorna una llista de les Regions de la Peça */
    public List<Regio> get_regions(){
        return _regions;
    }
    
    /** @brief La llista d'adjacents passa a ser la llista de Peces passades per paràmetres
	@pre --
	@post S'ha afegit la llista de Peces passades per paràmetres a la llista _adjacents[] */
    public void set_adjacents(List<Peça> _adjacents) {
        this._adjacents = _adjacents;
    }

    
    /** @brief Retorna el codi de la Peça
	@pre --
	@post Retorna el codi de la Peça */
    public String get_codi() {
        return _codi;
    }
    
    /** @brief El codi passa a ser el codi passat per paràmetres
	@pre --
	@post El codi passat per paràmetres és el codi de la Peça */
    public void set_codi(String _codi) {
        this._codi = _codi;
    }
    
    /** @brief Retorna la coordenada X de la Peça
	@pre --
	@post Retorna _x */
    public int get_x() {
        return _x;
    }
    
    
    /** @brief Canvia la coordenada X de la Peça
	@pre --
	@post La x passada per paràmetres és la _x de la Peça */
    public void set_x(int _x) {
        this._x = _x;
    }
    
    /** @brief Retorna la coordenada Y de la Peça
	@pre --
	@post Retorna _y */
    public int get_y() {
        return _y;
    }
    
    /** @brief Canvia la coordenada Y de la Peça 
	@pre --
	@post La x passada per paràmetres es la _y de la Peça */
    public void set_y(int _y) {
        this._y = _y;
    }
    
    /** @brief Retorna l'index de rotacio de la Peça
	@pre --
	@post Retorna _nRotacions%4 */
    public int getIndexRotacio(){
        return _nRotacions;
    }
    
}
