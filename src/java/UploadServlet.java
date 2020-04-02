/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.oreilly.servlet.MultipartRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Fareza Adityanto
 */
public class UploadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
//        doPost(request,response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        response.setContentType("text/html");
        
        PrintWriter out = response.getWriter();
//        out.println("hello sayang");
        ArrayList<String> ls = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();
        String path = "/usr/local/tomcat9/webapps/netcen/tes_upload";
        File upload = new File(path);
        MultipartRequest m1;
        try{
            m1 = new MultipartRequest(request,upload.toString());
            Enumeration name = m1.getFileNames();
//            weka_nBayes ml = null;
            
            while(name.hasMoreElements()){
                String realname = (String) name.nextElement();
                ls.add(m1.getFilesystemName(realname));
            }
//            out.println(ls.get(0));
//            out.println(ls.get(1));
            
            if(FilenameUtils.getExtension(ls.get(0)).equalsIgnoreCase("csv") 
                && FilenameUtils.getExtension(ls.get(1)).equalsIgnoreCase("csv")){
                
                //isi array kalo csv: path,sourceTest,DestTest,sourceTrain,destTrain
            
                String input = path+","+ls.get(1)+",tes.arff,"+ls.get(0)
                        +",train.arff";
                res.addAll(weka_nBayes.getResult(input));                
            }else if(FilenameUtils.getExtension(ls.get(0)).equalsIgnoreCase("arff") 
                && FilenameUtils.getExtension(ls.get(1)).equalsIgnoreCase("arff")){
                
                //isi array kalo pake arff: path/TestFile,path/TrainFile
                String input = path+"/"+ls.get(1)+","+path+"/"+ls.get(0);
                res.addAll(weka_nBayes.getResult(input));
            }else{
                out.print("Tipe file tidak sesuai");
            }
            
            for(String a: res){
                out.println(a.substring(1, a.length()-2)+"<br/>");
            }
            
        }catch(IOException e){
//            out.println("Upload file gagal, mungkin terjadi error");
            out.println(e);
        } catch (Exception ex) {
            Logger.getLogger(UploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex);
        }
     
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
