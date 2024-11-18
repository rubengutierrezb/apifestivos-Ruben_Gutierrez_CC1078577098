package apifestivos.apifestivos.presentacion;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apifestivos.apifestivos.aplicacion.FestivoServicio;
import apifestivos.apifestivos.core.interfaces.servicios.IFestivoServicio;

@RestController
@RequestMapping("/api/festivos")
public class FestivoControlador {

    private final IFestivoServicio servicio;

    public FestivoControlador(FestivoServicio servicio) {
        this.servicio = servicio;
    }

    @RequestMapping(value = "/verificarfecha/{año}/{mes}/{dia}")
    public String verificarFestivo(@PathVariable int año, @PathVariable int mes, @PathVariable int dia) {
        if (mes < 1 || mes > 12 || dia < 1 || dia > 31) {
            return "Fecha No válida";
        }

        LocalDate fecha = LocalDate.of(año, mes, dia);
        return servicio.esFestivo(fecha) ? "Es Festivo" : "No es festivo";
    }
}

