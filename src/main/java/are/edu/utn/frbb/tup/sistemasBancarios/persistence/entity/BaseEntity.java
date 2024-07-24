package are.edu.utn.frbb.tup.sistemasBancarios.persistence.entity;


public class BaseEntity {

    private final long Id;

    public BaseEntity(long Id){
        this.Id = Id;
    }

    public long getId() {
        return Id;
    }
}
