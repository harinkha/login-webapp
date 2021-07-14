/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.muic.ooc.webapp.servlet;

import io.muic.ooc.webapp.Routable;
import io.muic.ooc.webapp.model.User;
import io.muic.ooc.webapp.service.SecurityService;
import io.muic.ooc.webapp.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author gigadot
 */
public class DeleteUserServlet extends HttpServlet implements Routable {

    private SecurityService securityService;


    @Override
    public String getMapping() {
        return "user/delete";
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = securityService.isAuthorized(request);
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            UserService userService=UserService.getInstance();

            try{
                User currentUser = userService.findByUsername(username);
                User deletingUser = userService.findByUsername(request.getParameter(username));
                if(userService.deleteUserByUsername(deletingUser.getUsername())){
                    request.getSession().setAttribute("haeError",false);
                    request.getSession().setAttribute("message",String.format("User %s is successfully deleted",deletingUser.getUsername()));
                }
                else{
                    request.getSession().setAttribute("haeError",true);
                    request.getSession().setAttribute("message",String.format("Unable to delete User %s.",deletingUser.getUsername()));

                }
            }catch (Exception e){
                request.getSession().setAttribute("haeError",true);
                request.getSession().setAttribute("message",String.format("User %s is successfully deleted",request.getParameter(username)));

            }

            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }
}
