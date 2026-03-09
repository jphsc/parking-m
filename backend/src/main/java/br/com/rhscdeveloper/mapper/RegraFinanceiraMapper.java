package br.com.rhscdeveloper.mapper;

import org.mapstruct.Mapper;

import br.com.rhscdeveloper.bean.RegraFinanceiraBean;
import br.com.rhscdeveloper.dto.RegraFinancRespDTO;
import br.com.rhscdeveloper.model.RegraFinanceiraVO;

@Mapper(componentModel = "cdi") 
public interface RegraFinanceiraMapper {
	
	RegraFinancRespDTO voToDto(final RegraFinanceiraVO entity);
	
	RegraFinanceiraBean voToBean(final RegraFinanceiraVO entity);
	
	RegraFinancRespDTO beanToDto(final RegraFinanceiraBean bean);
	
}
