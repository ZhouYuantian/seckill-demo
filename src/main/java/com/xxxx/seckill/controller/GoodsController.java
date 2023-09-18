package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVO;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    IUserService userService;
    @Autowired
    IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolve;

    //优化前吞吐量:3495
    //缓存吞吐量: 8000
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response)
    {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        String html=(String) valueOperations.get("goodsList");
        if(StringUtils.hasText(html))
        {
            return html;
        }
        else
        {
            //model.addAttribute("user",user);
            model.addAttribute("goodsList",goodsService.findGoodsVo());
            WebContext context=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
            html=thymeleafViewResolve.getTemplateEngine().process("goodsList",context);
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
            return html;
        }
    }

//    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String toDetail(Model model,User user,@PathVariable Long goodsId,HttpServletRequest request,HttpServletResponse response)
//    {
//        ValueOperations valueOperations=redisTemplate.opsForValue();
//        String html=(String) valueOperations.get("goodsDetail:"+goodsId);
//        if(StringUtils.hasText(html))
//        {
//            return html;
//        }
//        else
//        {
//            GoodsVo goodsVo=goodsService.findGoodsByGoodsId(goodsId);
//            Date startDate=goodsVo.getStartDate();
//            Date endDate=goodsVo.getEndDate();
//            Date nowDate=new Date();
//            int remainSeconds;
//            if (nowDate.after(endDate))
//            {
//                remainSeconds=-1;
//            }
//            else if(nowDate.before(startDate))
//            {
//                remainSeconds=((int)((startDate.getTime()-nowDate.getTime())/1000));
//            }
//            else
//            {
//                remainSeconds=0;
//            }
//            model.addAttribute("user",user);
//            model.addAttribute("goods",goodsVo);
//            model.addAttribute("remainSeconds",remainSeconds);
//
//            WebContext context=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
//            html=thymeleafViewResolve.getTemplateEngine().process("goodsDetail",context);
//            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
//            return html;
//        }
//    }
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId)
    {
        GoodsVo goodsVo=goodsService.findGoodsByGoodsId(goodsId);
        Date startDate=goodsVo.getStartDate();
        Date endDate=goodsVo.getEndDate();
        Date nowDate=new Date();
        int remainSeconds;
        if (nowDate.after(endDate))
        {
            remainSeconds=-1;
        }
        else if(nowDate.before(startDate))
        {
            remainSeconds=((int)((startDate.getTime()-nowDate.getTime())/1000));
        }
        else
        {
            remainSeconds=0;
        }
        DetailVO detailVO=new DetailVO();
        detailVO.setUser(user);
        detailVO.setGoodsVo(goodsVo);
        detailVO.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVO);
    }
}
