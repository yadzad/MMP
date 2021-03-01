package com.qi.demo.controller;

import com.qi.demo.DTO.ModelFileDTO;
import com.qi.demo.DTO.PointCloudFileDTO;
import com.qi.demo.VO.ResultVO;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.service.impl.ImageFileServiceImpl;
import com.qi.demo.service.impl.ModelFileServiceImpl;
import com.qi.demo.service.impl.ProjectServiceImpl;
import com.qi.demo.service.pointCloudHandle.PointCloudAlgorithm;
import com.qi.demo.utils.FileUtil;
import com.qi.demo.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequestMapping("/nr/model")
@Slf4j
public class ModelFileController {
    @Autowired
    ProjectServiceImpl projectService;

    @Autowired
    ModelFileServiceImpl modelFileService;

    @Autowired
    ImageFileServiceImpl imageFileService;

    PointCloudAlgorithm pointCloudAlgorithm = new PointCloudAlgorithm();
    private static final Logger logger = LoggerFactory.getLogger(ModelFileController.class);

    @PostMapping(value = "/upload")
    public ResultVO fileUpload(
            MultipartFile file,
            String documentsId,
            String projectId) {
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (file == null){
            log.error("[上传模型] 参数错误 file为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (documentsId == null) {
            log.error("[上传模型] 参数错误 documentsId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (projectId == null) {
            log.error("[上传模型] 参数错误 projectId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        //检查projectId
        if(projectService.findOneById(projectId) == null){
            return ResultVOUtil.error(ResultEnum.PROJECT_ID_NOT_EXISTED);
        }
        try {
//            //检查documentsId
//            if (!documentsId.equals(FileUtil.filePath2docPath(documentsId)+file.getOriginalFilename())){
//                return ResultVOUtil.error(ResultEnum.FILE_NAME_NOT_MATCH_WITH_PATH);
//            }

            //尝试新建这个文件
            FileUtil.createFile(documentsId);
            byte[] bytes = file.getBytes();
            //windows
//            Path path = Paths.get(FileUtil.filePath2docPath(documentsId)+file.getOriginalFilename());
            //linux
            Path path = Paths.get(documentsId);
            Files.write(path,bytes);
            //检查数据库是否存在该DocId
            ModelFileDTO result = modelFileService.findModelByDocId(documentsId);
            if (result == null){
                //不存在则最后写入数据库
                ModelFileDTO saveResult = modelFileService.saveModel(documentsId,projectId);
                return ResultVOUtil.success(saveResult);
            }
            //记录结束时间
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            return ResultVOUtil.successWithRunTIme(result, runTime);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.UPLOAD_PATH_ILLEGAL);
        }
    }


    @RequestMapping("/download")
    public Object downloadFile(@RequestParam String modelId,
                               final HttpServletResponse response, final HttpServletRequest request) throws Exception {
        if (modelId == null) {
            log.error("[下载模型] 参数错误 modelId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String modelPath = modelFileService.findModel(modelId);
        //判断ID是否存在
        if(modelPath == null){
            return ResultVOUtil.error(ResultEnum.MODEL_FILE_NOT_FOUND);
        }
        //判断该文件在服务器上是否存在
        File f = new File(modelPath);
        if (!f.exists()){
            return ResultVOUtil.error(ResultEnum.FILE_NOT_EXIST_IN_SERVICE);
        }

        String pathAfterSplit[] = modelPath.split("\\\\");
        String fileName = pathAfterSplit[pathAfterSplit.length-1];

        OutputStream os = null;
        InputStream is= null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/x-download;charset=GBK");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "iso-8859-1"));
            //读取流
            is = new FileInputStream(f);
            if (is == null) {
                logger.error("下载附件失败，请检查模型文件目录“" + fileName + "”是否存在");
                return ResultVOUtil.error(1,"下载附件失败，请检查模型文件目录“" + fileName + "”是否存在");
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            return ResultVOUtil.error(2,"下载附件失败,error:"+e.getMessage());
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("/delete")
    public ResultVO delete(@RequestParam(value = "modelId") String modelId) throws Exception{

        //记录开始时间
        long startTime = System.currentTimeMillis();
        if (modelId == null) {
            log.error("[删除模型] 参数错误 modelId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        //检查该id是否存在
        if (modelFileService.findModel(modelId) ==null){
            return ResultVOUtil.error(ResultEnum.MODEL_FILE_NOT_FOUND);
        }
        //删除操作
        if (modelFileService.delete(modelId) == true){
            //记录结束时间
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            return ResultVOUtil.successWithRunTIme(null, runTime);
        }
        return ResultVOUtil.error(ResultEnum.MODEL_FILE_NOT_FOUND);
    }

    @PostMapping("list")
    public ResultVO list(@RequestParam(value = "projectId") String projectId){
        //记录开始时间
        long startTime = System.currentTimeMillis();
        if (projectId == null) {
            log.error("[找寻所有模型] 参数错误 projectId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        //检查projectId是否存在
        if (projectService.findOneById(projectId) == null){
            return ResultVOUtil.error(ResultEnum.PROJECT_ID_NOT_EXISTED);
        }

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        //调用service层方法返回list
        return ResultVOUtil.successWithRunTIme(modelFileService.findAllModel(projectId), runTime);
    }

    /***
     * 纹理映射
     * @param projectId
     * @param modelId
     * @param imageId
     * @return
     */
    @PostMapping("/texture/textureMappingService")
    public ResultVO textureMappingService(@RequestParam(value = "projectId") String projectId,
                                          @RequestParam(value = "modelId") String modelId,
                                          @RequestParam(value = "imageId")String imageId
    ){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null || modelId == null || imageId == null){
            log.error("[纹理映射] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }

        String model_path = modelFileService.findModel(modelId);
        String image_path = imageFileService.findImage(imageId);
        String newPath = pointCloudAlgorithm.textureMappingService(model_path,image_path);
        ModelFileDTO modelFileDTO = modelFileService.saveModel(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(modelFileDTO, runTime);
    }

    @PostMapping("/texture/textureMultiMappingService")
    public ResultVO textureMultiMappingService(@RequestParam(value = "projectId") String projectId,
                                          @RequestParam(value = "modelId") String modelId,
                                          @RequestParam(value = "imageId")String imageId
    ){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null || modelId == null || imageId == null){
            log.error("[纹理映射] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String image_folder_path = "/home/nsy/necrms/软件后端/Mydemo01/zed/";
        String model_path = modelFileService.findModel(modelId);
        String pos_path = imageFileService.findImage(imageId);
        String newPath = pointCloudAlgorithm.textureMultiMappingService(model_path,pos_path,image_folder_path);
        ModelFileDTO modelFileDTO = modelFileService.saveModel(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(modelFileDTO, runTime);
    }
}
