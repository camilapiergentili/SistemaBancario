package are.edu.utn.frbb.tup.sistemasBancarios.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Cliente extends Persona{

    private LocalDate fechaAltaCliente;
    private String banco;
    private TipoPersona tipoPersona;
    private Set<Cuenta> cuentasDelCliente;

    public Cliente(){
        this.fechaAltaCliente = LocalDate.now();
        cuentasDelCliente = new HashSet<>();
    }

    public LocalDate getFechaAltaCliente() {
        return fechaAltaCliente;
    }

    public void setFechaAltaCliente(LocalDate fechaAltaCliente) {
        this.fechaAltaCliente = fechaAltaCliente;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public Set<Cuenta> getCuentasDelCliente() {
        return cuentasDelCliente;
    }

    public void setCuentasDelCliente(Cuenta cuenta){
        this.cuentasDelCliente.add(cuenta);
    }


    public boolean tieneCuenta(TipoCuenta tipocuenta, TipoMoneda tipoMoneda){

        for(Cuenta  cuenta : cuentasDelCliente){
            if(cuenta.getTipoCuenta().equals(tipocuenta) && cuenta.getTipoMoneda().equals(tipoMoneda)){
                return true;
            }
        }

        return false;
    }
}
