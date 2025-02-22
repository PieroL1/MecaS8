package presentador;

import modelo.Factura;
import modelo.Pago;
import java.util.List;
import util.PDFGenerator;

public class FacturaPresentador {
    
    
    public boolean generarFactura(int ordenId) {
        return Factura.generarFactura(ordenId);
    }

    
    public boolean registrarPago(int facturaId, double monto, String metodoPago) {
        // Verificar si la factura ya está pagada
        if (Pago.facturaEstaPagada(facturaId)) {
            return false;
        }
        
        // Verificar que el monto del pago sea igual al total de la factura
        Factura factura = Factura.obtenerFacturaPorId(facturaId);
        if (factura == null || monto != factura.getTotal()) {
            return false;
        }
        
        Pago pago = new Pago(0, facturaId, monto, metodoPago, "");
        return pago.registrarPago();
    }

   
    public List<Factura> obtenerFacturas() {
        return Factura.obtenerFacturas();
    }
    
    
    public List<Factura> obtenerFacturasPendientes() {
        return Factura.obtenerFacturasPendientes();
    }
    
    public Factura obtenerFacturaPorId(int facturaId) {
        return Factura.obtenerFacturaPorId(facturaId);
    }
    
    public boolean generarComprobantePDF(int facturaId, int pagoId, String rutaDestino) {
        return PDFGenerator.generarComprobantePago(facturaId, pagoId, rutaDestino);
    }
}