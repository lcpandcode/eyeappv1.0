package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/24.
 */

import com.bysj.eyeapp.vo.TestVisionQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 该工具类用于生成视力测试的数据，主要是根据距离传感器获取的距离，生成对象的视力表数据
 */
public class TestVisionUtil {
    private static final float EYE_MIN_ANGLE = 0.0002909f;//人眼最小分辨角(单位是弧度rad)
    private static final float EYE_ANGLE_INCREASE_RATE = 1.258925f;//视角增率
    private static final float [] VISION_ARR = new float[]{4.0f,4.1f,4.2f,4.3f,4.4f,4.5f,4.6f,4.7f,4.8f,4.9f,5.0f,5.1f,5.2f};

    /**
     * 该方法主要生成测试图形对应的方向（随机）以及图形大小（根据视力表的数值：4.0-5.2）
     * @param distance 距离,单位是厘米
     * @param vision 视力值：4.0-5.2
     * @return 问题列表，问题尺寸是单位是毫米
     */
    public static TestVisionQuestionVO getTestVisionQuestionVO(float distance,float vision,List<Character> list){
        boolean visionIsContain = false;
        for(float i : VISION_ARR){
            if(vision==i){
                visionIsContain = true;
            }
        }
        if(!visionIsContain){
            throw new RuntimeException("视力值不合法");
        }
        TestVisionQuestionVO question = new TestVisionQuestionVO();
        //随机获取方向
        int random = (int)(Math.random() * list.size());
        char direction = list.get(random);
        list.remove(random);
        question.setDirection(direction);
        float normalVisionSize = distance * EYE_MIN_ANGLE;//5.0标准分辨大小距离
        int multiple = (int)((5.0-vision)/0.1);//计算vision值和5.0视力值的倍率关系
        float size = (float)(normalVisionSize * (Math.pow(EYE_ANGLE_INCREASE_RATE,(float)multiple)));//计算对应vision的尺寸
        question.setSize(size * 10 *5);//标准视力表是五分最小视角，所以乘以5，乘以10是因为question的size返回值是毫米
        return question;
    }

    /**
     * 获取常规的视力表数据，通常视力测试调用该方法即可
     * @param distance 测试距离
     * @param num 每个视力值测试的图标个数
     * @return 问题列表
     */
    public static List<TestVisionQuestionVO> getTestVisionQuestionVOs(float distance,int num){
        if(distance<=0 || num<=0){
            throw new RuntimeException("数据异常：传入的distance和num必须大于0");
        }
        List<TestVisionQuestionVO> questions = new ArrayList<>();

        List<Character> listTem = new ArrayList<>(4);
        for(float i : VISION_ARR){
            listTem.clear();
            listTem.add('左');
            listTem.add('右');
            listTem.add('上');
            listTem.add('下');
            for(int j=0;j<num;j++){
                TestVisionQuestionVO question = getTestVisionQuestionVO(distance,i,listTem);
                question.setVisionVal(i);
                questions.add(question);
            }
        }
        return questions;
    }

}
