/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
 */ 
 
package com.infosys.aiauto.operationsportal.exception;

import com.infosys.aiauto.operationsportal.dto.ErrorDTO;

@SuppressWarnings("serial")
public class ValidationException extends Exception {

	private ErrorDTO errorDTO;
	
	public ValidationException(){
		super();
	}
	
	public ValidationException(ErrorDTO dto) {
		super();
		this.errorDTO = dto;
	}
	
	public ValidationException(int errorCode, String errorMessage) {
		super();
		errorDTO = new ErrorDTO();
		errorDTO.setCode(String.valueOf(errorCode));
		errorDTO.setDescription(errorMessage);
	}
	
	public ValidationException(String errorCode, String errorMessage) {
		super();
		errorDTO = new ErrorDTO();
		errorDTO.setCode(errorCode);
		errorDTO.setDescription(errorMessage);
	}

	public ErrorDTO getErrorDTO() {
		return errorDTO;
	}

	public void setErrorDTO(ErrorDTO dto) {
		this.errorDTO = dto;
	}
	
	public String getErrorCode(){
		return errorDTO != null ? errorDTO.getCode() : "";
	}
	
	public String getErrorMessage(){
		return errorDTO != null ? errorDTO.getDescription() : "";
	}
}
