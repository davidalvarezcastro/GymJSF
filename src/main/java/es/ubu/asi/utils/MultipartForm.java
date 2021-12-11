/**
 * 
 */
package es.ubu.asi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.Part;

/**
 * @author david {dac1005@alu.ubu.es}
 *
 */
public class MultipartForm {

	/**
     * Extrae el nombre del fichero de los datos de cabecera.
     * 
     * @param part
     *            fichero
     * @return nombre del fichero o nulo en su defecto
     */
    public static String obtenerNombreFichero(Part part) {
        String cabecera = part.getHeader("content-disposition");
        for (String cd : cabecera.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Recupera el valor del formulario si es de tipo texto.
     * 
     * @param part
     *            parte del formulario
     * @return texto extraído
     * @throws IOException
     *             si hay algún error en el manejo de los ficheros
     */
    public static String obtenerValor(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                part.getInputStream(), "UTF-8"));
        StringBuilder valor = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0;) {
            valor.append(buffer, 0, length);
        }
        return valor.toString();
    }
}
