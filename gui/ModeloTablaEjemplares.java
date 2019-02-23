/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;
import aplicacion.Ejemplar;
import javax.swing.table.*;
import aplicacion.Prestamo;
/**
 *
 * @author basesdatos
 */
public class ModeloTablaEjemplares extends AbstractTableModel{
    
    private java.util.List<Ejemplar> ejemplares;
    private java.util.HashMap<Integer,Prestamo> prestamos;
    private baseDatos.FachadaBaseDatos fbd;

    public ModeloTablaEjemplares(baseDatos.FachadaBaseDatos fbd){
        this.ejemplares=new java.util.ArrayList<Ejemplar>();
        this.prestamos = new java.util.HashMap<Integer, Prestamo>();
        this.fbd = fbd;
    }

    public int getColumnCount (){
        return 6;
    }

    public int getRowCount(){
        return ejemplares.size();
    }

    @Override
    public String getColumnName(int col){
        String nombre="";

        switch (col){
            case 0: nombre= "Id"; break;
            case 1: nombre= "Localizador"; break;
            case 2: nombre= "Año de compra"; break;
            case 3: nombre = "Usuario"; break;
            case 4: nombre = "Fecha"; break;
            case 5: nombre = "Vence";break;
        }
        return nombre;
    }

    @Override
    public Class getColumnClass(int col){
        Class clase=null;

        switch (col){
            case 0: clase= java.lang.Integer.class; break;
            case 1: clase= java.lang.String.class; break;
            case 2: clase=java.lang.String.class; break;
            case 3: clase=java.lang.String.class; break;
            case 4: clase=java.lang.String.class; break;
            case 5: clase=java.lang.String.class; break;
        }
        return clase;
    }

    @Override
    public boolean isCellEditable(int row, int col){
       return col>0;
    }

    public Object getValueAt(int row, int col){
        Object resultado=null;

        switch (col){
            case 0: resultado= ejemplares.get(row).getNumEjemplar(); break;
            case 1: resultado= ejemplares.get(row).getLocalizador(); break;
            case 2: resultado=ejemplares.get(row).getAnoCompra();break;
            case 3: resultado = prestamos.get(ejemplares.get(row).getNumEjemplar()).getUsuario();break;
            case 4: resultado = prestamos.get(ejemplares.get(row).getNumEjemplar()).getFechaPrestamo();break;
            case 5: resultado = prestamos.get(ejemplares.get(row).getNumEjemplar()).getFechaDevolucion();break;
        }
        return resultado;
    }

    @Override
    public void setValueAt(Object v, int row, int col){
        switch (col){
            case 1: ejemplares.get(row).setLocalizador((String) v); break;
            case 2: ejemplares.get(row).setAnoCompra((String) v);break;
        }
    }

    public void setFilas(java.util.List<Ejemplar> ejemplares){
        this.ejemplares=ejemplares;
        for(Ejemplar e: this.ejemplares){
            this.prestamos.put(e.getNumEjemplar(), this.fbd.consultarPrestamos(e.getLibro().getIdLibro(),e.getNumEjemplar()));
        }
        fireTableDataChanged();
    }

    public void nuevoEjemplar(Ejemplar e){
        this.ejemplares.add(e);
        fireTableRowsInserted(this.ejemplares.size()-1, this.ejemplares.size()-1);
    }

    public void borrarEjemplar(int indice){
        this.ejemplares.remove(indice);
        fireTableRowsDeleted(indice, indice);
    }

    public java.util.List<Ejemplar> getFilas(){
        return this.ejemplares;
    }

    public Ejemplar obtenerEjemplar(int i){
        return this.ejemplares.get(i);
    }
}
