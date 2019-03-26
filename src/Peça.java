import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Peça implements Comparable<Peça>{
    int _x;
    int _y;
    private String _codi;
    private List<Peça> _adjacents = new ArrayList<>(Arrays.asList(null,null,null,null));
    private List<Regio> _regions = new ArrayList<>(Arrays.asList(null,null,null,null));
    private Map<String,ArrayList<Integer>> _indexs = new HashMap<>();

    @Override
    public int compareTo(Peça other){
        return new Integer(this.hashCode()).compareTo(new Integer(other.hashCode()));
    }

    @Override
    public int hashCode() {
        return _x*100+_y;
    }


    public Peça(String _codi) {
        this._codi = _codi;
        //this.setGraphic(new ImageView(new Image("src/tiles/"+_codi+".png")));
        _indexs.put("V", new ArrayList<>());
        _indexs.put("C", new ArrayList<>());
        _indexs.put("F", new ArrayList<>());
        for(int i=1;i<5;i++){
            _indexs.get(""+_codi.charAt(i)).add(i-1);
        }
    }

    public Peça(int _x, int _y, String _codi) {
        this._x = _x;
        this._y = _y;
        this._codi = _codi;
    }

    public void rotarClockWise(){
        //this.getGraphic().setRotate(-90);
        String nouCodi = ""+_codi.charAt(0)+_codi.charAt(4)+_codi.charAt(1)+_codi.charAt(2)+_codi.charAt(3);
        _codi = nouCodi;
    }

    public boolean esCompatible(Peça peça, int orientacio){
        return _codi.charAt(orientacio) == peça.get_codi().charAt((orientacio+2)%4);
    }

    public Peça getPeçaAdjacent(int a){
        if(a > 0 || a < 4) throw new IndexOutOfBoundsException("Index "+a+" is out of bounds");
        else return _adjacents.get(a);
    }
    
    public Regio getRegio(int r){
        if(r > 0 || r < 4) throw new IndexOutOfBoundsException("Index "+r+" is out of bounds");
        else return _regions.get(r);
    }
    
    public void afegirSeguidor(int i, String color){
        _regions.get(i).setSeguidor(color);
    }

    public int afegirRegions(int nReg){
        int n = 0;
        char c = centre();
        if(c == 'V' || c == 'E'){
            Regio v = new Regio(nReg,c=='E');
            n++;
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V') _regions.add(i-1, v);
            }
        }
        else{
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='V'){
                    Regio v = new Regio(nReg+(n++));
                    _regions.add(i-1, v);
                }
            }
        }
        if(c == 'X'){
            for(int i=1;i<5;i++){
                if(_codi.charAt(i)=='C'){
                    Regio p = new Regio(nReg+(n++));
                    _regions.add(i-1, p);
                }
            }
        }
        else{
            if(_codi.indexOf('C')>0){
                Regio p = new Regio(nReg+(n++));
                for(int i=1;i<5;i++){
                    if(_codi.charAt(i)=='C') _regions.add(i-1, p);
                }
            }
        }
        return n;
    }

    public void setPeçaAdjacent(Peça peça, int orientacio){
        _adjacents.add(peça);
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

    public void set_x(int _x) {
        this._x = _x;
    }

    public int get_y() {
        return _y;
    }

    public void set_y(int _y) {
        this._y = _y;
    }
}
