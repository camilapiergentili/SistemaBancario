package are.edu.utn.frbb.tup.sistemasBancarios.controller.dto;

import are.edu.utn.frbb.tup.sistemasBancarios.model.Cliente;
import are.edu.utn.frbb.tup.sistemasBancarios.model.TipoPersona;

import java.time.LocalDate;

public class ClienteDto extends PersonaDto{

    private String tipoPersona;
    private String banco;




    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
