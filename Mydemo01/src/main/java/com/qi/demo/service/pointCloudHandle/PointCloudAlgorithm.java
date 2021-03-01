package com.qi.demo.service.pointCloudHandle;


import java.util.Vector;

public class PointCloudAlgorithm {
    static {
        System.load("/home/nsy/necrms/软件后端/Mydemo01/src/libMMP_Shared(2).so");
    }


    //点云滤波算法
    public native String voxelGridFilterService(String path_in, double leaf_size_x, double leaf_size_y, double leaf_size_z); //体素滤波

    public native String radiusFilterService(String path_in, double search_radius, int minimum_neighbor_num); //半径滤波

    public native String gaussStatisticalFilterService(String path_in, int search_nearest_point_num, double removal_thresh);  //高斯滤波

    public native String conditionalFilterService(String path_in, float x_cut, float y_cut, float z_cut);   //条件滤波

    /**
     * @brief 采样半径滤波
     * @details 取search_point_num个点统计其search_radius半径中的点的个数的均值，点云中邻近点小于该均值一半的点会被滤掉.
     */
    public native String samplingRadiusFilterService(String jpath,
                                            double search_radius,
                                            int search_point_num);

    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////// 点云融合 //////////////////////////////////////////////////////
// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 点云上色
     * @param cloud_path 点云路径
     * @param image_path 图片路径
     */
    public native String colorMappingService(String cloud_path, String image_path);

    /**
     * 点云上色
     * @param cloud_path 点云路径
     * @param pos_path 位姿文件路径
     * @param image_folder_path 图片文件夹路径
     */
    public native String colorMultiMappingService(String cloud_path,
                                     String pos_path,
                                     String image_folder_path);

    /**
     * 纹理映射
     * @param mesh_path 模型路径
     * @param image_path 图片路径
     */
    public native String textureMappingService(String mesh_path, String image_path);

    /**
     * 纹理映射
     * @param mesh_path 模型路径
     * @param pos_path 位姿文件路径
     * @param image_folder_path 图片文件夹路径
     */
    public native String textureMultiMappingService(String mesh_path,
                                       String pos_path,
                                       String image_folder_path);


// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ///////////////////////////////////////////////////// 三维重建 //////////////////////////////////////////////////////
// ///////////////////////////////////////////////////////////////////////////////////////

    /**
     * 贪婪三角化
     * @param cloud_path 点云路径
     * @param search_radius 连接点之间的最大距离
     * @param mu 样本点搜索近邻点点最远距离
     * @param use_mls 是否使用mls
     * @param mls_voxel_size mls体素滤波栅格大小
     * @param mls_search_radius 用于拟合的K近邻半径，越大平滑力度越大
     * @param mls_upsampling_radius 采样半径
     * @param mls_upsampling_step_size 采样步长
     */
    public native String greedyProjectionService(String cloud_path,
                                        float search_radius,
                                        float mu,
                                        boolean use_mls,
                                        float mls_voxel_size,
                                        float mls_search_radius,
                                        float mls_upsampling_radius,
                                        float mls_upsampling_step_size);

    /**
     * 贪婪三角化
     * @param cloud_path 点云路径
     * @param use_mls 是否使用mls
     * @param voxel_size 点云体素大小
     */
    public native String greedyProjectionService(String cloud_path,
                                        boolean use_mls,
                                        float voxel_size);
//    public static void main(String[] args) {
//        PointCloudAlgorithm pointCloudAlgorithm = new PointCloudAlgorithm();
//      System.out.println(pointCloudAlgorithm.voxelGridFilterService("/home/nercms/软件后端/Mydemo01/pcdFile/cloud.pcd",(float)0.1,(float)0.1,(float)0.1));
//       System.out.println(pointCloudAlgorithm.radiusFilterService("/home/nercms/软件后端/Mydemo01/pcdFile/cloud.pcd",1,50));
//        System.out.println(pointCloudAlgorithm.gaussStatisticalFilterService("/home/nercms/软件后端/Mydemo01/pcdFile/cloud.pcd",50,0.5));
//    }
}
