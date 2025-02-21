package presentador;

import modelo.OrdenReparacion;
import java.util.List;

public class OrdenReparacionPresentador {

    public boolean crearOrden(int vehiculoId, int usuarioId) {
        OrdenReparacion orden = new OrdenReparacion(0, vehiculoId, usuarioId, "Pendiente", "", "", 0.0);
        return orden.crearOrden();
    }

    public boolean actualizarEstado(int ordenId, String nuevoEstado) {
        OrdenReparacion orden = OrdenReparacion.obtenerOrdenPorId(ordenId);
        if (orden != null) {
            return orden.actualizarEstado(nuevoEstado);
        }
        return false;
    }

    public boolean asignarFechaEntrega(int ordenId, String fechaEntrega) {
        OrdenReparacion orden = OrdenReparacion.obtenerOrdenPorId(ordenId);
        if (orden != null) {
            return orden.asignarFechaEntrega(fechaEntrega);
        }
        return false;
    }

    public boolean calcularTotal(int ordenId, double nuevoTotal) {
        OrdenReparacion orden = OrdenReparacion.obtenerOrdenPorId(ordenId);
        if (orden != null) {
            return orden.calcularTotal(nuevoTotal);
        }
        return false;
    }

    public List<OrdenReparacion> obtenerOrdenesPorVehiculo(int vehiculoId) {
        return OrdenReparacion.obtenerHistorialPorVehiculo(vehiculoId);
    }
    
    public boolean asignarPieza(int ordenId, int piezaId, int cantidad) {
        return OrdenReparacion.asignarPieza(ordenId, piezaId, cantidad);
    }
    
    public List<OrdenReparacion> obtenerTodasOrdenes() {
        return OrdenReparacion.obtenerTodas();
    }

    
}