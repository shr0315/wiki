package com.jiawa.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Category;
import com.jiawa.wiki.domain.CategoryExample;
import com.jiawa.wiki.mapper.CategoryMapper;
import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        //   页码  每页的条数  例如：第1页，10条内容
        PageHelper.startPage(req.getPage(),req.getSize());
        // 紧跟着的第一个select方法会被分页
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());  //前端计算总页数

        //List<CategoryResp> respList = new ArrayList<>();
        //for (Category category : categoryList) {
        //    //CategoryResp categoryResp = new CategoryResp();
        //    //BeanUtils.copyProperties(category,categoryResp);
        //    对象复制
        //    CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
        //    respList.add(categoryResp);
        //}

        //列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    /**
     * 删除
     */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
