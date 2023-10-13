/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

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
}
