package presentador;

import modelo.Vehiculo;
import java.util.List;

public class VehiculoPresentador {
    public List<Vehiculo> obtenerVehiculos() {
        return Vehiculo.obtenerTodos();
    }

    public boolean agregarVehiculo(String clienteId, String placa, String marca, String modelo, String anio, String tipo) {
        Vehiculo nuevo = new Vehiculo(0, Integer.parseInt(clienteId), "", placa, marca, modelo, Integer.parseInt(anio), tipo);
        return nuevo.guardar();
    }

    public boolean modificarVehiculo(int id, String clienteId, String placa, String marca, String modelo, String anio, String tipo) {
        Vehiculo vehiculo = new Vehiculo(id, Integer.parseInt(clienteId), "", placa, marca, modelo, Integer.parseInt(anio), tipo);
        return vehiculo.editar();
    }

    public boolean eliminarVehiculo(int id) {
        Vehiculo vehiculo = new Vehiculo(id, 0, "", "", "", "", 0, "");
        return vehiculo.eliminar();
    }
}
