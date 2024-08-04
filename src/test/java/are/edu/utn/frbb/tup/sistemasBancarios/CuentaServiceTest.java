package are.edu.utn.frbb.tup.sistemasBancarios;


import are.edu.utn.frbb.tup.sistemasBancarios.controller.dto.CuentaDto;
import are.edu.utn.frbb.tup.sistemasBancarios.model.*;
import are.edu.utn.frbb.tup.sistemasBancarios.model.exception.*;
import are.edu.utn.frbb.tup.sistemasBancarios.persistence.implementation.ImplementsCuentaDao;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.ClienteServiceImplementation;
import are.edu.utn.frbb.tup.sistemasBancarios.service.implementation.CuentaServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class CuentaServiceTest {

    @InjectMocks
    CuentaServiceImplementation cuentaService;

    @Mock
    ImplementsCuentaDao cuentaDao;

    @Mock
    ClienteServiceImplementation clienteService;

    private CuentaDto cuentaDto;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CC");
        cuentaDto.setTipoMoneda("pesos");
    }


    //Valida que se lance la exception CuentaAlreadyExistsException
    @Test
    public void cuentaAlreadyExistsExceptionDarAltaCuentaTest(){

        long dniCliente = 38944251;

        when(cuentaDao.find(anyLong(), eq(false))).thenReturn(new Cuenta());
        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darAltaCuenta(cuentaDto, dniCliente));
    }


    //Valida que se lance la exception TipoCuentaNotSupportedException
    @Test
    public void tipoCuentaNotSupportedExceptionDarAltaCuentaTest(){

        cuentaDto.setTipoMoneda("dolares");
        Cuenta cuenta = toCuenta(cuentaDto);
        cuentaService.cuentaSoportada(cuenta);

        assertThrows(TipoCuentaNotSupportedException.class, () -> cuentaService.darAltaCuenta(cuentaDto, 38944251));

    }

    @Test
    public void darAltaCuentaTest() throws ClienteNoExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException, CuentaAlreadyExistsException {
        long dni = 38944251;

        // Configuración de mocks
        when(cuentaDao.find(anyLong(), eq(false))).thenReturn(null);
        doNothing().when(clienteService).agregarCuentasAlCliente(any(Cuenta.class), anyLong());


        cuentaService.darAltaCuenta(cuentaDto, dni);

        // Verificar interacciones
        verify(cuentaDao, times(1)).save(any(Cuenta.class));
        verify(clienteService, times(1)).agregarCuentasAlCliente(any(Cuenta.class), eq(dni));
    }

    @Test
    public void listCuentasByClienteConCuentasTest() throws CuentaNoEncontradaException {

        long dni = 38944251;
        List<Cuenta> mockCuentas = new ArrayList<>();
        mockCuentas.add(new Cuenta());
        mockCuentas.add(new Cuenta());

        when(cuentaDao.cuentasDelCliente(dni)).thenReturn(mockCuentas);

        List<Cuenta> cuentas = cuentaService.listCuentasByCliente(dni);

        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
        assertEquals(mockCuentas, cuentas);
        verify(cuentaDao, times(1)).cuentasDelCliente(dni);


    }

    @Test
    public void listCuentasByClienteSinCuentasTest(){
        long dni = 38944251;
        List<Cuenta> mockCuentas = new ArrayList<>();

        when(cuentaDao.cuentasDelCliente(dni)).thenReturn(mockCuentas);

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.listCuentasByCliente(dni));

    }

    @Test
    public void actualizarTitularCuentaTest() throws CuentaNoEncontradaException {

        long dniAntiguo = 38944251;

        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setDni(123456789);
        clienteActualizado.setNombre("Nuevo");
        clienteActualizado.setApellido("cliente");
        clienteActualizado.setFechaNacimiento(LocalDate.of(1997, 5, 11));

        List<Cuenta> cuentasMock = new ArrayList<>();
        cuentasMock.add(new Cuenta());

        when(cuentaDao.cuentasDelCliente(dniAntiguo)).thenReturn(cuentasMock);

        cuentaService.actualizarTitularCuenta(clienteActualizado, dniAntiguo);

        for(Cuenta c : cuentasMock){
            assertEquals(clienteActualizado, c.getTitular());
        }

        verify(cuentaDao, times(1)).save(any(Cuenta.class));

    }

    @Test
    public void actualizarTitularCuentaNegativaTest() throws CuentaNoEncontradaException {
        long dniAntiguo = 38944251;

        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setDni(123456789);
        clienteActualizado.setNombre("Nuevo");
        clienteActualizado.setApellido("cliente");
        clienteActualizado.setFechaNacimiento(LocalDate.of(1997, 5, 11));

        List<Cuenta> cuentasMock = new ArrayList<>();

        when(cuentaDao.cuentasDelCliente(dniAntiguo)).thenReturn(cuentasMock);

        CuentaNoEncontradaException exception = assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.listCuentasByCliente(dniAntiguo));

        assertEquals("El cliente no tiene cuentas", exception.getMessage());
        verify(cuentaDao, times(1)).cuentasDelCliente(dniAntiguo);
    }

    @Test
    public void buscarCuentaByNumeroTest() throws CuentaNoEncontradaException {
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock.setTipoMoneda(TipoMoneda.ARS);

        when(cuentaDao.find(cuentaMock.getNumeroCuenta(), true)).thenReturn(cuentaMock);

        Cuenta cuenta = cuentaService.buscarCuentaPorNumero(cuentaMock.getNumeroCuenta());

        assertEquals(cuentaMock, cuenta);
    }

    @Test
    public void buscarCuentaByNumeroNullTest(){
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock.setTipoMoneda(TipoMoneda.ARS);

        when(cuentaDao.find(cuentaMock.getNumeroCuenta(), true)).thenReturn(null);

        assertThrows(CuentaNoEncontradaException.class, () -> cuentaService.buscarCuentaPorNumero(cuentaMock.getNumeroCuenta()));

    }

    @Test
    public void cuentaSoportadaTrueTest(){
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock.setTipoMoneda(TipoMoneda.ARS);

        assertTrue(cuentaService.cuentaSoportada(cuentaMock));
    }

    @Test
    public void cuentaSoportadaFalseTest(){

        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock.setTipoMoneda(TipoMoneda.USD);

        assertFalse(cuentaService.cuentaSoportada(cuentaMock));
    }

    @Test
    public void tieneCuentaDeMonedaTrueTest(){

        long dni = 38944251;
        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock.setTipoMoneda(TipoMoneda.ARS);

        List<Cuenta> cuentasMock = new ArrayList<>();
        cuentasMock.add(cuentaMock);

        Cuenta cuentaMock2 = new Cuenta();
        cuentaMock2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaMock2.setTipoMoneda(TipoMoneda.ARS);

        cuentasMock.add(cuentaMock2);

        when(cuentaDao.cuentasDelCliente(dni)).thenReturn(cuentasMock);

        boolean estado = cuentaService.tieneCuentaDeTipoMoneda(dni, cuentaMock.getTipoMoneda(), cuentaMock.getTipoCuenta());

        assertTrue(estado);

    }

    @Test
    public void obtenerCuentaParaPrestamoTest(){
        long dni = 38944251;
        TipoMoneda moneda = TipoMoneda.ARS;

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setTipoMoneda(TipoMoneda.ARS);

        List<Cuenta> cuentasMock = new ArrayList<>();
        cuentasMock.add(cuenta);

        when(cuentaDao.cuentasDelCliente(dni)).thenReturn(cuentasMock);

        Cuenta obtenerCuenta = cuentaService.obtenerCuentaParaPrestamo(dni, moneda);

        assertNotNull(obtenerCuenta);
        assertEquals(cuenta, obtenerCuenta);
        verify(cuentaDao, times(1)).cuentasDelCliente(dni);

    }

    @Test
    public void obtenerCuentaParaPrestamoNullTest(){

        long dni = 38944251;
        TipoMoneda moneda = TipoMoneda.USD;

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setTipoMoneda(TipoMoneda.ARS);

        List<Cuenta> cuentasByCliente = new ArrayList<>();
        cuentasByCliente.add(cuenta);

        when(cuentaDao.cuentasDelCliente(dni)).thenReturn(cuentasByCliente);

        Cuenta cuentaPrestamo = cuentaService.obtenerCuentaParaPrestamo(dni, moneda);

        assertNull(cuentaPrestamo);

    }



    private Cuenta toCuenta(CuentaDto cuentaDto){
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.fromString(cuentaDto.getTipoCuenta()));
        cuenta.setTipoMoneda(TipoMoneda.fromString(cuentaDto.getTipoMoneda()));

        return cuenta;
    }





}
