package cc.mrbird.febs.common.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import cc.mrbird.febs.common.configure.GlobalConfig;
import cc.mrbird.febs.common.exception.FebsException;

public class Tools {
	
	public static Boolean isImageFile(String fileName) {
		String[] img_type = new String[] { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
		if (fileName == null) {
			return false;
		}
		fileName = fileName.toLowerCase();
		for (String type : img_type) {
			if (fileName.endsWith(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件后缀名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (fileName != null && fileName.indexOf(".") >= 0) {
			return fileName.substring(fileName.lastIndexOf("."), fileName.length());
		}
		return "";
	}
	
	public static boolean deleteFile(String path) {
		if(path.length() <= 6)
			return false;
		String filePath = GlobalConfig.uploadBasePath + path.substring(6);
        File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

	public static List<String> saveFiles(MultipartFile[] files, String folder) throws FebsException {
		try {
			String uploadPath = GlobalConfig.uploadBasePath + folder+"/";
			File uploadDirectory = new File(uploadPath);
			if (uploadDirectory.exists()) {
				if (!uploadDirectory.isDirectory()) {
					uploadDirectory.delete();
				}
			} else {
				uploadDirectory.mkdir();
			}
			List<String> imageList = new ArrayList<>();
			// 这里可以支持多文件上传
			if (files != null && files.length >= 1) {
				for (MultipartFile item : files) {
					String fileName = item.getOriginalFilename();
					// 判断是否有文件且是否为图片文件
					if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && Tools.isImageFile(fileName)) {
						// 创建输出文件对象
						long imageId = IdWorker.getId();
						File outFile = new File(uploadPath + imageId + Tools.getFileType(fileName));
						// 拷贝文件到输出文件对象
						FileUtils.copyInputStreamToFile(item.getInputStream(), outFile);
						imageList.add("/files/" + folder + "/" + outFile.getName());
					}
				}
			}
			return imageList;
		} catch (Exception e) {
			throw new FebsException("上传文件失败");
		}
	}

	public static String saveFile(MultipartFile file, String folder) throws FebsException {
		try {
			String uploadPath = GlobalConfig.uploadBasePath + folder + "/";
			File uploadDirectory = new File(uploadPath);
			if (uploadDirectory.exists()) {
				if (!uploadDirectory.isDirectory()) {
					uploadDirectory.delete();
				}
			} else {
				uploadDirectory.mkdir();
			}
			if (file != null) {
				String fileName = file.getOriginalFilename();
				if (fileName != null && !"".equalsIgnoreCase(fileName.trim())) {
					// 创建输出文件对象
					long id = IdWorker.getId();
					File outFile = new File(uploadPath  + id + Tools.getFileType(fileName));
					// 拷贝文件到输出文件对象
					FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
					return "/files/" + folder + "/" + outFile.getName();
				}

			}
			return null;
		} catch (Exception e) {
			throw new FebsException("上传文件失败");
		}
	}
}
