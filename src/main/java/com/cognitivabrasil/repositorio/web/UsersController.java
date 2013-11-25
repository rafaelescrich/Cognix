/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cognitivabrasil.repositorio.web;

import com.cognitivabrasil.repositorio.web.validators.UserEditPasswordValidator;
import com.cognitivabrasil.repositorio.web.validators.UserEditValidator;
import com.cognitivabrasil.repositorio.web.validators.UserNewValidator;
import com.cognitivabrasil.repositorio.data.entities.User;
import com.cognitivabrasil.repositorio.services.UserService;
import com.cognitivabrasil.repositorio.util.Message;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Marcos Nunes <marcosn@gmail.com>
 */
@RequestMapping("/users")
@Controller
public class UsersController {

    private static Logger log = Logger.getLogger(UsersController.class);
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) {
        List<User> l = userService.getAll();
        model.addAttribute("users", l);
        model.addAttribute("total", l.size());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username

        model.addAttribute("username", name);
        model.addAttribute("manageUser", User.MANAGE_USER);
        model.addAttribute("userAdministrator", request.isUserInRole(User.MANAGE_USER));
        return "users/list";
    }

    @RequestMapping(value = "save", method = RequestMethod.GET)
    public String save(Model model) {
        UserDto u = new UserDto();
        model.addAttribute("userDto", u);
        model.addAttribute("roleList", User.getRoles());
        return "users/save";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(UserDto user, BindingResult bindingResult,
            ExtendedModelMap model, HttpServletResponse response) {
        UserNewValidator validator = new UserNewValidator(userService);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", user);
            response.setStatus(200);
            model.addAttribute("roleList", User.getRoles());
            return "users/save";
        }
        model.asMap().clear();
        userService.save(user.getUser());
        response.setStatus(201);
        return "ajax";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("id") int id, Model model) {
        User u = userService.get(id);
        UserDto userDto = new UserDto(u);
        model.addAttribute("userDto", userDto);
        model.addAttribute("roleList", User.getRoles());
        return "users/edit";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable("id") int id, UserDto user, BindingResult bindingResult,
            ExtendedModelMap model, HttpServletResponse response) {
        User u = userService.get(id);

        UserEditValidator validator = new UserEditValidator(userService);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", user);
            model.addAttribute("roleList", User.getRoles());
            response.setStatus(200);
            return "users/edit";
        }

        model.asMap().clear();
        userService.save(user.updateUser(u));
        response.setStatus(201);
        return "ajax";
    }

    @RequestMapping(value = "/{id}/editPass", method = RequestMethod.GET)
    public String editPass(@PathVariable("id") int id, Model model, HttpServletResponse response) throws IOException {
        User u = userService.get(id);
        User uCurrent = getCurrentUser();
        if (!u.equals(uCurrent)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "ajax";
        }
        UserDto userDto = new UserDto(u);
        model.addAttribute("userDto", userDto);
        return "users/editPass";
    }

    @RequestMapping(value = "/{id}/editPass", method = RequestMethod.POST)
    public String editPass(@PathVariable("id") int id, UserDto uDto,
    @RequestParam(required=false) String currentPass,
            @RequestParam(required=false) String password,
            @RequestParam(required=false) String confirmPass,
    BindingResult bindingResult,
            ExtendedModelMap model, HttpServletResponse response) throws IOException{
        System.out.println("\n\n Edit password POST confirmPass"+ confirmPass+"\n\n");
        User u = userService.get(id);
        
        User uCurrent = getCurrentUser();
        if (!u.equals(uCurrent)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return "ajax";
        }
        
        uDto.setName(u.getName());
        uDto.setUsername(u.getUsername());
        uDto.setRole(u.getRole());

        UserEditPasswordValidator validator = new UserEditPasswordValidator(u, userService);
        validator.validate(uDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", uDto);
            response.setStatus(200);
            return "users/editPass";
        }

        model.asMap().clear();
        userService.save(uDto.updateUser(u));
        response.setStatus(201);
        return "ajax";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        Message msg;
        try {
            userService.delete(userService.get(id));
            msg = new Message(Message.SUCCESS, "Usuário excluido com sucesso");
        } catch (DataAccessException e) {
            msg = new Message(Message.ERROR, "Erro ao excluir o usuário");
            log.error("Erro ao excluir um usuário.", e);
        }
        return msg;
    }

    protected static User getCurrentUser() {
        User currentUser;
        try {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (NullPointerException e) {
            log.error("Não foi possível recuperar o usuário que está utilizando o sistema.", e);
            currentUser = null;
        }
        return currentUser;
    }
}
