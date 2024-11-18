package apifestivos.apifestivos.core.interfaces.servicios;

import java.time.LocalDate;

public interface IFestivoServicio {

    public String verificarFecha(int año, int mes, int dia);

    public boolean esFechaValida(String strFecha);

    public boolean esFestivo(LocalDate fecha);

    public LocalDate calcularDomingoPascua(int año);

}
