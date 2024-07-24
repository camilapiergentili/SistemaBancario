package are.edu.utn.frbb.tup.sistemasBancarios.model;

public enum TipoTransaccion {

    TRANSFERENCIA("transferencia"),
    DEBITO("debito"),
    RETIRO("retiro"),
    DEPOSITO("deposito"),
    CONSULTA("consulta");

    private final String text;

    TipoTransaccion(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static TipoTransaccion fromString(String opcion){

        for(TipoTransaccion tipo : TipoTransaccion.values()){
            if(tipo.text.equalsIgnoreCase(opcion)){
                return tipo;
            }
        }

        throw new IllegalArgumentException("No se pudo encontrar un Tipo de Transaccion con la descripción: " + opcion);

    }

}
