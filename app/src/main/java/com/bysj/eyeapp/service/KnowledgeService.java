package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2018/1/5.
 */

import com.bysj.eyeapp.view.KnowledgeFragment;
import com.bysj.eyeapp.view.R;
import com.bysj.eyeapp.vo.KnowledePaperVO;

import java.text.SimpleDateFormat;
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
    private List<KnowledePaperVO> getRecommendPaper(int page,int limit){
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
            paper.setId(page*10 + i);
            paper.setTitle(String.format("第%d页的标题,这是该页的第%d条数据",page,i));
            paper.setContent(String.format("第%d页的内容,这是该页的第%d条数据",page,i));
            paper.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            paper.setSign(i%2==0?"爽文":"热文");
            paper.setType(i%2==0?"专家讲座":"饮食习惯");
            paper.setViewCount((int)(Math.random()*100));
            papers.add(paper);
        }
        return papers;
    }

    /**
     * 根据文章类型获取对应文章
     * @param paperType 文章类型，具体的文章类型，该类型的对应常量在知识库Fragment中定义
     * @param page 第几页的文章
     * @param limit 每页显示多少文章
     * @return 文章列表
     */
    public List<KnowledePaperVO> getPaperByType(int paperType,int page,int limit){
        if(page<1){
            throw new RuntimeException("page 页数必须大于0");
        }
        if(limit<1){
            throw new RuntimeException("limit 每页显示页数必须大于0");
        }
        //如果推荐文章，直接调用推荐文章的方法
        if(paperType==KnowledgeFragment.PAPER_TYPE_DEFAULT){
            return getRecommendPaper(page,limit);
        }
        List<KnowledePaperVO> papers = new ArrayList<>();
        String type = null;
        if(paperType== KnowledgeFragment.PAPER_TYPE_BLOG){
            type = "博客";
        }else if(paperType== KnowledgeFragment.PAPER_TYPE_LECTURE){
            type = "讲座";
        }else {
            type = "饮食习惯";
        }
        for(int i=0;i<limit;i++){
            KnowledePaperVO paper = new KnowledePaperVO();
            paper.setId(page*10 + i);
            paper.setTitle(String.format("第%d页的" + type +"标题,这是该页的第%d条数据",page,i));
            paper.setContent(String.format("第%d页的" + type +"内容,这是该页的第%d条数据",page,i));
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            paper.setDate(date);
            paper.setType(i%2==0?"爽文":"热文");
            paper.setViewCount((int)(Math.random()*100));
            papers.add(paper);
        }
        return papers;
    }
}
