package com.nnk.springboot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("app")
public class LoginController {

    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }

    @GetMapping("error")
    public ModelAndView error(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String errorMessage= "Vous n'avez pas les permissions nécessaires pour accéder cette ressource.";
        mav.addObject("errorMsg", errorMessage);
        mav.addObject("requestUri", request.getRequestURI());
        mav.setStatus(HttpStatus.FORBIDDEN);
        mav.setViewName("403");
        return mav;
    }
}
