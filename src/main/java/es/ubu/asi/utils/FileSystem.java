/**
 * 
 */
package es.ubu.asi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.asi.Globals;
import es.ubu.asi.database.Database;

/**
 * @author david {dac1005@alu.ubu.es}
 */
public class FileSystem {
	private static Logger logger = LoggerFactory.getLogger(Database.class);
	
	
	/**
	 * Permite generar el path absoluto a partir del path de la appy de la actividad
	 *
	 * @param servletPath
	 * @param idActivity
	 * @return path absoluto al directorio de ficheros
	 */
	public static String generatePathFiles(String servletPath, long idActivity) {
		return servletPath + Globals.PATH_FILES_ACTIVITIES + "/" + idActivity;
	}

	/**
	 * Permite anhadir un fichero a una ruta determinada
	 *
	 * @param path ruta base
	 * @param filename nombre del nuevo fichero
	 * @param is
	 * @return si se ha creado o no
	 */
	public static boolean addFile (String path, String filename, InputStream is) {
        try {
        	File directory = new File(path);
            if (!directory.exists()){
            	directory.mkdirs();
            }

        	int i = is.available();
            byte[] b = new byte[i];
            is.read(b);

            FileOutputStream os = new FileOutputStream(path + "/" + filename);
            os.write(b);
            is.close();    
            os.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

        return true;
	}
	
	/**
	 * Permite comprobar si un fichero existe en el directorio
	 *
	 * @param filename ruta completa del fichero
	 * @return si sexiste o no
	 */
	public static boolean existsFile (String filename) {
		File directory = new File(filename);
        return directory.exists();
	}

	/**
	 * Permite eliminar un fichero
	 *
	 * @param filename ruta completa del fichero
	 * @return si se ha eliminado o no
	 */
	public static boolean removeFile (String filename) {
        try {
            File file = new File(filename);
            return file.delete();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * Permite eliminar un directorio completo
	 *
	 * @param path ruta
	 * @return si se ha eliminado o no
	 */
	public static boolean removePath (String path) {
        try {
            File file = new File(path);
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                	removePath(f.getPath());
                }
            }
            return file.delete();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Permite descargar un fichero dado una ruta
	 * 
	 * - Código obtenido de Stackoverflow
	 *
	 * @param path ruta completa del fichero
	 * @param name nombre del fichero
	 * @return si se ha descargado correctamente o no
	 */
	public static boolean downloadFile (String path, String nombre) {
		ServletOutputStream out = null;
		FileInputStream input = null;

		try {
			File file = new File(path);

			// obtenemos un objeto de tipo respuesta donde cargar la información del fichero
	        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	        response.setHeader("Content-Disposition", "attachment;filename=" + nombre);
	        response.setContentLength((int) file.length());
            input = new FileInputStream(file);
            
            // cargamos los datos del fichero
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            while (input.read(buffer) != -1) {
                out.write(buffer);
                out.flush();
            }

            // completamos la respuesta para permitir recibirla en el cliente (descargar fichero)
            FacesContext.getCurrentInstance().getResponseComplete();
            return true;
		} catch (Exception err) {
            err.printStackTrace();
            return false;
        } finally {
            try {
            	out.close();
            	input.close();
            } catch (IOException err) {
                return false;
            }
        }
	}
}
