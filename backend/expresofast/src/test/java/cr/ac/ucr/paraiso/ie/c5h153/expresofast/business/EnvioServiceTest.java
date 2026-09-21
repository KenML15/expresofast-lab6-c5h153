package cr.ac.ucr.paraiso.ie.c5h153.expresofast.business;

import cr.ac.ucr.paraiso.ie.c5h153.expresofast.data.BitacoraEnvioRepository;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.data.ConductorRepository;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.data.EnvioRepository;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.data.UsuarioRepository;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.data.VehiculoRepository;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.domain.Conductor;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.domain.Envio;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.domain.Vehiculo;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.dto.EnvioRequestDTO;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.dto.EnvioResponseDTO;
import cr.ac.ucr.paraiso.ie.c5h153.expresofast.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ConductorRepository conductorRepository;

    @Mock
    private BitacoraEnvioRepository bitacoraEnvioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EnvioService envioService;

    private Vehiculo vehiculo;
    private Conductor conductor;
    private EnvioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        vehiculo = new Vehiculo();
        vehiculo.setId(1);
        vehiculo.setPlaca("SJO-123");
        vehiculo.setCapacidadKg(new BigDecimal("500.00"));

        conductor = new Conductor();
        conductor.setId(1);
        conductor.setNombre("Carlos");
        conductor.setApellidos("Mora Vargas");

        requestDTO = new EnvioRequestDTO();
        requestDTO.setCodigoRastreo("EXP-1001");
        requestDTO.setDireccionDestino("San José, Costa Rica");
        requestDTO.setPesoKg(new BigDecimal("100.00"));
        requestDTO.setCosto(new BigDecimal("15000.00"));
        requestDTO.setVehiculoId(1);
        requestDTO.setConductorId(1);
    }

    @Test
    @DisplayName("Debe registrar un envío válido y retornarlo en estado PENDIENTE")
    void registrarEnvio_DatosValidos_RetornaEnvioResponseDTO() {
        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(1)).thenReturn(Optional.of(conductor));
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> {
            Envio envioGuardado = invocation.getArgument(0);
            envioGuardado.setId(10);
            return envioGuardado;
        });

        EnvioResponseDTO resultado = envioService.registrarEnvio(requestDTO);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstadoEnvio());
        assertEquals("EXP-1001", resultado.getCodigoRastreo());
        assertEquals("SJO-123", resultado.getPlacaVehiculo());
        assertEquals("Carlos Mora Vargas", resultado.getNombreConductor());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si el peso supera la capacidad del vehículo")
    void registrarEnvio_PesoExcedeCapacidad_LanzaIllegalArgumentException() {
        requestDTO.setPesoKg(new BigDecimal("999.00"));

        when(vehiculoRepository.findById(1)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(1)).thenReturn(Optional.of(conductor));

        assertThrows(IllegalArgumentException.class, () -> envioService.registrarEnvio(requestDTO));
        verify(envioRepository, never()).save(any(Envio.class));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el vehículo no existe")
    void registrarEnvio_VehiculoInexistente_LanzaResourceNotFoundException() {
        when(vehiculoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> envioService.registrarEnvio(requestDTO));
        verify(envioRepository, never()).save(any(Envio.class));
    }
}