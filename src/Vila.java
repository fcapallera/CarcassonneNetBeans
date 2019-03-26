import java.util.Iterator;

public class Vila extends Construccio {
    public Vila(Regio regio){ super(regio);}

    @Override
    public int puntuar(){
        int puntuacio = super.puntuar() * 2;
        Iterator<Regio> it = _regions.iterator();
        while(it.hasNext()){
            if(it.next().hiHaEscut()) puntuacio += 2;
        }
        return puntuacio;
    }
}
