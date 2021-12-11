package es.ubu.asi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import es.ubu.asi.dao.FileDAO;
import es.ubu.asi.model.FileBean;
import es.ubu.asi.utils.FileSystem;
import es.ubu.asi.utils.MultipartForm;
import es.ubu.asi.utils.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;


/**
 * @author david {dac1005@alu.ubu.es}
 *
 * File Controller
 */
@ManagedBean(name="fileController")
@RequestScoped
public class FileController implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String root = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/"); // root path

	// corregir problema con la lista de parts
	// http://omnifaces-fans.blogspot.com/2015/02/jsf-22-multiple-file-upload-with-html-5.html
	// private List<Part> files;
	private Part file;
	private String msg;
	private String error;
	private Properties p = new Properties();
	
	@ManagedProperty(value="#{activityController}")
    private ActivityController activityController;  // propiedad inyectada para acceder al controlador de la actividad
	@ManagedProperty(value="#{loginController}")
    private LoginController loginController;  // propiedad inyectada para acceder al controlador de login

	// contructor
	public FileController() {
		super();
	}

	// getter & setters
	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getErrors() {
		return error;
	}
	
	public String getSuccess() {
		return msg;
	}
	
	public ActivityController getActivityController() {
		return activityController;
	}

	public void setActivityController(ActivityController activityController) {
		this.activityController = activityController;
	}
	
	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	// methods
	/**
	 * Función para solucionar el problema con los inputFiles sin tener que generar un renderer
	 *
	 * A partir de un Part obtiene el listado completo de Part
	 */
	public Collection<Part> getAllParts(Part part) throws ServletException, IOException {
	    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
	}
	
	/**
	 * Permite descargar un fichero del servidor
	 *
	 * @return jsf
	 */
	public String upload() {
		try {
			/**
			 * Debido a una limitación del <h:inputFile>, es necesario obtener el listado de imágenes
			 * accediendo a la request del contexto del bean.
			 * 
			 * Véase http://ostack.cn/?qa=1043150/
			 */
			Collection<Part> parts = getAllParts(file);
			long idActivity = this.activityController.getActivity().getId();
			HashMap<String, Part> files = new HashMap<>();

			// recorremos las múltiples partes para quedarnos con los ficheros
			for (Part part : parts)
			{
				String filename = MultipartForm.obtenerNombreFichero(part);
				if (filename != null) {
					files.put(filename, part);
				}
			}

			// se recorre el haspmap con los datos de los ficheros adjuntos
            for(String filename : files.keySet())
            {
            	Part part = files.get(filename);
				InputStream is = part.getInputStream();
				String path = FileSystem.generatePathFiles(root, idActivity);
				boolean updateFile = FileSystem.existsFile(path + "/" + filename); // se comprueba si existe el fichero en el directorio (actualizar información)

				// en caso de subir el fichero correctamente, lo añadimos a la BD (si ya existía, lo reemplaza)
				if (FileSystem.addFile(path, filename, is)) {
					if (updateFile) { // por si el usuario sube un fichero con el mismo nombre
						FileBean fileUpdate = null;
						// si hay algún fallo al actualizar un fichero en la base de datos, no es necesario eliminar toda la actividad, solo ese fichero
						try {
							String newRoute = (path + "/" + filename).replaceAll(root, "");
							fileUpdate = FileDAO.getFileByRoute(newRoute);
							fileUpdate.setRoute(newRoute);
							fileUpdate.setTitle(filename);
							FileDAO.update(fileUpdate);
						} catch (Exception e) {
							// en caso de error, se elimina el fichero para que se vuelva a subir otra vez
							try {
								if (fileUpdate != null) {
									FileDAO.deleteFile(fileUpdate.getId());
									FileSystem.removeFile(path + "/" + filename);
								}
							} catch (Exception e2) {}

							throw new Exception("Error updating file " + filename, e);
						}
					} else {
						try {
							FileDAO.add(new FileBean(filename, (path + "/" + filename).replaceAll(root, ""), idActivity, this.loginController.getId()));
						} catch (Exception e) {
							// en caso de error, solo se lanza la excepción
							throw new Exception("Error uploading file " + filename, e);
						}
					}
				}
            }
            
            if (files.keySet().size() > 0) {
            	msg = String.format(p.getMsg("success_cargar_ficheros"), files.keySet().size(), idActivity);            	
            }
		} catch (Exception e) {
			error = p.getMsg("error_cargar_ficheros");
		}

		return "activity?faces-redirect";
    }

	/**
	 * Permite descargar un fichero del servidor
	 *
	 * @param file información del fichero
	 * @return jsf
	 */
	public String download(FileBean file) {
		String path = file.getRoute();
		String[] aux = path.split("/"); // obtenemos el nombre del fichero

		try {
			FileSystem.downloadFile(root + path, aux[aux.length - 1]);
		} catch (Exception e) {
			error = String.format(p.getMsg("error_descargar_fichero"), file.getTitle());
		}

		return "activity?faces-redirect";
    }
}
