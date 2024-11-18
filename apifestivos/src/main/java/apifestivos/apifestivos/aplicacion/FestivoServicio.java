package apifestivos.apifestivos.aplicacion;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import apifestivos.apifestivos.core.interfaces.repositorios.IFestivoRepositorio;
import apifestivos.apifestivos.core.interfaces.servicios.IFestivoServicio;
import apifestivos.apifestivos.dominio.entidades.Festivo;

@Service
public class FestivoServicio implements IFestivoServicio {

    private final IFestivoRepositorio repositorio;

    public FestivoServicio(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public String verificarFecha(int año, int mes, int dia) {
        if (!esFechaValida(año, mes, dia)) {
            return "Fecha No válida";
        }

        LocalDate fecha = LocalDate.of(año, mes, dia);
        List<Festivo> festivos = repositorio.findByMesAndDia(mes, dia);

        for (Festivo festivo : festivos) {
            if (esFestivo(festivo, fecha)) {
                return "Es Festivo";
            }
        }
        return "No es festivo";
    }

    private boolean esFechaValida(int año, int mes, int dia) {
        try {
            LocalDate.of(año, mes, dia);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    private boolean esFestivo(List<Festivo> festivos, LocalDate fecha) {
        for (Festivo festivo : festivos) {
            if (esFestivo(festivo, fecha)) {
                return true;
            }
        }
        return false;
    }

    private boolean esFestivo(Festivo festivo, LocalDate fecha) {
        LocalDate fechaFestivo;
    
        switch (festivo.getTipo().getId()) {
            case 1:
                // Festivo fijo
                fechaFestivo = LocalDate.of(fecha.getYear(), festivo.getMes(), festivo.getDia());
                break;
    
            case 2:
                // Festivo con "puente festivo" (se mueve al lunes si no cae en lunes)
                fechaFestivo = LocalDate.of(fecha.getYear(), festivo.getMes(), festivo.getDia());
                if (fechaFestivo.getDayOfWeek() != DayOfWeek.MONDAY) {
                    fechaFestivo = fechaFestivo.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
                }
                break;
    
            case 3:
                // Basado en Pascua
                fechaFestivo = calcularDomingoPascua(fecha.getYear()).plusDays(festivo.getDiaspascua());
                break;
    
            case 4:
                // Basado en Pascua con "puente festivo" (se mueve al lunes si no cae en lunes)
                fechaFestivo = calcularDomingoPascua(fecha.getYear()).plusDays(festivo.getDiaspascua());
                if (fechaFestivo.getDayOfWeek() != DayOfWeek.MONDAY) {
                    fechaFestivo = fechaFestivo.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
                }
                break;
    
            default:
                // Tipo no reconocido
                return false;
        }
    
        return fecha.equals(fechaFestivo);
    }
    
    @Override
    public LocalDate calcularDomingoPascua(int año) {
        int a = año % 19;
        int b = año % 4;
        int c = año % 7;
        int d = (19 * a + 24) % 30;
        int e = (2 * b + 4 * c + 6 * d + 5) % 7;
        int dias = 22 + d + e;

        if (dias > 31) {
            return LocalDate.of(año, 4, dias - 31); // Abril
        } else {
            return LocalDate.of(año, 3, dias); // Marzo
        }
    }

    @Override
    public boolean esFechaValida(String strFecha) {
        try {
            LocalDate.parse(strFecha);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    @Override
    public boolean esFestivo(LocalDate fecha) {
        List<Festivo> festivos = repositorio.findAll();
        return esFestivo(festivos, fecha);
    }
}


