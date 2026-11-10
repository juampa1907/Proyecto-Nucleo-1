package co.edu.unbosque.collegeAdministrator.config;

import co.edu.unbosque.collegeAdministrator.entity.LimiteInventarioAula;
import co.edu.unbosque.collegeAdministrator.entity.enums.Sede;
import co.edu.unbosque.collegeAdministrator.entity.enums.TipoEspacio;
import co.edu.unbosque.collegeAdministrator.repository.LimiteInventarioAulaRepository;
import co.edu.unbosque.collegeAdministrator.repository.UsuarioRepository;
import co.edu.unbosque.collegeAdministrator.service.api.ParametrosAcademicosServiceAPI;
import co.edu.unbosque.collegeAdministrator.service.api.UsuarioServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Carga los datos mínimos de la aplicación al arrancar.
 */
@Component
public class DatosIniciales implements CommandLineRunner {

    /** El registrador de eventos de la clase. */
    private static final Logger LOG = LoggerFactory.getLogger(DatosIniciales.class);

    /** El servicio de parámetros académicos institucionales. */
    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    /** El repositorio de los topes de inventario de aulas. */
    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;
    /** El repositorio de persistencia de usuarios. */
    private final UsuarioRepository usuarioRepository;
    /** El servicio de gestión de usuarios. */
    private final UsuarioServiceAPI usuarioService;
    /** El nombre del administrador inicial. */
    private final String adminUsername;
    /** El correo del administrador inicial. */
    private final String adminCorreo;
    /** La contraseña del administrador inicial. */
    private final String adminPassword;

    /**
     * Crea una instancia de {@code DatosIniciales} con las dependencias requeridas.
     *
     * @param parametrosAcademicosService el servicio de parámetros académicos institucionales
     * @param limiteInventarioAulaRepository el repositorio de los topes de inventario de aulas
     * @param usuarioRepository el repositorio de persistencia de usuarios
     * @param usuarioService el servicio de gestión de usuarios
     * @param adminUsername el nombre del administrador inicial
     * @param adminCorreo el correo del administrador inicial
     * @param adminPassword la contraseña del administrador inicial
     */
    public DatosIniciales(ParametrosAcademicosServiceAPI parametrosAcademicosService,
                          LimiteInventarioAulaRepository limiteInventarioAulaRepository,
                          UsuarioRepository usuarioRepository,
                          UsuarioServiceAPI usuarioService,
                          @Value("${app.admin.username}") String adminUsername,
                          @Value("${app.admin.correo}") String adminCorreo,
                          @Value("${app.admin.password}") String adminPassword) {
        this.parametrosAcademicosService = parametrosAcademicosService;
        this.limiteInventarioAulaRepository = limiteInventarioAulaRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.adminUsername = adminUsername;
        this.adminCorreo = adminCorreo;
        this.adminPassword = adminPassword;
    }

    /**
     * Asegura que existan los parámetros académicos, carga los topes de inventario si no hay ninguno y crea
     * el administrador inicial si no hay usuarios.
     *
     * @param args los argumentos de arranque; no se usan
     */
    @Override
    @Transactional
    public void run(String... args) {
        parametrosAcademicosService.obtener();
        if (limiteInventarioAulaRepository.count() == 0) {
            limiteInventarioAulaRepository.saveAll(List.of(
                    new LimiteInventarioAula(TipoEspacio.REGULAR, Sede.USAQUEN, 123),
                    new LimiteInventarioAula(TipoEspacio.REGULAR, Sede.CHIA, 26),
                    new LimiteInventarioAula(TipoEspacio.COMPUTO, Sede.USAQUEN, 20),
                    new LimiteInventarioAula(TipoEspacio.COMPUTO, Sede.CHIA, 0),
                    new LimiteInventarioAula(TipoEspacio.LABORATORIO, Sede.USAQUEN, 38),
                    new LimiteInventarioAula(TipoEspacio.LABORATORIO, Sede.CHIA, 2),
                    new LimiteInventarioAula(TipoEspacio.ESPECIALIZADA, Sede.USAQUEN, 73),
                    new LimiteInventarioAula(TipoEspacio.ESPECIALIZADA, Sede.CHIA, 0),
                    new LimiteInventarioAula(TipoEspacio.AUDITORIO, Sede.USAQUEN, 4),
                    new LimiteInventarioAula(TipoEspacio.AUDITORIO, Sede.CHIA, 0),
                    new LimiteInventarioAula(TipoEspacio.MULTIPROPOSITO, Sede.USAQUEN, 59),
                    new LimiteInventarioAula(TipoEspacio.MULTIPROPOSITO, Sede.CHIA, 10)));
        }
        if (usuarioRepository.count() == 0) {
            usuarioService.crearAdministrador(adminUsername, adminCorreo, adminPassword);
            LOG.warn("Se creó el administrador inicial '{}'. Cambia su contraseña después del primer ingreso.", adminUsername);
        }
    }
}
