import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Peça implements Comparable<Peça>{
    int _x;
    int _y;
    private String _codi;
    private int _nRotacions = 0;
    private List<Peça> _adjacents = new ArrayList<>(Arrays.asList(null,null,null,null));
    final private List<Regio> _regions = new ArrayList<>(Arrays.asList(null,null,null,null));
    final private Map<String,ArrayList<Integer>> _indexs = new HashMap<>();

    @Override
    public int compareTo(Peça other){
        return new Integer(this.hashCode()).compareTo(other.hashCode());
    }

    @Override
    public int hashCode() {
        return _x*100+_y;
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
        final Peça other = (Peça) obj;
        if (this._x != other._x) {
            return false;
        }
        if (this._y != other._y) {
            return false;
        }
        return Objects.equals(this._codi, other._codi);
    }


    public Peça(String _codi) {
        this._codi = _codi;
        //this.setGraphic(new ImageView(new Image("src/tiles/"+_codi+".png")));
        _indexs.put("V", new ArrayList<>());
        _indexs.put("C", new ArrayList<>());
        _indexs.put("F", new ArrayList<>());
    }

    public Peça(int _x, int _y, String _codi) {
        this._x = _x;
        this._y = _y;
        this._codi = _codi;
    }

    public void rotarClockWise(){
        //String nouCodi = ""+_codi.charAt(0)+_codi.charAt(4)+_codi.charAt(1)+_codi.charAt(2)+_codi.charAt(3);
        //_codi = nouCodi;
        Regio sud = _regions.get(3);
        for(int i=1;i<4;i++){
            _regions.set(i,_regions.get(i-1));
        }
        _regions.set(0,sud);
        _nRotacions++;
    }
    

    public boolean esCompatible(Peça peça, int orientacio){
        return _regions.get(orientacio).get_codi() == peça.getRegio((orientacio+2)%4).get_codi();
    }

    public Peça getPeçaAdjacent(int a){
        if(a < 0 || a > 4) throw new IndexOutOfBoundsException("Index "+a+" is out of bounds");
        else return _adjacents.get(a);
    }
    
    public Regio getRegio(int r){
        if(r < 0 || r > 4) throw new IndexOutOfBoundsException("Index "+r+" is out of bounds");
        else return _regions.get(r);
    }
    
    public void afegirSeguidor(int i, String color){
        _regions.get(i).setSeguidor(color);
    }

    public int afegirRegions(int nReg){
        int n = 0;
        char c = centre();
        if(c == 'V' || c == 'E'){
            Regio v = new Regio(nReg,c=='E',this,'V');
            n++;
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V') _regions.set(i-1, v);
            }
        }
        else{
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V'){
                    Regio v = new Regio(nReg+(n++),this,'V');
                    _regions.set(i-1, v);
                }
            }
        }
        if(c == 'X'){
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='C'){
                    Regio p = new Regio(nReg+(n++),this,'C');
                    _regions.set(i-1, p);
                }
            }
        }
        else{
            if(_codi.indexOf('C')>0){
                Regio p = new Regio(nReg+(n++),this,'C');
                for(int i=1;i<5;i++){
                    if(_codi.charAt(i)=='C') _regions.set(i-1, p);
                }
            }
        }
        if(c == 'F'){
            Regio f = new Regio(nReg,this,'F');
            n++;
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='F') _regions.set(i-1, f);
            }
        }
        else{
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='F'){
                    Regio f = new Regio(nReg+(n++),this,'F');
                    _regions.set(i-1, f);
                }
            }
        }
        return n;
    }

    public void setPeçaAdjacent(Peça peça, int orientacio){
        _adjacents.set(orientacio,peça);
    }

    public char getRegioAdjacent(int a){
        if(a > 0 || a < 4) throw new IndexOutOfBoundsException("Index "+a+" is out of bounds");
        else return _codi.charAt(a);
    }
    

    public boolean hiHaEscut(){
        return _codi.indexOf('E') > 0;
    }

    public List<Integer> indexRegio(char regio){
        List<Integer> retorn = new ArrayList<>();
        for(int i=1;i<5;i++){
            if(_codi.charAt(i)==regio) retorn.add(i);
        }
        return retorn;
    }

    public List<Peça> adjacenciesConnexes(char regio){
        return _adjacents.stream()
                .filter(p -> p != null && _codi.charAt(_adjacents.indexOf(p))==regio).collect(Collectors.toList());
    }

    public char centre(){ return _codi.charAt(0);}

    public List<Peça> get_adjacents() {
        return _adjacents;
    }
    
    public List<Regio> get_regions(){
        return _regions;
    }

    public void set_adjacents(List<Peça> _adjacents) {
        this._adjacents = _adjacents;
    }
    
    public Map<String,ArrayList<Integer>> get_indexs(){
        return _indexs;
    }

    public String get_codi() {
        return _codi;
    }

    public void set_codi(String _codi) {
        this._codi = _codi;
    }

    public int get_x() {
        return _x;
    }
    
    public void set_indexs(){
        for(int i=1;i<5;i++){
            _indexs.get(""+_regions.get(i).get_codi()).add(i-1);
        }
    }

    public void set_x(int _x) {
        this._x = _x;
    }

    public int get_y() {
        return _y;
    }

    public void set_y(int _y) {
        this._y = _y;
    }
    
    public int getIndexRotacio(){
        return _nRotacions % 4;
    }
}
