package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2018/1/12.
 */

import android.content.Context;

import com.bysj.eyeapp.dao.EyedataDAO;
import com.bysj.eyeapp.dto.EyedataDTO;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.vo.EyedataVO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用眼数据部分服务类
 */
public class EyedataService {
    private EyedataDAO dao = new EyedataDAO();


    /**
     * 累加本次开屏时间：更新数据库中本次开屏时间
     * @param eyedataVO 需要更新的数据
     */
    public void updateEyedata(Context context,EyedataVO eyedataVO){
        String date = new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(new Date());
        EyedataDTO dto = voToDto(eyedataVO);
        dao.updateEyedata(context,dto,date);
    }

    /**
     * 获取今天的用眼数据
     * @return
     */
    public EyedataVO getEyedataToday(Context context){
        EyedataVO eyedataVO = new EyedataVO();
        String date = new String(new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(new Date()));
        EyedataDTO dto = dao.findEyedataByDate(context,date);
        return dtoToVo(dto);
    }


    /**
     * 插入一条新的用眼数据记录，当天第一次打开app时初始化，插入之前先要判断当天记录是否存在
     * @param context
     * @param vo
     */
    public void addEyedata(Context context,EyedataVO vo){
        dao.insertEyedata(context,voToDto(vo));
    }

    private EyedataVO dtoToVo(EyedataDTO dto){
        EyedataVO vo = new EyedataVO();
        vo.setIndoorTime(dto.getIndoorTime());
        vo.setOutdoorTime(dto.getOutdoorTime());
        vo.setOpenScreenCount(dto.getOpenScreenCount());
        vo.setOpenScreenTimeCountToday(dto.getOpenScreenTime());
        return vo;
    }

    private EyedataDTO voToDto(EyedataVO vo){
        EyedataDTO dto = new EyedataDTO();
        String date = new String(new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(new Date()));
        dto.setDate(date);
        dto.setOutdoorTime(vo.getOutdoorTime());
        dto.setIndoorTime(vo.getIndoorTime());
        dto.setOpenScreenCount(vo.getOpenScreenCount());
        dto.setOpenScreenTime(vo.getOpenScreenTimeCountToday());
        return dto;
    }

    public boolean judgeTodayEyedataExist(Context context){
        String date = new String(new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(new Date()));
        return dao.judgeEyedataExistByDate(context,date);
    }



}
