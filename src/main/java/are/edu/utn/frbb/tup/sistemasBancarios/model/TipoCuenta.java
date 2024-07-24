package are.edu.utn.frbb.tup.sistemasBancarios.model;

public enum TipoCuenta {
    CUENTA_CORRIENTE("CC"),
    CAJA_AHORRO("CA");

    private final String text;

    TipoCuenta(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public static TipoCuenta fromString(String opcion){

        for(TipoCuenta tipo : TipoCuenta.values()){
            if(tipo.text.equalsIgnoreCase(opcion)){
                return tipo;
            }
        }

        throw new IllegalArgumentException("No se pudo encontrar un TipoCuenta con la descripción: " + opcion);

    }
}
