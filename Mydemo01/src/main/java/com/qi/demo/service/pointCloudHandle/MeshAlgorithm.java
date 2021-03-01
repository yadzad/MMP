package com.qi.demo.service.pointCloudHandle;

import java.util.Vector;

public class MeshAlgorithm {
    static {
        System.load("/home/nsy/necrms/软件后端/Mydemo01/src/libMMP_Shared.so");
    }
    class SPoint{
        public SPoint(){}
        public double x_;
        public double y_;
        public double z_;
    }
    class SAABB{
        public SAABB(){}
        SPoint min_point_;
        SPoint max_point_;
    }
    class SOBB{
        public SOBB(){}
        SPoint min_point_;
        SPoint max_point_;
        SPoint position_;
        double [][]rotational_matrix_;
    }
    class SDefectDetectionResult{
        public SDefectDetectionResult(){}
        Vector<SAABB> aabb_list_;
        Vector<SOBB> obb_list_;
        String defect_colored_cloud_path_;
    }

    public native SDefectDetectionResult c2cClassicService(String jcompared_cloud_path,
                                                                               String jreference_cloud_path,
                                                                               /// C2C参数
                                                                               int nearest_neighbor_num_to_search,
                                                                               /// 连通性分析参数
                                                                               double threshold_of_defect_partition,
                                                                               double radius_of_connected_);

    public native SDefectDetectionResult c2mClassicService(String jcompared_cloud_path,
                                                                               String jreference_cloud_path,
                                                                               /// 连通性分析参数
                                                                               double threshold_of_defect_partition,
                                                                               double radius_of_connected_);

    public native SDefectDetectionResult c2mGravityCenterService(String jcloud_path,
                                                                                     String jmesh_path,
                                                                                     ///连通性分析参数
                                                                                     double threshold_of_defect_partition,
                                                                                     double radius_of_connected_);
}
