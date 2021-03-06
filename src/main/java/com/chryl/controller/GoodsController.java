package com.chryl.controller;

import com.alibaba.fastjson.JSONObject;
import com.chryl.core.response.ReturnResult;
import com.chryl.po.ChrGoods;
import com.chryl.service.GoodsService;
import com.chryl.vo.GoodsVo;
import com.chryl.vo.PageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chr.yl on 2020/6/10.
 *
 * @author Chr.yl
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/create")
    public Object create(@RequestBody ChrGoods chrGoods) {
        boolean res = goodsService.createChrGoods(chrGoods);
        if (res) {
            return ReturnResult.create(HttpStatus.OK);
        }
        return ReturnResult.create(null);
    }

    /**
     * 问题遗留: 后端接受的时间为格林威治时间,无法转化为北京时间
     *
     * @param chrGoods
     * @return
     */
    @PostMapping("/update")
    public Object update(@RequestBody ChrGoods chrGoods) {
        boolean res = goodsService.updateChrGoods(chrGoods);
        if (res) {
            return ReturnResult.create(HttpStatus.OK);
        }
        return ReturnResult.create(null);
    }

    /**
     * 问题遗留:注意这里,使用form-data接收多选框的数据时不成功,暂时使用json格式接收即可解决
     *
     * @param page
     * @param chrGoods
     * @return
     */
    //数据列表
//    @PostMapping("/list")
//    public Object list(PageVo page, ChrGoods chrGoods) {
//        return ReturnResult.create(goodsService.goodsList(page, chrGoods));
//    }
    @PostMapping("/list")
    public Object list(@RequestBody GoodsVo goodsVo) {
        ChrGoods chrGoods = new ChrGoods();
        BeanUtils.copyProperties(goodsVo, chrGoods);
        PageVo pageVo = new PageVo(goodsVo.getPage(), goodsVo.getLimit());
        return ReturnResult.create(goodsService.goodsList(pageVo, chrGoods));
    }


    //获取查询框的查询条件
    @GetMapping("/conditions")
    public Object conditions() {
        List<Map<String, String>> queryConditions = goodsService.queryConditions();
        return ReturnResult.create(queryConditions);
    }

    //修改商品的发布状态
    @PostMapping("/changeGoodsStatus")
    public ReturnResult changeGoodsStatus(@RequestBody ChrGoods chrGoods) {
//    public ReturnResult changeGoodsStatus( ChrGoodsModel chrGoods) {
        boolean res = goodsService.changeGoodsStatus(chrGoods);
        if (res) {
            return ReturnResult.create(HttpStatus.OK);
        }
        return ReturnResult.create(null);
    }

    //删除
    @DeleteMapping("/deleteGoods/{goodsId}")
    public ReturnResult deleteGoods(@PathVariable String goodsId) {
        boolean res = goodsService.deleteGoods(goodsId);
        if (res) {
            return ReturnResult.create(HttpStatus.OK);
        }
        return ReturnResult.create(null);
    }

    //上传列表
    @PostMapping("/upload")
    public Object upload(List<MultipartFile> uploadFileList) {

        for (MultipartFile multipartFile : uploadFileList) {
            System.out.println(multipartFile.getOriginalFilename());
        }
        return ReturnResult.create(null);
    }
    //单独上传
    @PostMapping("/uploadOnly")
    public Object uploadOnly(MultipartFile uploadFile) {

        System.out.println(uploadFile.getOriginalFilename());
        return ReturnResult.create(null);
    }

    //展示图片
    @GetMapping("/showimg")
    public void showimg(@RequestParam(required = false) String path, HttpServletResponse response) {
        if (path == null || path.trim().length() == 0) {
            path = "/Users/chryl/upload/common/20200127/13881196847010329.jpg";
        }
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()
        ) {
            if (file.exists()) {

                byte[] bytes = new byte[inputStream.available()];
                int length = 0;
                while ((length = inputStream.read(bytes)) != -1) {
                    System.out.println("length in ::" + length);
                    //写出客户端
                    outputStream.write(bytes, 0, length);
                }
                System.out.println("length::" + length);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //echerts
    @GetMapping("/pieshow")
    public ReturnResult pieshow() {
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> seriesData = new ArrayList<>();
        List<String> legendData = new ArrayList<>();

        List<ChrGoods> allGoods = goodsService.getAllGoods();
        for (ChrGoods allGood : allGoods) {
            legendData.add(allGood.getGoodsName());
            JSONObject seriesData_ = new JSONObject();
            seriesData_.put("name", allGood.getGoodsName());
            seriesData_.put("value", allGood.getGoodsPrice());
            seriesData.add(seriesData_);
        }
        jsonObject.put("legend", legendData);
        jsonObject.put("series", seriesData);
        return ReturnResult.create(jsonObject);
    }

    @GetMapping("/barshow")
    public ReturnResult barshow() {
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> seriesData = new ArrayList<>();
        List<String> legendData = new ArrayList<>();

        List<ChrGoods> allGoods = goodsService.getAllGoods();
        for (ChrGoods allGood : allGoods) {
            legendData.add(allGood.getGoodsName());
            JSONObject seriesData_ = new JSONObject();
            seriesData_.put("name", allGood.getGoodsName());
            seriesData_.put("value", allGood.getGoodsPrice());
            seriesData.add(seriesData_);
        }
        jsonObject.put("legend", legendData);
        jsonObject.put("series", seriesData);
        return ReturnResult.create(jsonObject);
    }
}
