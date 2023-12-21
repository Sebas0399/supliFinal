/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Sebas
 */
public class StringUtils {
    public static String agregarGuiones(String numero) {
        StringBuilder stringBuilder = new StringBuilder(numero);
        
        // Insertar guiones después de los primeros tres dígitos y después de los siguientes tres dígitos.
        
        stringBuilder.insert(7, '-');
        
        return stringBuilder.toString();
    }
    public static String tranformarNombre(String nombre){
        if(nombre!=null){
            return nombre.substring(0,3).toUpperCase();
        }
        else{
            return null;
        }
    }
    public static String transformarFecha(Date date){

        // Convertir la cadena de fecha original a un objeto Date
        
        
        // Crear un nuevo formato para la fecha deseada
        SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yy");
        // Formatear la fecha en el nuevo formato
        String fechaFormateada = formatoDeseado.format(date);
        // Imprimir la fecha formateada
        return fechaFormateada;
    }
    public static String formatearFecha(String fecha){
        var f=fecha.split("/");
        
        System.out.println(Arrays.toString(f));
        var fin=f[2].trim().subSequence(2, 4);
        
        return f[0]+"/"+f[1]+"/"+fin;
    }
}
