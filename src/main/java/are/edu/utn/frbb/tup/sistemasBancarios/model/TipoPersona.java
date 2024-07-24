package are.edu.utn.frbb.tup.sistemasBancarios.model;


public enum TipoPersona {
    PERSONA_FISICA("F"),
    PERSONA_JURIDICA("J");

    private final String text;

    TipoPersona(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static TipoPersona fromString(String opcion){

        for(TipoPersona tipo : TipoPersona.values()){
            if(tipo.text.equalsIgnoreCase(opcion)){
                return tipo;
            }
        }

        throw new IllegalArgumentException("No se pudo encontrar un TipoPersona con la descripción: " + opcion);

    }

}
