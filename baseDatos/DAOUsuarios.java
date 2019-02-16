/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package baseDatos;
import aplicacion.Libro;
import aplicacion.Usuario;
import aplicacion.TipoUsuario;
import java.sql.*;
/**
 *
 * @author basesdatos
 */
public class DAOUsuarios extends AbstractDAO {

   public DAOUsuarios (Connection conexion, aplicacion.FachadaAplicacion fa){
        super.setConexion(conexion);
        super.setFachadaAplicacion(fa);
    }

    public Usuario validarUsuario(String idUsuario, String clave){
        Usuario resultado=null;
        Connection con;
        PreparedStatement stmUsuario=null;
        ResultSet rsUsuario;

        con=this.getConexion();

        try {
        stmUsuario=con.prepareStatement("select id_usuario, clave, nombre, direccion, email, tipo_usuario "+
                                        "from usuario "+
                                        "where id_usuario = ? and clave = ?");
        stmUsuario.setString(1, idUsuario);
        stmUsuario.setString(2, clave);
        rsUsuario=stmUsuario.executeQuery();
        if (rsUsuario.next())
        {
            resultado = new Usuario(rsUsuario.getString("id_usuario"), rsUsuario.getString("clave"),
                                      rsUsuario.getString("nombre"), rsUsuario.getString("direccion"),
                                      rsUsuario.getString("email"), TipoUsuario.valueOf(rsUsuario.getString("tipo_usuario")));

        }
        } catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmUsuario.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
        return resultado;
    }

    public String insertarUsuario(Usuario usuario){
        
        Connection con;
        PreparedStatement stmUsuario=null;
        PreparedStatement stmIdUsuario=null;
        ResultSet rsIdUsuario;

        String idUsuario=null;

        con=super.getConexion();
        
        try {
            stmUsuario=con.prepareStatement("insert into usuario(id_usuario, nombre, clave, email, direccion, tipo_usuario) "+
                                          "values (?,?,?,?,?,?)");
            stmUsuario.setString(1, usuario.getIdUsuario());
            stmUsuario.setString(2, usuario.getNombre());
            stmUsuario.setString(3, usuario.getClave());
            stmUsuario.setString(4, usuario.getEmail() );
            stmUsuario.setString(5, usuario.getDireccion());
            stmUsuario.setString(6, usuario.getStringTipoUsuario());
            stmUsuario.executeUpdate();

            try{
            stmIdUsuario=con.prepareStatement("select currval('seq_usuario_id_usuario') as id_usuario");
            rsIdUsuario=stmIdUsuario.executeQuery();
            rsIdUsuario.next();
            idUsuario=rsIdUsuario.getString("id_usuario");
            } catch (SQLException e){
              System.out.println(e.getMessage());
            }finally{stmIdUsuario.close();}
             
        }catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmUsuario.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
        
        return idUsuario;
    }
    
    public java.util.List<Usuario> consultarUsuarios(String id, String nombre){
        java.util.List<Usuario> resultado = new java.util.ArrayList<Usuario>();
        Usuario usuarioActual;
        Connection con;
        PreparedStatement stmUsuarios = null;
        ResultSet rsUsuarios;
        
        con = this.getConexion();
        
        String consulta = "select id_usuario, nombre, clave, email, direccion, tipo_usuario " + 
                                "from usuario as u " +
                                "where id_usuario like ? " +
                                " and nombre like ?";

        try{
            stmUsuarios = con.prepareStatement(consulta);
            stmUsuarios.setString(1, "%"+id+"%");
            stmUsuarios.setString(2, "%"+nombre+"%");
            rsUsuarios = stmUsuarios.executeQuery();
            
            while (rsUsuarios.next()){
                usuarioActual = new Usuario(rsUsuarios.getString("id_usuario"),rsUsuarios.getString("nombre"),
                rsUsuarios.getString("clave"),rsUsuarios.getString("email"),rsUsuarios.getString("direccion"),TipoUsuario.Normal);
                resultado.add(usuarioActual);
            }
        }
        catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmUsuarios.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
        return resultado;
    }
    
    public boolean borrarUsuario(String id){
        
        Connection con;
        PreparedStatement stmUsuario=null;
        PreparedStatement stmBorrarUsuario=null;
        ResultSet rsUsuario;

        String idUsuario=null;

        con=super.getConexion();
        
        try{
            stmUsuario=con.prepareStatement("select * "+
                                        "from prestamo "+
                                        "where usuario = ? and fecha_devolucion IS NULL");
            stmUsuario.setString(1, id);
            rsUsuario=stmUsuario.executeQuery();
            if(rsUsuario.next()) return false;
            
            try{
                stmBorrarUsuario = con.prepareStatement("delete from usuario where id_usuario = ?");
                stmBorrarUsuario.setString(1, idUsuario);
                stmBorrarUsuario.executeQuery();
            }catch (SQLException e){
              System.out.println(e.getMessage());
            }finally{stmBorrarUsuario.close();}

        }catch (SQLException e){
          System.out.println(e.getMessage());
          this.getFachadaAplicacion().muestraExcepcion(e.getMessage());
        }finally{
          try {stmUsuario.close();} catch (SQLException e){System.out.println("Imposible cerrar cursores");}
        }
      
        return true;
    }
    
}