/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class Monestir extends Construccio {
    private Peça _peça;
    
    public Monestir(Regio regio) {
        super(regio);
    }
    
    public void set_peça(Peça peça){
        _peça = peça;
        int[] hashKeyAdj = {100,101,1,-99,-100,-101,-1,99};
        for(int i : hashKeyAdj){
            _pendents.add(peça.hashCode()+i);
        }
    }
    
}
