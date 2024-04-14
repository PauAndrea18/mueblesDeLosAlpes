/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$ ServicioSeguridadMock.java
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 3.0
 *
 * Ejercicio: Muebles de los Alpes
 * Autor: Juan Sebastián Urrego
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb;

import co.edu.uniandes.csw.mueblesdelosalpes.persistencia.mock.ServicioPersistenciaMock;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioPersistenciaMockLocal;
import javax.ejb.Stateful;


/**
 * Clase que se encarga de la autenticación de un usuario en el sistema
 * @author Juan Sebastián Urrego
 */
@Stateful
public class ServicioSeguridadMock implements IServicioSeguridadMockLocal
{

    //-----------------------------------------------------------
    // Atributos
    //-----------------------------------------------------------
    
    /**
     * Interface con referencia al servicio de persistencia en el sistema
     */
    private IServicioPersistenciaMockLocal persistencia;

    //-----------------------------------------------------------
    // Métodos
    //-----------------------------------------------------------

    /**
     * Constructor sin argumentos de la clase
     */
    public ServicioSeguridadMock()
    {
        persistencia=new ServicioPersistenciaMock();
    }

    //-----------------------------------------------------------
    // Métodos
    //-----------------------------------------------------------

    /**
     * Registra el ingreso de un usuario al sistema.
     * @param nombre Login del usuario que quiere ingresar al sistema.
     * @param contraseña Contraseña del usuario que quiere ingresar al sistema.
     * @return usuario Retorna el objeto que contiene la información del usuario que ingreso al sistema.
     */
    @Override
    public Usuario ingresar(String nombre, String contraseña) throws AutenticacionException
    {
   
        Usuario u = (Usuario) persistencia.findById(Usuario.class, nombre);
        if (u != null)
        {
            u.setNombreCompleto(u.getContraseña()); 
            u.setSeleccion(true);
            if (u.getContraseña().equals(contraseña))
            {
                return u;
            } 
            else
            {
                throw new AutenticacionException("La contraseña no es válida. Por favor, asegúrate de que el bloqueo de mayúsculas no está activado e inténtalo de nuevo.");
            }
        } 
        else
        {
            throw new AutenticacionException("El nombre de usuario proporcionado no pertenece a ninguna cuenta.");
        }
    }
    
    /**
     * Registra la salida de un usuario del sistema.
     * @param nombre Login del usuario que quiere salir del sistema.
     * @return usuario Retorna el objeto que contiene la información del usuario que salio del sistema.
     */
    @Override
    public Usuario salir(String nombre) throws AutenticacionException {
        Usuario u = (Usuario) persistencia.findById(Usuario.class, nombre);
        if(u != null){
            if(u.isSeleccion()) {
                u.setSeleccion(false);
                return u;
            }
            else
                throw new AutenticacionException("El usuario proporcionado no se encuentra con inicio de sesión activa");
        }
        else
            throw new AutenticacionException("El nombre de usuario proporcionado no pertenece a ninguna cuenta.");       
    }
    
    @Override
    public boolean usuarioEnSesion(String username) throws AutenticacionException{
        Usuario u = (Usuario) persistencia.findById(Usuario.class, username);
        if(u != null){
            if(u.isSeleccion())
                return true;
            else
                return false;
        }
        else
            throw new AutenticacionException("El nombre de usuario proporcionado no pertenece a ninguna cuenta.");
    }
}
