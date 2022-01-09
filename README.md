# SQL

**Autor:** David Álvarez Castro

**Fecha:** 15/12/2021

**Asignatura:** ARQUITECTURA Y SERVICIOS DE INTERNET

---

Proyecto Maven para la gestión de inscripciones y puntuaciones de las actividades en un gimnasio desarrollado haciendo uso de JSF.

## GymJSF

Se explican brevemente algunos de los ficheros más destacados que forman la aplicación:

### **Vista**

- ***login.xhtml:*** formulario para insertar usuario y contraseña y permitir llamar al servlet de *login*
- ***home.xhtml:*** visualización del listado de actividades y posibilidad de visualizar y/o inscribirse en las actividades.
- ***menu.xhtml:*** menú superior con los tres botones para redirigir al usuario al `home`, registrar nuevos usuarios y hacer logout.
- ***activity.xhtml:*** vista con los detalles de la actividad. Esta vista hace uso de las vistas `files.xhtml` y la de `voting.xhtml`.
- ***files.xhtml:*** tabla con el listado de ficheros asociado a una actividad y la posibilidad de añadir nuevos ficheros (modal).
- ***voting.xhtml:*** formulario para puntuar una actividad.
- ***participate.xhtml:*** formulario para inscribirse en una actividad.
- ***users/registrar.xhtml:*** formulario para insertar los nuevos datos de un usuarios.
- ***css/:*** carpeta formada por algunos archivos css organizados con los estilos de la aplicación (muy simples) para el menú, el modal, botones, etc.
- ***js/:*** carpeta formada por un archivo javascript con una serie de funciones de apoyo (gestión del texto en el menú superior o la gestión del modal para subir ficheros).

### **JAVA (Modelos y Controladores)**

En este apartado se describen y listan algunos de los archivos `java` utilizados:

#### **Controladores**

Listado de los controladores empleados en la aplicación.

- ***ActivityController:*** bean encargado de mostrar el listado de actividades en la vista correspondiente, controlar si es posible votar/participar para mostrar los botones correspondientes y llevar a cabo la navegación desde la vista `home`.
- ***LoginController:*** bean encargado de gestionar el login/logout de la aplicación a través del almacenamiento del usuario como un atributo de la sesión.
- ***VoteController:*** bean encargado del registro de la votación de un usuario a una actividad a través de los datos del formulario.
- ***ParticipateController:*** bean encargado de la inscripción de un usuario a una actividad.
- ***FileController:*** bean encargado de llevar a cabo la descarga de ficheros desde el servidor y subir el listado de ficheros asociados a una actividad.
- ***UserController:*** bean encargado del registro del nuevo usuario en la base de datos (*UserDAO*) a través de los datos del formulario.

#### **DAO**

Clases encargadas de llevar a cabo el acceso a las tablas de la base de datos (servicios de acceso a las tablas).

- ***ActivityDAO:*** funciones de acceso a la tabla de actividades de la base de datos (crear/actualizar actividad, listar actividades, etc).
- ***FileDAO:*** funciones de acceso a la tabla de ficheros de la base de datos (crear fichero, lista de ficheros por actividad, etc).
- ***UserDAO:*** funciones de acceso a la tabla de usuarios de la base de datos (registrar nuevo usuario o verificar credenciales).
- ***ParticipationDAO:*** funciones de acceso a la tabla de participaciones de la base de datos (registrar/editar participaciones u obtener el listado de participaciones de un usuario).

#### **Filters**

Se define un filtro de URL basado en la sesión del request para verificar si el usuario se encuentra o no logueado en la aplicación.

#### **Model**

- ***ActivityBean:*** clase java formada por los atributos, getters y setter asociados a la tabla de actividades de la base de datos.
- ***FileBean:*** clase java formada por los atributos, getters y setter asociados a la tabla de ficheros de la base de datos.
- ***UserBean:*** clase java formada por los atributos, getters y setter asociados a la tabla de usuarios de la base de datos.
- ***ParticipationBean:*** clase java formada por los atributos, getters y setter asociados a la tabla de participaciones de la base de datos.

#### **Utils**

Listado con archivos de utilidades utilizados en la aplicación.

- ***FileSystem:*** funciones para gestionar el acceso al directorio de ficheros: añadir/eliminar fichero, eliminar directorio o comprobar la existencia de un fichero son algunas de las funciones implementadas.
- ***MultipartForm:*** funciones para gestionar la obtención de valores de los formularios (código de ejemplo del aula virtual).
- ***Session:*** funciones para gestionar la creación de sesiones.
- ***Properties:*** clase con la función encargada de obtener los textos en español por clave del archivo `properties`.

## **Consideraciones**

Para llevar a cabo esta práctica se han tenido en cuenta una serie de consideraciones/decisiones. Se muestran algunas de ellas a continuación:

- se considera que es necesario impedir el acceso a las URLs de la app a usuarios no registrados,
- se permiten registrar nuevos usuarios,
- solo se puede participar en una actividad no finalizada (fecha de fin sea inferior al día actual),
- solo se puede votar a actividades en las que has participado per hayan finalizado,
- se puede subir ficheros en cualquier actividad en la que has participado,
  - los usuarios con el perfil de administradores pueden subir ficheros a cualquier actividad
- se permite subir más de un fichero al mismo tiempo en la vista de *Detalle de Actividad*,
- se utiliza el nombre del fichero como campo título (a diferencia de la P1) para simplificar la gestión de ficheros,
- no se ha priorizado el uso de estilos para la obtención de una aplicación final "bonita"

## **Deploy**

1. En primer lugar es necesario contar con una base de datos MySQL corriendo (en mi caso lo hice a través de un contenedor Docker).
2. El segundo paso es disponer de una instancia de Glassfish funcionando en el sistema.
3. El siguiente paso es configurar un servidor web de aplicaciones; se configura un servidor Glassfish siguiendo los pasos descritos en los guiones de prácticas anteriores y crear el pool de conexiones junto con el recurso JDBC (*jdbc/actividades*).
4. Obtención del archivo `.war`. En la raíz del proyecto se ejecuta `mvn clean` para limpiar los ficheros compilados/generados. A continuación se ejecuta `mvn package` para generar el paquete `.war`. En este punto hay dos opciones:
    - acceder a la web de administración de Glassfish y desplegar el `war` desde la ventana de `Applications`, o
    - ejecutar `mvn glassfish:deploy` (plugin en el `pom.xml`) para llevar el despliegue de forma automática.
5. Para finalizar, acceder a <http://localhost:8080/socios/> actividades para entrar en la aplicación de gestión de actividades.
