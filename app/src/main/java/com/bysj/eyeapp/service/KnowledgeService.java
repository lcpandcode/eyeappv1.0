package com.bysj.eyeapp.service;

/**
 * Created by lcplcp on 2018/1/5.
 */

import com.alibaba.fastjson.JSON;
import com.bysj.eyeapp.exception.BackstageException;
import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.UserException;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.HttpUtil;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.view.KnowledgeFragment;
import com.bysj.eyeapp.view.R;
import com.bysj.eyeapp.vo.KnowledePaperVO;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识库模块服务类
 */
public class KnowledgeService {
    private static final String ADVISORY_PATH = "/askquestion.do";
    private static final String GET_PAPER_LIST_PATH = "/knowledge/list.do";
    private static final String GET_HOTPAPER_LIST_PATH = "/knowledge/hotlist.do";
    private static final String POST_ADVISORY = "/knowledge/askquestion.do";
    private static final String REMIND_NOT_LOGIN = "您尚未登录,请先登录！";
    private static final String REMNID_BACKSTAGE_ERROR = "后台出错！";
    private static int DEFAULT_LIMIT = 10;
    private int recommendPaperMaxPage = -1;//推荐文章最大页数
    private int blogPaperMaxPage = -1;//博客最大页数
    private int eatdietPaperMaxPages = -1;//饮食习惯最大页数存储
    private int lecturePaperMaxPage = -1;


    /**
     * 获取推荐文章列表，该方法在知识库初始化时调用，加载推荐文章数据
     * 如果已经没有更多数据，则返回一个空的List，如果网络请求出错，则返回null
     * @param page 获取推荐文章第几页
     * @param limit 每页显示的个数
     * @return 文章列表
     */
    private List<KnowledePaperVO> getRecommendPaper(int page,int limit) throws HttpException {
        if(page<1){
            page = 1;
        }
        if(limit<1){
            limit = DEFAULT_LIMIT;
        }
        List<KnowledePaperVO> papers = new ArrayList<>();
        if(recommendPaperMaxPage!=-1 && page>recommendPaperMaxPage){
            return papers;
        }
        Map<String,String> param = new HashMap<>();
        param.put("pageNum",page + "");
        param.put("pageSize",limit + "");
        String result = HttpUtil.synGet(GET_HOTPAPER_LIST_PATH,param);
        Map<String,Object> resultMap = (Map<String, Object>) JavaBeanUtil.jsonToObj(result);
        //判断请求状态
        if(!(0==(Integer)resultMap.get("status"))){
            throw new HttpException("请求失败！服务出错！");
        }
        Map<String , Object> dataMap = (Map<String, Object>) resultMap.get("data");
        //判断是否已经到达最后一页，如果到达最后一页，返回空列表
        recommendPaperMaxPage = (Integer)dataMap.get("lastPage");
        List<Map<String,Object>> papersTem = (List<Map<String,Object>>)dataMap.get("list");
        for(Map<String,Object> map : papersTem){
            KnowledePaperVO paper = new KnowledePaperVO();
            paper.setId((Integer) map.get("id"));
            paper.setTitle(map.get("title").toString());
            paper.setContent(map.get("content").toString());
            String date = new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(map.get("date"));
            paper.setDate(date);
            paper.setType(map.get("type").toString());
            paper.setSign("热文");
            paper.setViewCount((Integer)map.get("viewCount"));
            papers.add(paper);
        }
        return papers;
    }

