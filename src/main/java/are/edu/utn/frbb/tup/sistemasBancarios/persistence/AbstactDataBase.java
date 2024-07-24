package are.edu.utn.frbb.tup.sistemasBancarios.persistence;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstactDataBase {

    protected abstract String getEntityName();
    protected static Map<String, Map<Long, Object>> mapa = new HashMap<>();

    protected Map<Long, Object> getInMemoryDataBase(){
        if(mapa.get(getEntityName()) == null){
            mapa.put(getEntityName(), new HashMap<>());
        }

        return mapa.get(getEntityName());
    }

}
