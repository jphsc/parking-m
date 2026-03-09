package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.bean.VeiculoBean;
import br.com.rhscdeveloper.dto.VeiculoRespDTO;
import br.com.rhscdeveloper.model.VeiculoVO;

@Mapper(componentModel = "cdi") 
public interface VeiculoMapper {

	VeiculoRespDTO voToDto(final VeiculoVO entity);
	
	VeiculoBean voToBean(final VeiculoVO entity);
	
	VeiculoRespDTO beanToDto(final VeiculoBean bean);
	
}
