package cc.mrbird.febs.basicInfo.controller;

import cc.mrbird.febs.basicInfo.entity.Operate;
import cc.mrbird.febs.basicInfo.service.IOpertaeService;
import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.interceptor.Token;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.schema.Server;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author wsq
 */
@Configuration
@Slf4j
@RestController
@RequestMapping("operate")
public class OperateController extends BaseController {
    @Autowired
    private IOpertaeService opertaeService;

    @GetMapping("list")
    @RequiresPermissions("operate:view")
    public FebsResponse operateList(Operate operate, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.opertaeService.findOperate(request, operate));
        return new FebsResponse().success().data(dataTable);
    }

    @Log("新增Operate")
    @Token(remove = true)
    @PostMapping
    @RequiresPermissions("operate:add")
    public FebsResponse addArea(@Valid Operate operate) throws FebsException {
        try {
            operate.setUploadTime(new Date());
            operate.setUpdateTime(operate.getUploadTime());
            this.opertaeService.createOperate(operate);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增Operate失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("删除Operate")
    @GetMapping("delete/{id}")
    @RequiresPermissions("operate:delete")
    public FebsResponse deleteOperate(@NotBlank(message = "{required}") @PathVariable String id) throws FebsException {
        try {
            this.opertaeService.deleteOperate(id);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除Operate失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @Log("修改Operate")
    @PostMapping("update")
    @RequiresPermissions("operate:update")
    public FebsResponse updateArea(Operate operate) throws FebsException {
        try {
            operate.setUpdateTime(new Date());
            this.opertaeService.updateOperate(operate);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改Operate失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("operate:export")
    public void export(QueryRequest queryRequest, Operate operate, HttpServletResponse response) throws FebsException {
        try {
            List<Operate> operates = this.opertaeService.findOperate(queryRequest, operate).getRecords();
            ExcelKit.$Export(Operate.class, response).downXlsx(operates, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @RequestMapping("upload")
    @ResponseBody
    public Map<String,Object> fileUpload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpSession session) throws IOException {

        Map map = new HashMap<String,Object>();

        if (file.isEmpty()) {
             map.put("mess","请选择上传文件");
        }

        String fileName = file.getOriginalFilename();
        String filePath = "/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            map.put("mess","上传成功");
            map.put("addressName",filePath+file.getOriginalFilename());
            log.info("上传成功");
        } catch (IOException e) {
            log.error(e.toString(), e);
            map.put("mess","上传失败");
        }
        return map;
    }

    @RequestMapping("download")
    public void downloadOne(HttpServletRequest req,HttpServletResponse response) throws IOException, FebsException {
        String address=req.getParameter("attachAddress");
        String fileName = new String(address.substring(address.lastIndexOf("/") + 1).getBytes("UTF-8"), "ISO-8859-1");
        ;//被下载文件的名称

        File file = new File(address);
        if (file.exists()) {
            response.encodeURL("UTF-8");
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
           PrintWriter outpw=response.getWriter();
           outpw.println("<script>");
           outpw.println("alert('download failed');window.history.back();");
           outpw.println("</script>");
        }
    }
}
