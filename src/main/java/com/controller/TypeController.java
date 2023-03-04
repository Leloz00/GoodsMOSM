package com.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pojo.Type;
import com.service.TypeService;
import com.validator.TypeValidator;

@Controller
public class TypeController {
	
	@Autowired
	private TypeService  typeService;		
	
	
	@RequestMapping("/typeList")
	public String typeList (HttpServletRequest request) {
				
		typeService.queryAll(request);
		
		return "type/typeList.jsp";
	}
	
	
	@RequestMapping("/typeShow")
	public String typeShow (HttpServletRequest request) {
				
		typeService.queryByTypeId(request);
		
		Type type = (Type) request.getAttribute("type");
		
		if (type != null) {
			String note = type.getNote(); 
			note = note.replace("\n", "<br>");				//换行符替换
			type.setNote(note);
		
			request.setAttribute("type", type);				//****传递对象
		}
		
		return "type/typeShow.jsp";
	}
	
	
	@RequestMapping("/typeAdd")
	public String typeAdd (HttpServletRequest request) {
		
		return "type/typeAdd.jsp";
	}
	
	
	@InitBinder											//将首先执行的动作
	public void initBinder (DataBinder binder) {
		
		binder.setValidator(new TypeValidator());		//进行数据校验
	}
	
	
	@RequestMapping(value="/typeAddDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String typeAddDo (@Validated Type type, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		String typeId = typeService.addType(type, request);
		
		if (typeId.equals("0")) {									//新添失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:typeShow?typeId=" + typeId;				//新添成功
	}
	

	@RequestMapping("/typeEdit")
	public String typeEdit (HttpServletRequest request) {
				
		typeService.queryByTypeId(request);
		
		return "type/typeEdit.jsp";
	}	
	
	
	@RequestMapping(value="/typeEditDo", produces={"text/html;charset=UTF-8"})		//避免返回的中文内容为乱码
	@ResponseBody																	//返回数据给Ajax，而不是转发到网址
	public String typeEditDo (@Validated Type type, Errors errors, HttpServletRequest request) {
		
		String msg = "";
		
		if (errors.hasFieldErrors()) {								//如果校验时生成了错误信息
			List<ObjectError> errorList = errors.getAllErrors();
			
			for (ObjectError error : errorList) {
				msg += error.getDefaultMessage();					//该字段的错误信息
			}
			
			return msg;
		}
		
		boolean result = typeService.editType(type, request);
		
		if (result == false) {										//更新失败
			msg = (String) request.getAttribute("msg");
			return msg;
		}
		
		return "@Redirect:typeShow?typeId=" + type.getTypeId();		//更新成功
	}
	
	
	@RequestMapping("/typeDeleteDo")
	public String typeDeleteDo (HttpServletRequest request) {
				
		typeService.deleteByTypeId(request);
		
		return "redirect:typeList";									//重定向到类型列表页
	}
}
