package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2017/12/29.
 */


import com.bysj.eyeapp.view.TestSensitivityActivity;
import com.bysj.eyeapp.vo.TestSensitivityQuestionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类主要放敏感度相关的工具类：包括动态生成敏感度测试区域的算法等
 */
public class TestSensitivityUtil {
    private final static int DEFAULT_DIFFICULTY = 50;//默认难度系数为50
    private final static int DIFFICULTY_MIN = 1;//难度最小值（最难）为1
    private final static int DIFFICULTY_MAX = 100;//难度最大值为100（最容易）
    private final static double SENSITIVITY_MAX = TestSensitivityActivity.getMaxSensitivity();//敏感度最大为100

    /**
     * 这是个默认获取问题列表的方法，客户端一般直接调用该方法即可获得一个问题列表，该列表默认生成规则如下：
     * 默认问题个数是10，最大难度数为50，最小为10，每个题目之间难度相差（50-10）/(10-2)=5
     * @return
     */
    public static List<TestSensitivityQuestionVO> defaultCreateQuestions(){
        int defaultQuestionNum = 10;
        int maxDifficulty = 35;
        int minDifficulty = 3;
        int gradient = (maxDifficulty - minDifficulty) / (defaultQuestionNum - 1);
        List<TestSensitivityQuestionVO> questions = new ArrayList<>();
        for(int i=0;i<defaultQuestionNum;i++){
            questions.add(createQuestion(maxDifficulty));
            maxDifficulty-=gradient;
        }
        return questions;
    }


    /**
     * 默认生成问题的方法，默认难度系数为50
     *
     * @param num 要生成问题的个数
     * @return 敏感度测试问题列表
     */
    public static List<TestSensitivityQuestionVO> createQuestions(int num) {
        return createQuestions(num, DEFAULT_DIFFICULTY);
    }

