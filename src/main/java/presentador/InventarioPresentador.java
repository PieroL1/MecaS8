package presentador;

import modelo.Pieza;

import java.util.List;

public class InventarioPresentador {

    public List<Pieza> obtenerPiezas() {
        return Pieza.obtenerTodas();
    }

    public Pieza obtenerPiezaPorId(int id) {
        return Pieza.obtenerPiezaPorId(id);
    }

    public boolean actualizarStock(int id, int cantidad) {
        return Pieza.actualizarStock(id, cantidad);
    }

    public boolean registrarPieza(Pieza pieza) {
        return Pieza.registrarPieza(pieza);
    }
}