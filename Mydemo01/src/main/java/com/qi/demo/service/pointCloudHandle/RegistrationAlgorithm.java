package com.qi.demo.service.pointCloudHandle;

public class RegistrationAlgorithm {
    static {
        System.load("/home/nsy/necrms/软件后端/Mydemo01/src/libMMP_Shared_registration.so");
    }

    public native String registrationService(String path_in,
                               double voxel_size,
                               boolean use_pose_recovery,
                               boolean use_pose_graph);
}
