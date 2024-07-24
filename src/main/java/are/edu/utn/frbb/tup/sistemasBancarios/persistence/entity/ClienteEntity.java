package are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.Cuenta;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoPersona;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ClienteEntity extends BaseEntity {

    private final String nombre;
    private final String apellido;
    private final LocalDate fechaNacimiento;
    private final LocalDate fechaAltaCliente;
    private final String banco;
    private final String tipoPersona;
    private final Set<Long> cuentasDelCliente = new HashSet<>();

    public ClienteEntity(Cliente cliente){
        super(cliente.getDni());
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.fechaAltaCliente = cliente.getFechaAltaCliente();
        this.banco = cliente.getBanco();
        this.tipoPersona = cliente.getTipoPersona() != null ? cliente.getTipoPersona().getText() : null;
        if(cliente.getCuentasDelCliente() != null || !cliente.getCuentasDelCliente().isEmpty()){
            for (Cuenta c : cliente.getCuentasDelCliente()){
                this.cuentasDelCliente.add(c.getNumeroCuenta());
            }
        }
    }

    public Cliente toCliente(){
        Cliente cliente = new Cliente();
        cliente.setDni(super.getId());
        cliente.setNombre(this.nombre);
        cliente.setApellido(this.apellido);
        cliente.setFechaNacimiento(this.fechaNacimiento);
        cliente.setFechaAltaCliente(this.fechaAltaCliente);
        cliente.setBanco(this.banco);
        cliente.setTipoPersona(TipoPersona.fromString(this.tipoPersona));

        return cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDate getFechaAltaCliente() {
        return fechaAltaCliente;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public Set<Long> getCuentasDelCliente() {
        return cuentasDelCliente;
    }
}
