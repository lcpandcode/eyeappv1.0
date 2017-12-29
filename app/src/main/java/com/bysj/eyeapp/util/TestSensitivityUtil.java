package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2017/12/29.
 */

import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类主要放敏感度相关的工具类：包括动态生成敏感度测试区域的算法等
 */
public class TestSensitivityUtil {

    /**
     *
     * @param num 要生成问题的个数
     * @return 敏感度测试问题列表
     */
    public static List<TestSensitivityQuestionVO> createQuestions(int num){
        List<TestSensitivityQuestionVO> vos = new ArrayList<>();

        return vos;
    }

    /**
     *
     * @return 敏感度测试问题对象
     */
    public static TestSensitivityQuestionVO createQuestion(){
        //核心代码待完善
        return null;
    }
}
