package com.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import com.pojo.Goods;

@Service
public interface GoodsImageService {
	
	public String getImageTag (HttpServletRequest request, String image);
	public String getImageTagAndLink (HttpServletRequest request, String image);
	public String getImageTagAndLink (HttpServletRequest request);
	
	public boolean imageUploadDo (HttpServletRequest request);
	public Goods   imageDeleteDo (HttpServletRequest request);
}
