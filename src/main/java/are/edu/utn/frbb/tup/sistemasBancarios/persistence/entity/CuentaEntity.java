package are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity;

import are.edu.utn.frbb.tup.sistemasBancarios.model.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CuentaEntity extends BaseEntity{

    
    private final double saldo;
    private final LocalDate fechaAltaCuenta;
    private final Cliente dniTitutar;
    private final String tipoMoneda;
    private final String tipoCuenta;

    private final List<Long> listaMovimientos = new ArrayList<>();
    
    public CuentaEntity(Cuenta cuenta){
        super(cuenta.getNumeroCuenta());
        this.saldo = cuenta.getSaldo();
        this.fechaAltaCuenta = cuenta.getFechaAltaCuenta();
        this.dniTitutar = cuenta.getTitular();
        this.tipoMoneda = cuenta.getTipoMoneda().toString();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();

        if(cuenta.getMovimientosCuenta() != null || !cuenta.getMovimientosCuenta().isEmpty()){
            for(Movimientos m : cuenta.getMovimientosCuenta()){
                this.listaMovimientos.add(m.getIdTransaccion());

            }
        }
    }

    public Cuenta toCuenta(){
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(super.getId());
        cuenta.setTitular(this.dniTitutar);
        cuenta.setSaldo(this.saldo);
        cuenta.setFechaAltaCuenta(this.fechaAltaCuenta);
        cuenta.setTipoMoneda(TipoMoneda.valueOf(this.tipoMoneda));
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));

        return cuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public LocalDate getFechaAltaCuenta() {
        return fechaAltaCuenta;
    }

    public Cliente getDniTitutar() {
        return dniTitutar;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public List<Long> getListaMovimientos() {
        return listaMovimientos;
    }
}
