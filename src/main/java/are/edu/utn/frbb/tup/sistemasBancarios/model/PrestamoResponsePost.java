package are.edu.utn.frbb.tup.sistemasBancarios.model;

import java.util.List;

public class PrestamoResponsePost {

        private String estado;
        private String mensaje;
        private List<Pagos> planPagos;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public List<Pagos> getPlanPagos() {
            return planPagos;
        }

        public void setPlanPagos(List<Pagos> planPagos) {
            this.planPagos = planPagos;
        }

}
