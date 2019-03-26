import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Peça implements Comparable<Peça>{
    int _x;
    int _y;
    private String _codi;
    private List<Peça> _adjacents = new ArrayList<>(Arrays.asList(null,null,null,null));
    private List<Regio> _regions = new ArrayList<>(Arrays.asList(null,null,null,null));

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

    /*public int afegirRegions(int nReg){
        int i = 0;
        if()
        return i;
    }*/

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

    public void set_adjacents(List<Peça> _adjacents) {
        this._adjacents = _adjacents;
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
