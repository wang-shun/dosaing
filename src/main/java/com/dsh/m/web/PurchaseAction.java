package com.dsh.m.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dsh.m.dao.GoodsBclassMapper;
import com.dsh.m.dao.GoodsMapper;
import com.dsh.m.dao.GoodsSclassMapper;
import com.dsh.m.dao.PurchaseDetailMapper;
import com.dsh.m.model.Goods;
import com.dsh.m.model.GoodsBclass;
import com.dsh.m.model.GoodsBclassExample;
import com.dsh.m.model.GoodsExample;
import com.dsh.m.model.GoodsSclass;
import com.dsh.m.model.GoodsSclassExample;
import com.dsh.m.model.PurchaseDetail;
import com.dsh.m.model.PurchaseDetailExample;
import com.dsh.m.service.PurchaseService;
import com.dsh.m.util.redis.Cache;
import com.dsh.m.util.redis.Redis;

@RequestMapping("/purchase")
@Controller
public class PurchaseAction extends BaseAction {
	
	@Autowired
	private PurchaseDetailMapper purchaseDetailMapper;
	@Autowired
	private GoodsBclassMapper goodsBclassMapper;
	@Autowired
	private GoodsSclassMapper goodsSclassMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private PurchaseService purchaseService;
	
	@RequestMapping("/detail")
	public String detail(Integer purchaseid, ModelMap model) {
		PurchaseDetailExample example = new PurchaseDetailExample();
		example.createCriteria().andPurchaseidEqualTo(purchaseid);
		List<PurchaseDetail> details = purchaseDetailMapper.selectByExample(example);
		model.addAttribute("details", details);
		return "purchase/detail";
	}
	
	@RequestMapping("/input")
	public String input(ModelMap model) {
		List<GoodsBclass> bclasses = goodsBclassMapper.selectByExample(new GoodsBclassExample());
		Integer bclassid = bclasses.get(0).getBclassid();
		GoodsSclassExample sclassExample = new GoodsSclassExample();
		sclassExample.createCriteria().andBclassidEqualTo(bclassid);
		List<GoodsSclass> sclasses = goodsSclassMapper.selectByExample(sclassExample);
		Integer sclassid = sclasses.get(0).getSclassid();
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andSclassidEqualTo(sclassid);
		List<Goods> goods = goodsMapper.selectByExample(goodsExample);
		model.addAttribute("bclasses", bclasses);
		model.addAttribute("sclasses", sclasses);
		model.addAttribute("goods", goods);
		return "purchase/input";
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/loadsclass")
	public String loadsclass(Integer bclassid) {
		GoodsSclassExample sclassExample = new GoodsSclassExample();
		sclassExample.createCriteria().andBclassidEqualTo(bclassid);
		List<GoodsSclass> sclasses = goodsSclassMapper.selectByExample(sclassExample);
		Integer sclassid = sclasses.get(0).getSclassid();
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andSclassidEqualTo(sclassid);
		List<Goods> goods = goodsMapper.selectByExample(goodsExample);
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		map.put("sclasses", sclasses);
		map.put("goods", goods);
		return success(null, map);
	}
	
	@ResponseBody
	@RequestMapping("/loadgoods")
	public String loadgoods(Integer sclassid) {
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andSclassidEqualTo(sclassid);
		List<Goods> goods = goodsMapper.selectByExample(goodsExample);
		return success(null, goods);
	}
	
	@ResponseBody
	@RequestMapping("/confirmDetail")
	public String confirmDetail(HttpServletRequest request) {
		Integer goodsid = super.getParaToInt(request, "goodsid");
		BigDecimal amount = new BigDecimal(request.getParameter("amount"));
		BigDecimal unitPrice = new BigDecimal(request.getParameter("unitPrice"));
		String remark = request.getParameter("beizhu");
		Integer userid = super.getUserId(request.getSession());
		
		try {
			Cache cache = Redis.use();
			cache.getJedis().hset("purchasecart:"+userid, goodsid+"", amount.setScale(2, RoundingMode.CEILING).toString()
					+"|"+unitPrice.setScale(2, RoundingMode.CEILING).toString()+"|"+remark);
			return success("成功！！");
		} catch (Exception e) {
			e.printStackTrace();
			return fail("失败！！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/confirmPurchase")
	public String confirmPurchase(HttpSession session) {
		try {
			Integer purchaseid = purchaseService.savePurchase(super.getUserId(session));
			return success("成功！！", purchaseid);
		} catch (Exception e) {
			e.printStackTrace();
			return fail("失败！！");
		}
	}
	
	@ResponseBody
	@RequestMapping("/loadCart")
	public String loadCart(HttpSession session) {
		Cache cache = Redis.use();
		String key = "purchasecart:"+super.getUserId(session);
		Long num = cache.hlen(key);
		Set<String> keys = cache.hkeys(key);
		BigDecimal total = new BigDecimal(0);
		for(String k:keys) {
			String text = cache.hget(key, k);
			String[] strs = text.split("\\|");
			BigDecimal amount = new BigDecimal(strs[0]);
			BigDecimal unitPrice = new BigDecimal(strs[1]);
			total = total.add(amount.multiply(unitPrice));
		}
		JSONObject json = new JSONObject();
		json.put("num", num);
		json.put("total", total);
		return json.toString();
	}
	
	@RequestMapping("/cart")
	public String cart(HttpSession session, ModelMap model) {
		Cache cache = Redis.use();
		String key = "purchasecart:"+super.getUserId(session);
		Set<String> keys = cache.hkeys(key);
		JSONArray array = new JSONArray();
		for(String k:keys) {
			JSONObject json = new JSONObject();
			String text = cache.hget(key, k);
			String[] strs = text.split("\\|");
			BigDecimal amount = new BigDecimal(strs[0]);
			BigDecimal unitPrice = new BigDecimal(strs[1]);
			BigDecimal total = amount.multiply(unitPrice);
			json.put("goodsid", k);
			json.put("amount", amount.setScale(2, RoundingMode.CEILING).toString());
			json.put("unitprice", unitPrice.setScale(2, RoundingMode.CEILING).toString());
			json.put("total", total.setScale(2, RoundingMode.CEILING).toString());
			array.add(json);
		}
		model.addAttribute("goods", array);
		return "purchase/cart";
	}
	
	@ResponseBody
	@RequestMapping("/cartdel")
	public String cartdel(HttpSession session, Integer goodsid) {
		Cache cache = Redis.use();
		String key = "purchasecart:"+super.getUserId(session);
		try {
			cache.getJedis().hdel(key, goodsid+"");
			return success("删除成功！！");
		} catch (Exception e) {
			e.printStackTrace();
			return fail("删除失败！！");
		}
	}
	
}