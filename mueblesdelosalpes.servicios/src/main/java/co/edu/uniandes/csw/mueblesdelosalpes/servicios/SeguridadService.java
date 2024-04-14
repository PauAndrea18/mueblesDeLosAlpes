/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.uniandes.csw.mueblesdelosalpes.servicios;

import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.excepciones.AutenticacionException;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.ejb.ServicioSeguridadMock;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioSeguridadMockLocal;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Paula Gomez
 */

@Path("/Sesion")
@Stateful
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeguridadService {
    
    /**
     * Referencia al Ejb de la seguridad encargada de realizar las operaciones del mismo.
     */
    @EJB
    private IServicioSeguridadMockLocal seguridadEjb = new ServicioSeguridadMock();
    
    @PUT
    @Path("iniciarSesion/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Usuario iniciarSesion (@PathParam ("username") String username, String contrasena) throws AutenticacionException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(contrasena);
        String contraseña = rootNode.path("contrasena").asText();
        return seguridadEjb.ingresar(username, contraseña);
    }
    
    @PUT
    @Path("cerrarSesion/{username}")
    public Usuario salirSesion (@PathParam ("username") String username) throws AutenticacionException {
        return seguridadEjb.salir(username);
    }
    
    @GET
    @Path("usuarioActivo/{username}")
    public boolean confirmarSesionUsuario (@PathParam ("username") String username) throws AutenticacionException {
        return seguridadEjb.usuarioEnSesion(username);
    }
}
