package org.example.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ControllerUtil {
    public static void errorMessage(HttpServletResponse response,Exception ex){
        try{
            ex.printStackTrace();
            response.sendRedirect("error?msg="+ex.getMessage());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
