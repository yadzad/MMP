package com.qi.demo.controller;

import com.qi.demo.DTO.ImageFileDTO;
import com.qi.demo.DTO.ModelFileDTO;
import com.qi.demo.DTO.PointCloudFileDTO;
import com.qi.demo.VO.ResultVO;
import com.qi.demo.enums.ResultEnum;
import com.qi.demo.service.impl.ImageFileServiceImpl;
import com.qi.demo.service.impl.ModelFileServiceImpl;
import com.qi.demo.service.impl.PointCloudFileServiceImpl;
import com.qi.demo.service.impl.ProjectServiceImpl;
import com.qi.demo.service.pointCloudHandle.PointCloudAlgorithm;
import com.qi.demo.service.pointCloudHandle.RegistrationAlgorithm;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/nr/pointCloud")
@Slf4j
public class PointCloudFileController {

    PointCloudAlgorithm pointCloudAlgorithm = new PointCloudAlgorithm();

    RegistrationAlgorithm registrationAlgorithm = new RegistrationAlgorithm();

    @Autowired
    ProjectServiceImpl projectService;

    @Autowired
    PointCloudFileServiceImpl pointCloudFileService;

    @Autowired
    ImageFileServiceImpl imageFileService;

    @Autowired
    ModelFileServiceImpl modelFileService;

    private static final Logger logger = LoggerFactory.getLogger(PointCloudFileController.class);