    /**
     * 根据文章类型获取对应文章
     * 如果已经没有更多数据，则返回一个空的List，如果网络请求出错，则返回null
     * @param paperType 文章类型，具体的文章类型，该类型的对应常量在知识库Fragment中定义
     * @param page 第几页的文章
     * @param limit 每页显示多少文章
     * @return 文章列表
     */
    public List<KnowledePaperVO> getPaperByType(int paperType,int page,int limit) throws HttpException {
        if(page<1){
            page = 1 ;
        }
        if(limit<1){
            limit = DEFAULT_LIMIT;
        }
        //如果推荐文章，直接调用推荐文章的方法
        if(paperType==KnowledgeFragment.PAPER_TYPE_DEFAULT){
            return getRecommendPaper(page,limit);
        }
        List<KnowledePaperVO> papers = new ArrayList<>();
        String type = null;
        if(paperType== KnowledgeFragment.PAPER_TYPE_BLOG){
            type = "护眼博客";
            //判断page是否大于最大页码
            if(blogPaperMaxPage!=-1 && page>blogPaperMaxPage){
                return papers;
            }
        }else if(paperType== KnowledgeFragment.PAPER_TYPE_LECTURE){
            type = "护眼讲座";
            //判断page是否大于最大页码
            if(lecturePaperMaxPage!=-1 && page>lecturePaperMaxPage){
                return papers;
            }
        }else {
            type = "饮食习惯";
            //判断page是否大于最大页码
            if(eatdietPaperMaxPages!=-1 && page>eatdietPaperMaxPages){
                return papers;
            }
        }
        Map<String,String> param = new HashMap<>();
        param.put("type",type);
        param.put("pageNum",page + "");
        param.put("pageSize",limit + "");
        String result = HttpUtil.synGet(GET_PAPER_LIST_PATH,param);
        Map<String,Object> resultMap = (Map<String, Object>) JavaBeanUtil.jsonToObj(result);
        //判断请求状态
        if(!(0==(Integer)resultMap.get("status"))){
            throw new HttpException("请求失败！服务出错！");
        }
        Map<String , Object> dataMap = (Map<String, Object>) resultMap.get("data");
        //判断是否已经到达最后一页，如果到达最后一页，返回空列表
        Integer lastPage = (Integer)dataMap.get("lastPage");
        if("护眼博客".equals(type)){
            blogPaperMaxPage = lastPage;
        }else if("护眼讲座".equals(type)){
            lecturePaperMaxPage = lastPage;
        }else {
            eatdietPaperMaxPages = lastPage;
        }

        List<Map<String,Object>> papersTem = (List<Map<String,Object>>)dataMap.get("list");
        for(Map<String,Object> map : papersTem){
            KnowledePaperVO paper = new KnowledePaperVO();
            paper.setId((Integer) map.get("id"));
            paper.setTitle(map.get("title").toString());
            paper.setContent(map.get("content").toString());
            String date = new SimpleDateFormat(GlobalConst.DATE_PATTERN).format(map.get("date"));
            paper.setDate(date);
            paper.setType(map.get("type").toString());
            paper.setSign("热文");
            paper.setViewCount((Integer)map.get("viewCount"));
            papers.add(paper);
        }
        return papers;
    }

    /**
     * 提交问题service方法
     * @param question 问题对象
     */
    public void advisorySubmitQUestion(KnowledgeAdvisoryQuestionVO question) throws HttpException {
        Map<String,String> params = new HashMap<>();
        params.put("title",question.getTitle());
        params.put("content",question.getContent());
        String result = HttpUtil.synPost(POST_ADVISORY,params);
        Map<String,Object> resultMap = (Map<String,Object>)JavaBeanUtil.jsonToObj(result);
        int status = (Integer) resultMap.get("status");
        if(status==10){
            throw new UserException(REMIND_NOT_LOGIN);
        }
        if(status==1){
            throw new UserException(REMIND_NOT_LOGIN);
        }
        if(status!=0){
            throw new BackstageException(GlobalConst.REMIND_BACKSTAGE_ERROR);
        }


    }

    public int getRecommendPaperMaxPage() {
        return recommendPaperMaxPage;
    }

    public int getBlogPaperMaxPage() {
        return blogPaperMaxPage;
    }

    public int getEatdietPaperMaxPages() {
        return eatdietPaperMaxPages;
    }

    public int getLecturePaperMaxPage() {
        return lecturePaperMaxPage;
    }
}
