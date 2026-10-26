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


@Component
public class DatosIniciales implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DatosIniciales.class);

    private final ParametrosAcademicosServiceAPI parametrosAcademicosService;
    private final LimiteInventarioAulaRepository limiteInventarioAulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceAPI usuarioService;
    private final String adminUsername;
    private final String adminCorreo;
    private final String adminPassword;

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
