package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import modelo.Factura;
import modelo.Cliente;
import modelo.OrdenReparacion;
import modelo.Vehiculo;
import modelo.Pago;

import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PDFGenerator {

    public static boolean generarComprobantePago(int facturaId, int pagoId, String rutaDestino) {
        try {
            if (rutaDestino == null || rutaDestino.trim().isEmpty()) {
                String userHome = System.getProperty("user.home");
                rutaDestino = Paths.get(userHome, "Downloads", "comprobante_pago_" + facturaId + ".pdf").toString();
            }

            // Crear documento correctamente sin sobrecargar la clase Document
            Document documento = new Document(PageSize.A5);
            PdfWriter.getInstance(documento, new FileOutputStream(rutaDestino));
            documento.open();

            // Obtener datos de la factura, pago y orden
            DatosComprobante datos = obtenerDatosComprobante(facturaId, pagoId);
            if (datos == null) {
                return false;
            }

            // Agregar contenido al PDF
            agregarEncabezado(documento);
            agregarInfoEmpresa(documento);
            agregarDetallesComprobante(documento, datos);
            agregarDetallesPago(documento, datos);
            agregarPieComprobante(documento);

            documento.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void agregarEncabezado(Document documento) throws DocumentException {
        // Logo
        try {
            Image logo = Image.getInstance("src/recursos/logo_taller.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            documento.add(logo);
        } catch (Exception e) {
            // Si no encuentra el logo, continúa sin él
            System.out.println("No se encontró el logo, continuando sin él: " + e.getMessage());
        }

        // Título del comprobante
        Paragraph titulo = new Paragraph("COMPROBANTE DE PAGO",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(15);
        documento.add(titulo);
    }

    private static void agregarInfoEmpresa(Document documento) throws DocumentException {
        Paragraph infoEmpresa = new Paragraph();
        infoEmpresa.setAlignment(Element.ALIGN_CENTER);
        infoEmpresa.add(new Chunk("TALLER MECÁNICO AUTOMOTRIZ\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        infoEmpresa.add("Dirección: Av. Los Motores 123, Lima\n");
        infoEmpresa.add("Teléfono: (01) 555-1234\n");
        infoEmpresa.add("Email: contacto@tallermeca.com\n");
        infoEmpresa.add("RUC: 20123456789\n");
        infoEmpresa.setSpacingAfter(15);
        documento.add(infoEmpresa);

        // Línea separadora
        LineSeparator linea = new LineSeparator();
        linea.setLineColor(new BaseColor(232, 232, 232));
        documento.add(new Chunk(linea));
    }

    private static void agregarDetallesComprobante(Document documento, DatosComprobante datos) throws DocumentException {
        PdfPTable tablaCliente = new PdfPTable(2);
        tablaCliente.setWidthPercentage(100);
        tablaCliente.setSpacingBefore(10);
        tablaCliente.setSpacingAfter(10);

        // Cliente y Fecha
        PdfPCell celdaClienteLabel = new PdfPCell(new Phrase("CLIENTE:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        celdaClienteLabel.setBorder(Rectangle.NO_BORDER);
        tablaCliente.addCell(celdaClienteLabel);

        PdfPCell celdaFechaLabel = new PdfPCell(new Phrase("FECHA:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        celdaFechaLabel.setBorder(Rectangle.NO_BORDER);
        celdaFechaLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaCliente.addCell(celdaFechaLabel);

        PdfPCell celdaCliente = new PdfPCell(new Phrase(datos.nombreCliente,
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaCliente.setBorder(Rectangle.NO_BORDER);
        tablaCliente.addCell(celdaCliente);

        PdfPCell celdaFecha = new PdfPCell(new Phrase(datos.fechaPago,
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaFecha.setBorder(Rectangle.NO_BORDER);
        celdaFecha.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaCliente.addCell(celdaFecha);

        // Número de factura y Método de pago
        PdfPCell celdaFacturaLabel = new PdfPCell(new Phrase("N° FACTURA:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        celdaFacturaLabel.setBorder(Rectangle.NO_BORDER);
        tablaCliente.addCell(celdaFacturaLabel);

        PdfPCell celdaMetodoLabel = new PdfPCell(new Phrase("MÉTODO DE PAGO:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        celdaMetodoLabel.setBorder(Rectangle.NO_BORDER);
        celdaMetodoLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaCliente.addCell(celdaMetodoLabel);

        PdfPCell celdaFactura = new PdfPCell(new Phrase("F-" + String.format("%06d", datos.facturaId),
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaFactura.setBorder(Rectangle.NO_BORDER);
        tablaCliente.addCell(celdaFactura);

        PdfPCell celdaMetodo = new PdfPCell(new Phrase(datos.metodoPago,
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaMetodo.setBorder(Rectangle.NO_BORDER);
        celdaMetodo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaCliente.addCell(celdaMetodo);

        // Vehículo
        PdfPCell celdaVehiculoLabel = new PdfPCell(new Phrase("VEHÍCULO:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        celdaVehiculoLabel.setBorder(Rectangle.NO_BORDER);
        celdaVehiculoLabel.setColspan(2);
        tablaCliente.addCell(celdaVehiculoLabel);

        PdfPCell celdaVehiculo = new PdfPCell(new Phrase(
                datos.marcaVehiculo + " " + datos.modeloVehiculo + " (" + datos.placaVehiculo + ")",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaVehiculo.setBorder(Rectangle.NO_BORDER);
        celdaVehiculo.setColspan(2);
        tablaCliente.addCell(celdaVehiculo);

        documento.add(tablaCliente);

        // Línea separadora
        LineSeparator linea = new LineSeparator();
        linea.setLineColor(new BaseColor(232, 232, 232));
        documento.add(new Chunk(linea));
    }

    private static void agregarDetallesPago(Document documento, DatosComprobante datos) throws DocumentException {
        // Título de detalles
        Paragraph titleDetalles = new Paragraph("DETALLE DE PAGO",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11));
        titleDetalles.setAlignment(Element.ALIGN_CENTER);
        titleDetalles.setSpacingBefore(10);
        titleDetalles.setSpacingAfter(10);
        documento.add(titleDetalles);

        // Tabla de detalles
        PdfPTable tablaDetalles = new PdfPTable(2);
        tablaDetalles.setWidthPercentage(100);

        // Configurar anchos relativos de las columnas
        float[] anchos = {7f, 3f};
        tablaDetalles.setWidths(anchos);

        // Encabezados
        PdfPCell celdaDescripcion = new PdfPCell(new Phrase("DESCRIPCIÓN",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        celdaDescripcion.setBackgroundColor(new BaseColor(66, 66, 66));
        celdaDescripcion.setPadding(5);
        tablaDetalles.addCell(celdaDescripcion);

        PdfPCell celdaMonto = new PdfPCell(new Phrase("MONTO",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        celdaMonto.setBackgroundColor(new BaseColor(66, 66, 66));
        celdaMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaMonto.setPadding(5);
        tablaDetalles.addCell(celdaMonto);

        // Datos de la orden
        PdfPCell celdaServicio = new PdfPCell(new Phrase("Servicios de reparación y mantenimiento",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaServicio.setPadding(5);
        tablaDetalles.addCell(celdaServicio);

        PdfPCell celdaTotal = new PdfPCell(new Phrase(String.format("S/ %.2f", datos.montoTotal),
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setPadding(5);
        tablaDetalles.addCell(celdaTotal);

        // Total a pagar
        PdfPCell celdaTotalLabel = new PdfPCell(new Phrase("TOTAL PAGADO:",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        celdaTotalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotalLabel.setPadding(5);
        tablaDetalles.addCell(celdaTotalLabel);

        PdfPCell celdaTotalPagado = new PdfPCell(new Phrase(String.format("S/ %.2f", datos.montoTotal),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        celdaTotalPagado.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotalPagado.setPadding(5);
        tablaDetalles.addCell(celdaTotalPagado);

        documento.add(tablaDetalles);
    }

    private static void agregarPieComprobante(Document documento) throws DocumentException {
        Paragraph pie = new Paragraph();
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(30);
        pie.add("Gracias por su preferencia\n");
        pie.add("Este documento es un comprobante de pago válido\n");
        documento.add(pie);

        // Agregar información de generación
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Paragraph infoGeneracion = new Paragraph("Generado el: " + sdf.format(new Date()),
                FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.GRAY));
        infoGeneracion.setAlignment(Element.ALIGN_RIGHT);
        infoGeneracion.setSpacingBefore(20);
        documento.add(infoGeneracion);
    }

    /**
     * Clase para almacenar los datos necesarios para el comprobante
     */
    private static class DatosComprobante {
        int facturaId;
        String nombreCliente;
        String fechaPago;
        String metodoPago;
        double montoTotal;
        String marcaVehiculo;
        String modeloVehiculo;
        String placaVehiculo;
    }

    /**
     * Obtiene todos los datos necesarios para generar el comprobante
     */
    private static DatosComprobante obtenerDatosComprobante(int facturaId, int pagoId) {
        Connection con = modelo.Database.conectar();
        DatosComprobante datos = new DatosComprobante();

        try {
            // Consulta para obtener datos de pago, factura, orden, vehículo y cliente
            String sql = "SELECT p.fecha as fecha_pago, p.metodo_pago, p.monto, " +
                    "f.id as factura_id, f.total as factura_total, " +
                    "o.vehiculo_id, " +
                    "v.placa, v.marca, v.modelo, " +
                    "c.nombre as cliente_nombre " +
                    "FROM pagos p " +
                    "JOIN facturas f ON p.factura_id = f.id " +
                    "JOIN ordenes_reparacion o ON f.orden_id = o.id " +
                    "JOIN vehiculos v ON o.vehiculo_id = v.id " +
                    "JOIN clientes c ON v.cliente_id = c.id " +
                    "WHERE f.id = ? AND p.id = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            stmt.setInt(2, pagoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                datos.facturaId = rs.getInt("factura_id");
                datos.nombreCliente = rs.getString("cliente_nombre");
                datos.fechaPago = rs.getString("fecha_pago");
                datos.metodoPago = rs.getString("metodo_pago");
                datos.montoTotal = rs.getDouble("factura_total");
                datos.marcaVehiculo = rs.getString("marca");
                datos.modeloVehiculo = rs.getString("modelo");
                datos.placaVehiculo = rs.getString("placa");

                con.close();
                return datos;
            }

            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener datos para el comprobante: " + e.getMessage());
        }

        return null;
    }
}