    /**
     * @param num        要生成问题的个数
     * @param difficulty 敏感度测试的难度系数：它是一个int，代表RGB之间的值间隔，间隔越小，代表颜色越接近，难度越高
     * @return 敏感度测试问题列表
     */
    public static List<TestSensitivityQuestionVO> createQuestions(int num, int difficulty) {
        if (difficulty < 0) {
            throw new RuntimeException("难度系数必须大于0");
        }
        if (num <= 0) {
            throw new RuntimeException("生成问题个数必须大于0");
        }
        List<TestSensitivityQuestionVO> vos = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            vos.add(createQuestion(difficulty));
        }
        return vos;
    }

    /**
     * 核心生成敏感度测试的方法：该方法定义了敏感度测试中，根据难度系数进行生成背景颜色以及正确选项的核心方法
     *
     * @param difficulty 敏感度测试的难度系数：它是一个int，代表RGB之间的值间隔，间隔越小，代表颜色越接近，难度越高
     * @return 问题对象
     */
    public static TestSensitivityQuestionVO createQuestion(int difficulty) {
        if (difficulty < DIFFICULTY_MIN || difficulty > DIFFICULTY_MAX) {
            throw new RuntimeException(String.format("难度系数必须大于%d且小于%d", DIFFICULTY_MIN, DIFFICULTY_MAX));
        }
        //核心代码待完善
        TestSensitivityQuestionVO question = new TestSensitivityQuestionVO();
        //生成对应的RGB颜色，随机获取一个0-255之间的数字
        int[] rgbArr = getRandonRgbVal(difficulty);
        int rgbVal1 = rgbArr[0];
        int rgbVal2 = rgbArr[1];
        int rgbVal3 = rgbArr[2];
        //把十进制颜色转换为16进制字符
        String rgbVal1Str = Integer.toHexString(rgbVal1);
        rgbVal1Str = rgbVal1Str.length() > 1 ? rgbVal1Str : "0" + rgbVal1Str;//补零操作
        String rgbVal2Str = Integer.toHexString(rgbVal2);
        rgbVal2Str = rgbVal2Str.length() > 1 ? rgbVal2Str : "0" + rgbVal2Str;//补零操作
        String rgbVal3Str = Integer.toHexString(rgbVal3);
        rgbVal3Str = rgbVal3Str.length() > 1 ? rgbVal3Str : "0" + rgbVal3Str;//补零操作
        //根据三个rgb生成对应的颜色对象
        //1、生成对应的16进制按钮背景数字
        StringBuffer sbBackground = new StringBuffer();
        sbBackground.append("#");
        sbBackground.append(rgbVal1Str);
        sbBackground.append(rgbVal2Str);
        sbBackground.append(rgbVal2Str);//错误选项的颜色第三个坐标RGB值和第二个一样
        question.setBackgroundColor(sbBackground.toString());
        //2、生成对应的16进制正确按钮颜色
        StringBuffer sbTrue = new StringBuffer();
        sbTrue.append("#");
        sbTrue.append(rgbVal1Str);
        sbTrue.append(rgbVal2Str);
        sbTrue.append(rgbVal3Str);
        question.setTrueOptionColor(sbTrue.toString());
        question.setDifficulty(difficulty);
        return question;
    }


    /**
     * 获取三个rgb值，rgb1和rgb2该值为随机运算获得，rgb3为rgb2和difficulty运算所得
     *
     * @param difficulty 难度系数
     * @return 三个rgb值组成的数组：第三个是用于生成正确按钮（即颜色不一样的按钮）用
     */
    private static int[] getRandonRgbVal(int difficulty) {
        //生成对应的RGB颜色，随机获取一个0-255之间的数字
        int rgbVal1 = getRandonRgbColorVal();//rgb1随机获取
        int rgbVal2 = 0;
        int rgbVal3 = 0;
        while (true) {
            rgbVal2 = getRandonRgbColorVal();//rgb2也随机获取
            rgbVal3 = rgbVal2 - DEFAULT_DIFFICULTY > 0 ? rgbVal2 - difficulty : rgbVal2 + difficulty;//rgb3和rgb2的值相差difficulty值
            if (rgbVal3 > 255) {
                //如果得到的rgb3值大于255，重新获取随机值
                continue;
            } else {
                break;
            }
        }
        return new int[]{rgbVal1, rgbVal2, rgbVal3};
    }

    /**
     * 获取随机的0-255之间的数值
     *
     * @return
     */
    private static int getRandonRgbColorVal() {
        double valTem = Math.random();
        return (int) (valTem * 255);
    }

    /**
     * 默认生成问题的方法
     *
     * @return 返回默认难度为50的问题对象
     */
    public static TestSensitivityQuestionVO createQuestion() {
        //默认难度系数为50
        return createQuestion(DEFAULT_DIFFICULTY);
    }

    /**
     * 计算敏感度的方法，它根据各个题目难度系数来计算比例（越难的题目占的比例越高），根据这一比例计算总分数
     * @param questions 回答的问题列表
     * @param result 答题结果
     * @return 计算结果
     */
    public static double countSensitivity(List<TestSensitivityQuestionVO> questions, List<Boolean> result) {
        //计算所有难度系数之和
        int sumDifficulty = 0;
        int[] proportion = new int[questions.size()];//存储每个题目的比重数值
        for (int i = 0; i < questions.size(); i++) {
            TestSensitivityQuestionVO question = questions.get(i);
            //计算各个题目的比重数值
            if(result.get(i)){
                //答题正确，计算比重值
                proportion[i] = DIFFICULTY_MAX - question.getDifficulty();
            }else {
                //答题错误，proportion为0
                proportion[i] = 0;
            }

            sumDifficulty += DIFFICULTY_MAX - question.getDifficulty();
        }
        //计算结果
        double sensitivity = 0.0;
        for (int i : proportion) {
            sensitivity += ((double)i) / sumDifficulty * SENSITIVITY_MAX;
        }
        return sensitivity;

    }

}
