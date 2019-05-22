/** @file Cami.java
    @brief Classe Cami
*/
import java.util.Set;
import java.util.TreeSet;

/** @class Cami
    @brief Costrucció de tipus Camí
    @author Ferran Capallera
*/
public class Cami extends Construccio {
    /** @brief Constructor de Cami a partir d'una regió.
	@pre cert
	@post S'ha creat el camí i conté una Regió. */
    public Cami(Regio regio){ super(regio);}
    
    /** @brief Implementació del mètode abstracte puntuar.
	@pre cert
	@post retorna el nombre de peces ("rajoles") que formen el camí. */
    @Override
    public int puntuar(){
        Set<Peça> peces = new TreeSet<>();
        _regions.forEach((r) -> {
            peces.add(r.get_peça());
        });
        return peces.size();
    }
}
