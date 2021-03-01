//package com.qi.demo.service.impl;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Slf4j
//public class UserInfoServiceImpl implements UserInfoService {
//    @Autowired
//    UserInfoRepository userInfoRepository;
//
//    //注册
//    @Override
//    @Transactional
//    public UserInfoDTO createUserInfoOne(UserInfoDTO userInfoDTO) throws Exception{
//        UserInfo userInfo = new UserInfo();
//        //判断Openid是否已经注册过
//       UserInfo result = userInfoRepository.findByUserOpenid(userInfoDTO.getUserOpenid());
//       if (result != null){
//           log.error("[注册用户] 注册失败，openid = {} 已被注册", userInfoDTO.getUserOpenid());
//           throw new SellException(ResultEnum.REGISTER_FAIL);
//       }
//
//       /*
//        //将上传的头像存储到本地
//        String path = "C:/Users/Administrator/Desktop/FourmPicture/"
//                +userInfoDTO.getUserOpenid()+"_avatar.png";
//        //判断文件是否存在
//        File file = new File(path);
//        if (!file.exists())
//        {
//            log.error("[注册用户] 头像保存失败");
//            throw new SellException(ResultEnum.AVATAR_STORE_FAIL);
//        }
//
//        */
//
//        //将用户信息写入到数据库
//        BeanUtils.copyProperties(userInfoDTO,userInfo);
//        userInfoRepository.save(userInfo);
//        return userInfoDTO;
//    }
//
//    //登录
//    public UserInfoDTO findUserInfoByBuyerOpenid(String openid, String password){
//        UserInfo userInfo = userInfoRepository.findByUserOpenid(openid);
//        if(userInfo == null)
//        {
//            log.error("[登录] 登陆失败,账号或密码错误");
//            throw new SellException (ResultEnum.LOGIN_FAIL);
//        }
//        UserInfoDTO userInfoDTO = new UserInfoDTO();
//
//        BeanUtils.copyProperties(userInfo, userInfoDTO);
//        if(!userInfoDTO.getUserPassword().equals(password))
//        {
//            log.error("[登录] 登陆失败,账号或密码错误");
//            throw new SellException (ResultEnum.LOGIN_FAIL);
//        }
//        return userInfoDTO;
//    }
//
//}
