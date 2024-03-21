package br.com.horario.service;

import java.util.List;

import br.com.horario.entity.DocenteEntity;

public interface DocenteService {

	String save(DocenteEntity docenteEntity) throws Exception;
	List<DocenteEntity> findAll();
	DocenteEntity getOneByIdDocente (Long idDocente) throws Exception; 
	
	//Começa a exclusão
	String deleteById(Long idDocente) throws Exception;
	//Termina a exclusão
	
	
}
