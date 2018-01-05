package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2018/1/5.
 */

import com.bysj.eyeapp.vo.KnowledePaperVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 知识库模块服务类
 */
public class KnowledgeService {

    /**
     * 获取推荐文章列表，该方法在知识库初始化时调用，加载推荐文章数据
     * @param page 获取推荐文章第几页
     * @param limit 每页显示的个数
     * @return 文章列表
     */
    public List<KnowledePaperVO> getRecommendPaper(int page,int limit){
        if(page<1){
            throw new RuntimeException("page 页数必须大于0");
        }
        if(limit<1){
            throw new RuntimeException("limit 每页显示页数必须大于0");
        }
        List<KnowledePaperVO> papers = new ArrayList<>();
        //由于网络请求相关方法尚未写好，这里先写死数据
        for(int i=0;i<limit;i++){
            KnowledePaperVO paper = new KnowledePaperVO();
            paper.setTitle(String.format("第%d页的标题,这是该页的第%d条数据",page,i));
            paper.setContent(String.format("第%d页的内容,这是该页的第%d条数据",page,i));
            paper.setDate(new Date());
            paper.setType(i%2==0?"爽文":"热文");
            paper.setViewCount((int)(Math.random()*100));
            papers.add(paper);
        }
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return papers;
    }
}
