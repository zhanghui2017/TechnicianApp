package com.gengli.technician.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gengli.technician.bean.City;
import com.gengli.technician.bean.Province;

import android.content.Context;
import android.util.Log;

public class GetProvinceData {
	//获取json对象
	public static JSONObject getJsonObject(String url,Context context) throws Exception{
		InputStream inputStream = context.getResources().getAssets().open(url);
		byte [] buffer = new byte[inputStream.available()+1] ; 
		inputStream.read(buffer);
		String json = new String (buffer,"utf-8");
		JSONObject object = new JSONObject(json);
		return object;
	}

	/*获取城市*/
	public static Map<String, List> getData(JSONObject object) throws JSONException{
		List<Province> list_province = new ArrayList<Province>();
		List<City> list_Cities = new ArrayList<City>();

		JSONArray data = object.getJSONArray("Data");
		for(int j=0; j<data.length(); j++){
			//外层循环解析省份
			JSONObject provinceJsonObject = data.getJSONObject(j);
			String id = provinceJsonObject.getString("Id");
			String name = provinceJsonObject.getString("Name");
			
			Province province = new Province();
			province.setProvince(name);
			province.setProvince_code(id);
			
			JSONArray subData = provinceJsonObject.getJSONArray("SubData");
			//内层循环解析城市
			List<City> city_province = new ArrayList<City>(); 
			for(int i = 0 ; i<subData.length() ; i++){
				//保持与外层循环一致，变量i不能搞错，如果写成j，则二级列表显示的会变成和一级列表一样的
				JSONObject cityJsonObject = subData.getJSONObject(i);
				String city_id = cityJsonObject.getString("Id");
				String city_name = cityJsonObject.getString("Name");

				City city = new City();
				city.setCity(city_name);
				city.setCity_code(city_id);
				city_province.add(city);
				province.setList(city_province);
				list_Cities.add(city);//城市一级数组集合
				//如果有县城了，再写个循环解析县城。
				//**************
			}
			list_province.add(province);//省份集合
		}

		Map<String, List> map = new HashMap<String, List>();
		map.put("provinces", list_province);
		map.put("citys",list_Cities);
//		Log.i("ALL", map+"");
		return map;
	}

	//获取省份集合
	public static List<Province> getProvincesList(Map<String, List> map){
		List<Province> provinces = new ArrayList<Province>();
		provinces = map.get("provinces");
		Log.i("Province", provinces+"");
		for(int i = 0 ; i < provinces.size() ; i ++){
			Log.i("Province", "--province:"+provinces.get(i).getProvince());
		}
		return provinces;
	}
	//获取省份集合
	public static List<City> getCityList(Map<String, List> map,int position){
		List<Province> provinces = new ArrayList<Province>();
		provinces = map.get("provinces");			 //获取城市列表集合
		
		List<City> cities = new ArrayList<City>();
		cities = provinces.get(position).getList();//得到对应的citylist
		Log.i("City", cities+"");
		return cities;
	}
	/**
	 * 区域名数组
	 * */
	public static String[] earo = {"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海",
		"江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南",
		"重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆"};
	/**
	 * 区域代码
	 * */
	public static String[] code = {"110000","120000","130000","140000","150000","210000","220000","230000","310000",
		"320000","330000","340000","350000","360000","370000","410000","420000","430000","440000","450000","460000",
		"500000","510000","520000","530000","540000","610000","620000","630000","640000","650000"};
	/**
	 * 设置固定区域集合
	 * */
	public static List<Province> getLocalProvinces(){
		List<Province> list = new ArrayList<Province>();
		for(int i = 0 ; i < earo.length; i ++){
			Province province = new Province();
			province.setProvince(earo[i]);
			province.setProvince_code(code[i]);
			list.add(province);
		}
		return list;
	}
	//-------------------测试---------
	/**
	 * 输出指定位数的随机数
	 * */
	public static String getCurentNum(int num){
		Random random = new Random();
		String result="";
		for(int i=0;i<num;i++){
			result+=random.nextInt(10);
		}
		System.out.print(result);
		return result;
	}
	/**
	 * 获取指定号段的随机号码
	 * */
	public static String getPhoneNum(String phone){
		return phone+getCurentNum(8);
	}
	/**
	 * 获取指定
	 * */
	public static List<String> getNumList(String phone){
		List<String> list = new ArrayList<String>();
		for(int i = 0 ; i < 50 ; i ++){
			list.add(getPhoneNum(phone));
		}
		return list;
	}
}
