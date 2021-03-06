/*
 * /*******************************************************************************
 *  * Copyright (c) 2016 Cognitiva Brasil - Tecnologias educacionais.
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available either under the terms of the GNU Public License v3
 *  * which accompanies this distribution, and is available at
 *  * http://www.gnu.org/licenses/gpl.html or for any other uses contact 
 *  * contato@cognitivabrasil.com.br for information.
 *  ******************************************************************************/

package com.cognitivabrasil.repositorio.data.repositories;

import com.cognitivabrasil.repositorio.data.entities.Files;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Marcos Freitas Nunes <marcosn@gmail.com>
 */
public interface FileRepository extends JpaRepository<Files, Integer> {

    @Override
    public List<Files> findAll();

}
