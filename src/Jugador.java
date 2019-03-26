public class Jugador {
    private int _id;
    private String _nom;
    private boolean _cpu = false;
    private int _punts = 0;
    private String _color;

    public Jugador(int _id) {
        this._id = _id;
    }

    public void setCpu() {
        this._cpu = true;
    }
}
