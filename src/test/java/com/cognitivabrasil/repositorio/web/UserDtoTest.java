/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cognitivabrasil.repositorio.web;

import com.cognitivabrasil.repositorio.data.entities.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class UserDtoTest {
    
    @Test
    public void basicTest(){
        UserDto u = new UserDto("marcos", "Marcos Nunes", "12345", 1, "admin");
        
        assertThat(u.getUsername(), equalTo("marcos"));
        assertThat(u.getName(), equalTo("Marcos Nunes"));
        assertThat(u.getPassword(), equalTo("12345"));
        assertThat(u.getRole(), equalTo("admin"));
        assertThat(u.getId(), equalTo(1));
        
    }
    
    @Test
    public void testSetters(){
        UserDto u = new UserDto();
        u.setId(1);
        u.setUsername("marcos");
        u.setName("Marcos Nunes");
        u.setPassword("12345");
        u.setRole("admin");
        u.setConfirmPass("12345");
        u.setCurrentPass("54321");
        
        assertThat(u.getId(), equalTo(1));
        assertThat(u.getUsername(), equalTo("marcos"));
        assertThat(u.getName(), equalTo("Marcos Nunes"));
        assertThat(u.getPassword(), equalTo("12345"));
        assertThat(u.getRole(), equalTo("admin"));
        assertThat(u.getConfirmPass(), equalTo("12345"));
        assertThat(u.getCurrentPass(), equalTo("54321"));
    }
    
    @Test
    public void testUpdateUser(){
        UserDto u = new UserDto();
        u.setId(1);
        u.setUsername("marcos");
        u.setName("Marcos Nunes");
        u.setPassword("12345");
        u.setRole("admin");
        u.setConfirmPass("12345");
        u.setCurrentPass("54321");
        
        User user = u.updateUser(null);
        
        assertThat(user.getUsername(), equalTo("marcos"));
        assertThat(user.getName(), equalTo("Marcos Nunes"));
        assertThat(user.getPassword(), equalTo("827ccb0eea8a706c4c34a16891f84e7b"));
        assertThat(user.getRole(), equalTo("admin"));
        
    }
    
}
