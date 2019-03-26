import java.util.Iterator;

public class Vila extends Construccio {
    public Vila(Peça peça){ super(peça);}

    @Override
    public int puntuar(){
        int puntuacio = super.puntuar() * 2;
        Iterator<Peça> it = _peces.iterator();
        while(it.hasNext()){
            if(it.next().hiHaEscut()) puntuacio += 2;
        }
        return puntuacio;
    }
}