    @PostMapping(value = "/upload")
    public ResultVO fileUpload(
            MultipartFile file,
            String documentsId,
            String projectId) {
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (file == null){
            log.error("[上传点云] 参数错误 file为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (documentsId == null) {
            log.error("[上传点云] 参数错误 documentsId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (projectId == null) {
            log.error("[上传点云] 参数错误 projectId为空");
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
            PointCloudFileDTO result = pointCloudFileService.findPointCloudByDocId(documentsId);
            if (result == null){
                //不存在则最后写入数据库
                PointCloudFileDTO saveResult = pointCloudFileService.savePointCloud(documentsId,projectId);
                //记录结束时间
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;

                return ResultVOUtil.successWithRunTIme(saveResult, runTime);
            }
            //记录结束时间
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            return ResultVOUtil.successWithRunTIme(result,runTime);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.error(ResultEnum.UPLOAD_PATH_ILLEGAL);
        }
    }


    @RequestMapping("/download")
    public Object downloadFile(@RequestParam String pointCloudId,
                               final HttpServletResponse response, final HttpServletRequest request) throws Exception {
        if (pointCloudId == null){
            log.error("[下载点云] 参数错误 pointCloudId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String pointCloudPath = pointCloudFileService.findPointCloud(pointCloudId);
        //判断ID是否存在
        if(pointCloudPath == null){
            return ResultVOUtil.error(ResultEnum.POINT_CLOUD_FILE_NOT_FOUND);
        }
        //判断该文件在服务器上是否存在
        File f = new File(pointCloudPath);
        if (!f.exists()){
            return ResultVOUtil.error(ResultEnum.FILE_NOT_EXIST_IN_SERVICE);
        }

        String pathAfterSplit[] = pointCloudPath.split("\\\\");
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
                logger.error("下载附件失败，请检查poin图片文件目录“" + fileName + "”是否存在");
                return ResultVOUtil.error(1,"下载附件失败，请检查图片文件目录“" + fileName + "”是否存在");
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
    public ResultVO delete(@RequestParam(value = "pointCloudId") String pointCloudId) throws Exception{
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (pointCloudId == null){
            log.error("[删除点云] 参数错误 pointCloudId为空");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        //检查该id是否存在
        if (pointCloudFileService.findPointCloud(pointCloudId) ==null){
            return ResultVOUtil.error(ResultEnum.POINT_CLOUD_FILE_NOT_FOUND);
        }
        //删除操作
        if (pointCloudFileService.delete(pointCloudId) == true){
            //记录结束时间
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            return ResultVOUtil.successWithRunTIme(null, runTime);
        }
        return ResultVOUtil.error(ResultEnum.POINT_CLOUD_FILE_NOT_FOUND);
    }
    @PostMapping("find/pointCloudId")
    public ResultVO findByPCId(@RequestParam(value = "pointCloudId") String pointCloudId){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        //call service
        String pointCloudPath = pointCloudFileService.findPointCloud(pointCloudId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        if (pointCloudPath != null){
            return ResultVOUtil.successWithRunTIme(pointCloudPath,runTime);
        }
        else
            return ResultVOUtil.error(ResultEnum.FILE_NOT_FOUND);

    }

    @PostMapping("list")
    public ResultVO list(@RequestParam(value = "projectId") String projectId){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null){
            log.error("[找寻所有点云] 参数错误 projectId为空");
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
        return ResultVOUtil.successWithRunTIme(pointCloudFileService.findAllPoinCloud(projectId), runTime);
    }

    @PostMapping("/denosing/voxel")
    public ResultVO voxel(@RequestParam(value = "projectId") String projectId,
                          @RequestParam(value = "pointCloudId") String pointCloudId,
                          @RequestParam(value = "x") Float x,
                          @RequestParam(value = "y") Float y,
                          @RequestParam(value = "z") Float z){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null || pointCloudId == null|| x == null || y == null|| z == null){
            log.error("[体素滤波] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }

        String path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.voxelGridFilterService(path,x,y,z);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    @PostMapping("/denosing/radius")
    public ResultVO radius(@RequestParam(value = "projectId") String projectId,
                          @RequestParam(value = "pointCloudId") String pointCloudId,
                          @RequestParam(value = "searchRadius") Float searchRadius,
                          @RequestParam(value = "minimumNeighborNum") Integer minimumNeighborNumy ){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null || pointCloudId == null|| searchRadius == null || minimumNeighborNumy == null){
            log.error("[半径滤波] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.radiusFilterService(path,searchRadius,minimumNeighborNumy);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);
        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    @PostMapping("/denosing/gauss")
    public ResultVO gauss(@RequestParam(value = "projectId") String projectId,
                           @RequestParam(value = "pointCloudId") String pointCloudId,
                           @RequestParam(value = "searchNearestPointNum") Integer searchNearestPointNum,
                           @RequestParam(value = "removalThresh") Double removalThresh ){
        //记录开始时间
        long startTime = System.currentTimeMillis();


        if (projectId == null || pointCloudId == null|| searchNearestPointNum == null || removalThresh == null){
            log.error("[高斯滤波] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.gaussStatisticalFilterService(path,searchNearestPointNum,removalThresh);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    /**
     * 测试成功
     * @param projectId
     * @param pointCloudId
     * @param x_cut
     * @param y_cut
     * @param z_cut
     * @return
     */
    @PostMapping("/denosing/conditional")
    public ResultVO conditional(@RequestParam(value = "projectId") String projectId,
                          @RequestParam(value = "pointCloudId") String pointCloudId,
                                @RequestParam(value = "x_cut") Float x_cut,
                                @RequestParam(value = "y_cut")Float y_cut,
                                @RequestParam(value = "z_cut")Float z_cut ){
        //记录开始时间
        long startTime = System.currentTimeMillis();


        if (projectId == null || pointCloudId == null){
            log.error("[条件滤波] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.conditionalFilterService(path,x_cut,y_cut,z_cut);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    /***
     * 测试成功
     * @param projectId
     * @param pointCloudId
     * @param search_radius
     * @param search_point_num
     * @return
     */
    @PostMapping("/denosing/samplingRadiusFilterService")
    public ResultVO samplingRadiusFilterService(@RequestParam(value = "projectId") String projectId,
                                @RequestParam(value = "pointCloudId") String pointCloudId,
                                                @RequestParam(value = "search_radius")Double search_radius,
                                                @RequestParam(value = "search_point_num")Integer search_point_num){
        //记录开始时间
        long startTime = System.currentTimeMillis();


        if (projectId == null || pointCloudId == null){
            log.error("[高斯滤波] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.samplingRadiusFilterService(path,search_radius,search_point_num);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    @PostMapping("/color/colorMappingService")
    public ResultVO colorMappingService(@RequestParam(value = "projectId") String projectId,
                                        @RequestParam(value = "pointCloudId") String pointCloudId,
                                        @RequestParam(value = "imageId") String imageId){
        //记录开始时间
        long startTime = System.currentTimeMillis();
        if (projectId == null || pointCloudId == null ||imageId == null){
            log.error("[点云上色] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String cloudPath = pointCloudFileService.findPointCloud(pointCloudId);
        String imagePath = imageFileService.findImage(imageId);
        String newPath = pointCloudAlgorithm.colorMappingService(cloudPath, imagePath);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    @PostMapping("/color/colorMultiMappingService")
    public ResultVO colorMultiMappingService(@RequestParam(value = "projectId") String projectId,
                                             @RequestParam(value = "pointCloudId") String pointCloudId,
                                             @RequestParam(value = "posId")String posId
                                             ){
        //记录开始时间
        long startTime = System.currentTimeMillis();

        if (projectId == null || pointCloudId == null || posId == null){
            log.error("[点云上色] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        String image_folder_path = "/home/nsy/necrms/软件后端/Mydemo01/zed/";
        String cloud_path = pointCloudFileService.findPointCloud(pointCloudId);
        String pos_path = pointCloudFileService.findPointCloud(posId);
        String newPath = pointCloudAlgorithm.colorMultiMappingService(cloud_path,pos_path,image_folder_path);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }

    @PostMapping("/greedyProjectionService")
    public ResultVO greedyProjectionService(@RequestParam(value = "projectId") String projectId,
                                             @RequestParam(value = "pointCloudId") String pointCloudId,
                                            @RequestParam(value = "search_radius")Float search_radius,
                                            @RequestParam(value = "mu")Float mu,
                                            @RequestParam(value = "use_mls")Integer use_mls,
                                            @RequestParam(value = "mls_voxel_size")Float mls_voxel_size,
                                            @RequestParam(value = "mls_search_radius")Float mls_search_radius,
                                            @RequestParam(value = "mls_upsampling_radius")Float mls_upsampling_radius,
                                            @RequestParam(value = "mls_upsampling_step_size")Float mls_upsampling_step_size
    ){
        //记录开始时间
        long startTime = System.currentTimeMillis();
        if (projectId == null || pointCloudId == null || search_radius == null || mu == null || use_mls == null ||mls_voxel_size == null|| mls_search_radius == null|| mls_upsampling_radius == null||mls_upsampling_step_size == null){
            log.error("[三维重建] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }
        boolean use_mls_boolean = false;
        if (use_mls == 0){
            use_mls_boolean = false;
        }
        else {
            use_mls_boolean = true;
        }
        String cloud_path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = pointCloudAlgorithm.greedyProjectionService(cloud_path,search_radius,mu,use_mls_boolean,mls_voxel_size,mls_search_radius,mls_upsampling_radius,mls_upsampling_radius);
        ModelFileDTO modelFileDTO = modelFileService.saveModel(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(modelFileDTO, runTime);
    }

    /***
     * 配準： 不需要打開Open3D 返回路徑應該是pcd文件的路徑
     * @param projectId
     * @param pointCloudId
     * @param voxel_size
     * @param use_pose_recovery
     * @param use_pose_graph
     * @return
     */
    @PostMapping("/registration")
    public ResultVO registration(@RequestParam(value = "projectId") String projectId,
                                 @RequestParam(value = "pointCloudId") String pointCloudId,
                                 @RequestParam(value = "voxel_size")Double voxel_size,
                                 @RequestParam(value = "use_pose_recovery")Boolean use_pose_recovery,
                                 @RequestParam(value = "use_pose_graph")Boolean use_pose_graph
    ){
        //记录开始时间
        long startTime = System.currentTimeMillis();
        if (projectId == null || pointCloudId == null || voxel_size == null || use_pose_recovery == null || use_pose_graph == null){
            log.error("[registration] 参数错误");
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR);
        }

        String cloud_path = pointCloudFileService.findPointCloud(pointCloudId);
        String newPath = registrationAlgorithm.registrationService(cloud_path,voxel_size,use_pose_recovery,use_pose_graph);
        PointCloudFileDTO pointCloudFileDTO = pointCloudFileService.savePointCloud(newPath,projectId);

        //记录结束时间
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        return ResultVOUtil.successWithRunTIme(pointCloudFileDTO, runTime);
    }
}
