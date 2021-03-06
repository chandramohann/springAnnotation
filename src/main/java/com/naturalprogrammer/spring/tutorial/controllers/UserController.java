package com.naturalprogrammer.spring.tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring.tutorial.domain.User;
import com.naturalprogrammer.spring.tutorial.services.UserService;
import com.naturalprogrammer.spring.tutorial.util.MyUtil;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;

    @RequestMapping(path="/{verificationCode}/verify", method=RequestMethod.GET)
    public String verify(@PathVariable String verificationCode,
    		RedirectAttributes redirectAttributes) {
    	
		userService.verify(verificationCode);
		MyUtil.flash(redirectAttributes, "success", "verificationSuccess");

		return "redirect:/";
    }
    
    @RequestMapping(value="/{id}/resend-verification-mail",
            method=RequestMethod.GET)
    public String resendVerificationMail(@PathVariable("id") User user,
    		RedirectAttributes redirectAttributes) {

    	userService.resendVerificationMail(user);
    	
    	MyUtil.flash(redirectAttributes,
    			"success", "verificationMailSent");
    	
    	return "redirect:/";
    }
    
    @RequestMapping(value = "/{userId}", method=RequestMethod.GET)
    public String getById(@PathVariable long userId, Model model) {
    	
    	model.addAttribute(userService.findById(userId));
	  	return "user";
    }
    
    @RequestMapping(value = "/{id}/edit", method=RequestMethod.GET)
    public String edit(@PathVariable("id") User user, Model model) {
    	
    	model.addAttribute(user);
		return "user-edit";
    }
    
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
	public String edit(@PathVariable("id") User user,
			@Validated(User.UpdateValidation.class) User updatedData,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors())
			return "user-edit";

		userService.update(user, updatedData);
		MyUtil.flash(redirectAttributes, "success", "editSuccess");

		return "redirect:/";
	}
}
