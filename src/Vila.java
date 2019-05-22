/** @file Vila.java
    @brief Classe Vila
*/
import java.util.Iterator;

/** @class Vila
    @brief Classe que representa una Vila
    @author 
*/
public class Vila extends Construccio {
    /** @brief Constructor de Vila
	@pre --
	@post Inicialitza una Vila */
    public Vila(Regio regio){ super(regio);}
    
    /** @brief Ens retorna la puntuació que obtenim amb aquesta Vila
	@pre --
	@post Retorna la puntuació de la Vila */
    @Override
    public int puntuar(){
        int puntuacio = _regions.size()*2;
        Iterator<Regio> it = _regions.iterator();
        while(it.hasNext()){
            if(it.next().hiHaEscut()) puntuacio += 2;
        }
        return puntuacio;
    }
}
