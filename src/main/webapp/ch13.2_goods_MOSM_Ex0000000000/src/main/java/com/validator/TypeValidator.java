package com.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.pojo.Type;

public class TypeValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		
		return Type.class.equals(arg0);			//只针对Type类的对象进行数据校验
	}
	
	@Override
	public void validate(Object object, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "typeName", 	null, "*分类名称不能为空<br>");
		
		Type type = (Type) object;
		
		String typeId 		= type.getTypeId();					//新添记录时，ID为null，所以这里不能有trim()方法
		String typeName 	= type.getTypeName().trim();
		String note			= type.getNote().trim();
		
		type.setTypeName(typeName);
		type.setNote(note);
		
		if (typeId != null) {									//编辑分类时才有参数typeId
			typeId = typeId.trim();
			type.setTypeId(typeId);
			
			try { 									 
				Integer.parseInt(typeId);					
			} catch (Exception e) {
				errors.rejectValue("typeId", null, "*参数typeId错误<br>");
			}
		}
		
		if (typeName.length() > 45) {
			errors.rejectValue("typeName", null, "*分类名称不能多于45个字符<br>");
		}
		
		if (note.length() > 2000) {
			errors.rejectValue("note", null, "*备注不能多于20000个字符<br>");
		}
	}
}