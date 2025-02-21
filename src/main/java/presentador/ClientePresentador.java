package presentador;

import modelo.Cliente;
import java.util.List;

public class ClientePresentador {

    public List<Cliente> obtenerClientes() {
        return Cliente.obtenerTodos();
    }

    public boolean agregarCliente(String nombre, String direccion, String telefono, String correo) {
        Cliente nuevo = new Cliente(0, nombre, direccion, telefono, correo);
        return nuevo.guardar();
    }

    public boolean modificarCliente(int id, String nombre, String direccion, String telefono, String correo) {
        Cliente cliente = new Cliente(id, nombre, direccion, telefono, correo);
        return cliente.editar();
    }

    public boolean eliminarCliente(int id) {
        Cliente cliente = new Cliente(id, "", "", "", "");
        return cliente.eliminar();
    }

    public Cliente buscarCliente(String nombre) {
        return Cliente.buscar(nombre);
    }
}
